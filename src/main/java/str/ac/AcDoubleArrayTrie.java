package str.ac;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * 基于双数组Trie树的AhoCorasick自动机
 * <p>
 * 该类是根据HanLP实现，在原本基础上进行修改，后续可以对内部代码优化，所以不继承
 */
public class AcDoubleArrayTrie<V> {
    /**
     * 双数组值check
     */
    protected int check[];
    /**
     * 双数组之base
     */
    protected int base[];
    /**
     * fail表
     */
    int fail[];
    /**
     * 输出表
     */
    int[][] output;
    /**
     * 保存value
     */
    protected V[] v;

    /**
     * 每个key的长度
     */
    protected int[] l;

    /**
     * base 和 check 的大小
     */
    protected int size;

    /**
     * 是否开启快速构建
     */
    private boolean enableFastBuild;

    public AcDoubleArrayTrie() {
    }

    /**
     * 开启快速构建，相比普通构建速度更快但内存占用微增，原理详见 https://github.com/hankcs/HanLP/issues/1801
     *
     * @param enableFastBuild 是否开启快速构建
     */
    public AcDoubleArrayTrie(boolean enableFastBuild) {
        this.enableFastBuild = enableFastBuild;
    }

    /**
     * 由一个词典创建
     *
     * @param dictionary 词典
     */
    public AcDoubleArrayTrie(TreeMap<String, V> dictionary) {
        build(dictionary);
    }

    /**
     * 匹配母文本
     *
     * @param text 一些文本
     * @return 一个pair列表
     */
    public List<Hit<V>> parseText(String text) {
        int position = 1;
        int currentState = 0;
        List<Hit<V>> collectedEmits = new LinkedList<Hit<V>>();
        for (int i = 0; i < text.length(); ++i) {
            currentState = getState(currentState, text.charAt(i));
            storeEmits(position, currentState, collectedEmits,text);
            ++position;
        }

        return collectedEmits;
    }

    /**
     * 处理文本
     *
     * @param text      文本
     * @param processor 处理器
     */
    public void parseText(String text, IHit<V> processor) {
        int position = 1;
        int currentState = 0;
        for (int i = 0; i < text.length(); ++i) {
            currentState = getState(currentState, text.charAt(i));
            int[] hitArray = output[currentState];
            if (hitArray != null) {
                for (int hit : hitArray) {
                    processor.hit(position - l[hit], position, v[hit]);
                }
            }
            ++position;
        }
    }

    /**
     * 处理文本
     *
     * @param text
     * @param processor
     */
    public void parseText(char[] text, IHit<V> processor) {
        int position = 1;
        int currentState = 0;
        for (char c : text) {
            currentState = getState(currentState, c);
            int[] hitArray = output[currentState];
            if (hitArray != null) {
                for (int hit : hitArray) {
                    processor.hit(position - l[hit], position, v[hit]);
                }
            }
            ++position;
        }
    }

    /**
     * 处理文本
     *
     * @param text
     * @param processor
     */
    public void parseText(char[] text, IHitFull<V> processor) {
        int position = 1;
        int currentState = 0;
        for (char c : text) {
            currentState = getState(currentState, c);
            int[] hitArray = output[currentState];
            if (hitArray != null) {
                for (int hit : hitArray) {
                    processor.hit(position - l[hit], position, v[hit], hit);
                }
            }
            ++position;
        }
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return
     */
    public V get(String key) {
        int index = exactMatchSearch(key);
        if (index >= 0) {
            return v[index];
        }

        return null;
    }

    /**
     * 更新某个键对应的值
     *
     * @param key   键
     * @param value 值
     * @return 是否成功（失败的原因是没有这个键）
     */
    public boolean set(String key, V value) {
        int index = exactMatchSearch(key);
        if (index >= 0) {
            v[index] = value;
            return true;
        }

        return false;
    }

    /**
     * 从值数组中提取下标为index的值<br>
     * 注意为了效率，此处不进行参数校验
     *
     * @param index 下标
     * @return 值
     */
    public V get(int index) {
        return v[index];
    }

    /**
     * 命中一个模式串的处理方法
     */
    public interface IHit<V> {
        /**
         * 命中一个模式串
         *
         * @param begin 模式串在母文本中的起始位置
         * @param end   模式串在母文本中的终止位置
         * @param value 模式串对应的值
         */
        void hit(int begin, int end, V value);
    }

    public interface IHitFull<V> {
        /**
         * 命中一个模式串
         *
         * @param begin 模式串在母文本中的起始位置
         * @param end   模式串在母文本中的终止位置
         * @param value 模式串对应的值
         * @param index 模式串对应的值的下标
         */
        void hit(int begin, int end, V value, int index);
    }

    /**
     * 一个命中结果
     *
     * @param <V>
     */
    public class Hit<V> {
        /**
         * 模式串在母文本中的起始位置
         */
        public int begin;
        /**
         * 模式串在母文本中的终止位置
         */
        public int end;
        /**
         * 模式串对应的值
         */
        public String key;
        /**
         * 模式串对应的值
         */
        public V value;

        public Hit(int begin, int end, V value,String key) {
            this.begin = begin;
            this.end = end;
            this.value = value;
            this.key = key;
        }

        @Override
        public String toString() {
            return String.format("[%d:%d]=%s-%s", key,begin, end, value);
        }
    }

    /**
     * 转移状态，支持failure转移
     *
     * @param currentState
     * @param character
     * @return
     */
    private int getState(int currentState, char character) {
        int newCurrentState = transitionWithRoot(currentState, character);  // 先按success跳转
        while (newCurrentState == -1) // 跳转失败的话，按failure跳转
        {
            currentState = fail[currentState];
            newCurrentState = transitionWithRoot(currentState, character);
        }
        return newCurrentState;
    }

    /**
     * 保存输出
     *
     * @param position
     * @param currentState
     * @param collectedEmits
     */
    private void storeEmits(int position, int currentState, List<Hit<V>> collectedEmits,String text) {
        int[] hitArray = output[currentState];
        if (hitArray != null) {
            for (int hit : hitArray) {
                collectedEmits.add(new Hit<V>(position - l[hit], position, v[hit],text.substring(position - l[hit],position)));
            }
        }
    }

    /**
     * 转移状态
     *
     * @param current
     * @param c
     * @return
     */
    protected int transition(int current, char c) {
        int b = current;
        int p;

        p = b + c + 1;
        if (b == check[p])
            b = base[p];
        else
            return -1;

        p = b;
        return p;
    }

    /**
     * c转移，如果是根节点则返回自己
     *
     * @param from
     * @param c
     * @return
     */
    protected int transitionWithRoot(int from, char c) {
        int b = base[from];
        int p;

        p = b + c + 1;
        if (b != check[p]) {
            if (from == 0) return 0;
            return -1;
        }

        return p;
    }


    /**
     * 由一个排序好的map创建
     */
    public void build(TreeMap<String, V> map) {
        new Builder().build(map);
    }

    /**
     * 获取直接相连的子节点
     *
     * @param parent   父节点
     * @param siblings （子）兄弟节点
     * @return 兄弟节点个数
     */
    private int fetch(State parent, List<Map.Entry<Integer, State>> siblings) {
        if (parent.isAcceptable()) {
            State fakeNode = new State(-(parent.getDepth() + 1));  // 此节点是parent的子节点，同时具备parent的输出
            fakeNode.addEmit(parent.getLargestValueId());
            siblings.add(new AbstractMap.SimpleEntry<Integer, State>(0, fakeNode));
        }
        for (Map.Entry<Character, State> entry : parent.getSuccess().entrySet()) {
            siblings.add(new AbstractMap.SimpleEntry<Integer, State>(entry.getKey() + 1, entry.getValue()));
        }
        return siblings.size();
    }

    /**
     * 精确匹配
     *
     * @param key 键
     * @return 值的下标
     */
    public int exactMatchSearch(String key) {
        return exactMatchSearch(key, 0, 0, 0);
    }

    /**
     * 精确匹配
     *
     * @param key
     * @param pos
     * @param len
     * @param nodePos
     * @return
     */
    private int exactMatchSearch(String key, int pos, int len, int nodePos) {
        if (len <= 0)
            len = key.length();
        if (nodePos <= 0)
            nodePos = 0;

        int result = -1;

        char[] keyChars = key.toCharArray();

        int b = base[nodePos];
        int p;

        for (int i = pos; i < len; i++) {
            p = b + (int) (keyChars[i]) + 1;
            if (b == check[p])
                b = base[p];
            else
                return result;
        }

        p = b;
        int n = base[p];
        if (b == check[p] && n < 0) {
            result = -n - 1;
        }
        return result;
    }

    /**
     * 精确查询
     *
     * @param keyChars 键的char数组
     * @param pos      char数组的起始位置
     * @param len      键的长度
     * @param nodePos  开始查找的位置（本参数允许从非根节点查询）
     * @return 查到的节点代表的value ID，负数表示不存在
     */
    private int exactMatchSearch(char[] keyChars, int pos, int len, int nodePos) {
        int result = -1;

        int b = base[nodePos];
        int p;

        for (int i = pos; i < len; i++) {
            p = b + (int) (keyChars[i]) + 1;
            if (b == check[p])
                b = base[p];
            else
                return result;
        }

        p = b;
        int n = base[p];
        if (b == check[p] && n < 0) {
            result = -n - 1;
        }
        return result;
    }

    /**
     * 一个顺序输出变量名与变量值的调试类
     */
    private static class DebugArray {
        Map<String, String> nameValueMap = new LinkedHashMap<String, String>();

        public void add(String name, int value) {
            String valueInMap = nameValueMap.get(name);
            if (valueInMap == null) {
                valueInMap = "";
            }

            valueInMap += " " + String.format("%5d", value);

            nameValueMap.put(name, valueInMap);
        }

        @Override
        public String toString() {
            String text = "";
            for (Map.Entry<String, String> entry : nameValueMap.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                text += String.format("%-5s", name) + "= " + value + '\n';
            }

            return text;
        }

        public void println() {
            System.out.print(this);
        }
    }

    /**
     * 大小，即包含多少个模式串
     *
     * @return
     */
    public int size() {
        return v == null ? 0 : v.length;
    }

    /**
     * 构建工具
     */
    private class Builder {
        /**
         * 根节点，仅仅用于构建过程
         */
        private State rootState = new State();
        /**
         * 是否占用，仅仅用于构建
         */
        private boolean used[];
        /**
         * 已分配在内存中的大小
         */
        private int allocSize;
        /**
         * 一个控制增长速度的变量
         */
        private int progress;
        /**
         * 下一个插入的位置将从此开始搜索
         */
        private int nextCheckPos;
        /**
         * 键值对的大小
         */
        private int keySize;

        /**
         * 由一个排序好的map创建
         */
        @SuppressWarnings("unchecked")
        public void build(TreeMap<String, V> map) {
            // 把值保存下来
            v = (V[]) map.values().toArray();
            l = new int[v.length];
            Set<String> keySet = map.keySet();
            // 构建二分trie树
            addAllKeyword(keySet);
            // 在二分trie树的基础上构建双数组trie树
            buildDoubleArrayTrie(keySet);
            used = null;
            // 构建failure表并且合并output表
            constructFailureStates();
            rootState = null;
            loseWeight();
        }

        /**
         * 添加一个键
         *
         * @param keyword 键
         * @param index   值的下标
         */
        private void addKeyword(String keyword, int index) {
            State currentState = this.rootState;
            for (Character character : keyword.toCharArray()) {
                currentState = currentState.addState(character);
            }
            currentState.addEmit(index);
            l[index] = keyword.length();
        }

        /**
         * 一系列键
         *
         * @param keywordSet
         */
        private void addAllKeyword(Collection<String> keywordSet) {
            int i = 0;
            for (String keyword : keywordSet) {
                addKeyword(keyword, i++);
            }
        }

        /**
         * 建立failure表
         */
        private void constructFailureStates() {
            fail = new int[size + 1];
            output = new int[size + 1][];
            Queue<State> queue = new LinkedBlockingDeque<State>();

            // 第一步，将深度为1的节点的failure设为根节点
            for (State depthOneState : this.rootState.getStates()) {
                depthOneState.setFailure(this.rootState, fail);
                queue.add(depthOneState);
                constructOutput(depthOneState);
            }

            // 第二步，为深度 > 1 的节点建立failure表，这是一个bfs
            while (!queue.isEmpty()) {
                State currentState = queue.remove();

                for (Character transition : currentState.getTransitions()) {
                    State targetState = currentState.nextState(transition);
                    queue.add(targetState);

                    State traceFailureState = currentState.failure();
                    while (traceFailureState.nextState(transition) == null) {
                        traceFailureState = traceFailureState.failure();
                    }
                    State newFailureState = traceFailureState.nextState(transition);
                    targetState.setFailure(newFailureState, fail);
                    targetState.addEmit(newFailureState.emit());
                    constructOutput(targetState);
                }
            }
        }

        /**
         * 建立output表
         */
        private void constructOutput(State targetState) {
            Collection<Integer> emit = targetState.emit();
            if (emit == null || emit.size() == 0) return;
            int output[] = new int[emit.size()];
            Iterator<Integer> it = emit.iterator();
            for (int i = 0; i < output.length; ++i) {
                output[i] = it.next();
            }
            AcDoubleArrayTrie.this.output[targetState.getIndex()] = output;
        }

        private void buildDoubleArrayTrie(Set<String> keySet) {
            progress = 0;
            keySize = keySet.size();
            resize(65536); // 32个双字节

            base[0] = 1;
            nextCheckPos = 0;

            State root_node = this.rootState;

            List<Map.Entry<Integer, State>> siblings = new ArrayList<Map.Entry<Integer, State>>(root_node.getSuccess().entrySet().size());
            fetch(root_node, siblings);
            if (siblings.isEmpty())
                Arrays.fill(check, -1); // fill -1 such that no transition is allowed
            else
                insert(siblings);
        }

        /**
         * 拓展数组
         *
         * @param newSize
         * @return
         */
        private int resize(int newSize) {
            int[] base2 = new int[newSize];
            int[] check2 = new int[newSize];
            boolean used2[] = new boolean[newSize];
            if (allocSize > 0) {
                System.arraycopy(base, 0, base2, 0, allocSize);
                System.arraycopy(check, 0, check2, 0, allocSize);
                System.arraycopy(used, 0, used2, 0, allocSize);
            }

            base = base2;
            check = check2;
            used = used2;

            return allocSize = newSize;
        }

        /**
         * 插入节点
         *
         * @param siblings 等待插入的兄弟节点
         * @return 插入位置
         */
        private int insert(List<Map.Entry<Integer, State>> siblings) {
            int begin = 0;
            int pos = Math.max(siblings.get(0).getKey() + 1, enableFastBuild ? (nextCheckPos + 1) : nextCheckPos) - 1;
            int nonzero_num = 0;
            int first = 0;

            if (allocSize <= pos)
                resize(pos + 1);

            outer:
            // 此循环体的目标是找出满足base[begin + a1...an]  == 0的n个空闲空间,a1...an是siblings中的n个节点
            while (true) {
                pos++;

                if (allocSize <= pos)
                    resize(pos + 1);

                if (check[pos] != 0) {
                    nonzero_num++;
                    continue;
                } else if (first == 0) {
                    nextCheckPos = pos;
                    first = 1;
                }

                begin = pos - siblings.get(0).getKey(); // 当前位置离第一个兄弟节点的距离
                if (allocSize <= (begin + siblings.get(siblings.size() - 1).getKey())) {
                    // progress can be zero // 防止progress产生除零错误
//                    double l = (1.05 > 1.0 * keySize / (progress + 1)) ? 1.05 : 1.0 * keySize / (progress + 1);
                    resize((int) (allocSize * 2));
                }

                if (used[begin])
                    continue;

                for (int i = 1; i < siblings.size(); i++)
                    if (check[begin + siblings.get(i).getKey()] != 0)
                        continue outer;

                break;
            }

            // -- Simple heuristics --
            // if the percentage of non-empty contents in check between the
            // index
            // 'next_check_pos' and 'check' is greater than some constant value
            // (e.g. 0.9),
            // new 'next_check_pos' index is written by 'check'.
            if (1.0 * nonzero_num / (pos - nextCheckPos + 1) >= 0.95)
                nextCheckPos = pos; // 从位置 next_check_pos 开始到 pos 间，如果已占用的空间在95%以上，下次插入节点时，直接从 pos 位置处开始查找
            used[begin] = true;

            size = (size > begin + siblings.get(siblings.size() - 1).getKey() + 1) ? size : begin + siblings.get(siblings.size() - 1).getKey() + 1;

            for (Map.Entry<Integer, State> sibling : siblings) {
                check[begin + sibling.getKey()] = begin;
            }

            for (Map.Entry<Integer, State> sibling : siblings) {
                List<Map.Entry<Integer, State>> new_siblings = new ArrayList<Map.Entry<Integer, State>>(sibling.getValue().getSuccess().entrySet().size() + 1);

                if (fetch(sibling.getValue(), new_siblings) == 0)  // 一个词的终止且不为其他词的前缀，其实就是叶子节点
                {
                    base[begin + sibling.getKey()] = (-sibling.getValue().getLargestValueId() - 1);
                    progress++;
                } else {
                    int h = insert(new_siblings);   // dfs
                    base[begin + sibling.getKey()] = h;
                }
                sibling.getValue().setIndex(begin + sibling.getKey());
            }
            return begin;
        }

        /**
         * 释放空闲的内存
         */
        private void loseWeight() {
            int nbase[] = new int[size + 65535];
            System.arraycopy(base, 0, nbase, 0, size);
            base = nbase;

            int ncheck[] = new int[size + 65535];
            System.arraycopy(check, 0, ncheck, 0, Math.min(check.length, ncheck.length));
            check = ncheck;
        }
    }

    /**
     * 出现最长的模式串
     *
     * @param text
     * @return
     */
    public List<String> getMostLong(String text) {
        List<String> res = new ArrayList<>();
        int maxLen = 0;
        Map<Integer, Integer> map = new HashMap<>();

        int position = 1;
        int currentState = 0;
        for (int i = 0; i < text.length(); ++i) {
            currentState = getState(currentState, text.charAt(i));
            int[] hitArray = output[currentState];
            if (hitArray != null) {

                for (int hit : hitArray) {

                    if (l[hit] == maxLen) {
                        map.put(hit, position);
                    } else if (l[hit] > maxLen) {
                        maxLen = l[hit];
                        map.clear();
                        map.put(hit, position);
                    }
                }
            }
            ++position;
        }

        map.forEach((x, y) -> {
            res.add(v[x].toString());
        });

        return res;
    }

    /**
     * 出现次数最多的模式串
     *
     * @param text
     * @return
     */
    public List<String> getMostOccurrences(String text) {
        List<String> res = new ArrayList<>();
        int maxCount = 0;
        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, Integer> positionMap = new HashMap<>();

        int position = 1;
        int currentState = 0;
        for (int i = 0; i < text.length(); ++i) {
            currentState = getState(currentState, text.charAt(i));
            int[] hitArray = output[currentState];
            if (hitArray != null) {

                for (int hit : hitArray) {
                    Integer num = map.get(hit);
                    int newNum = 0;
                    if (num != null) {
                        newNum = num + 1;
                    } else {
                        newNum = 1;
                    }

                    map.put(hit, newNum);

                    if (newNum == maxCount) {
                        positionMap.put(hit, position);
                    } else if (newNum > maxCount) {
                        maxCount = newNum;
                        positionMap.clear();
                        positionMap.put(hit, position);
                    }


                }
            }
            ++position;
        }


        positionMap.forEach((x, y) -> {
            res.add(v[x].toString());
        });

        return res;
    }


    /**
     * 按最长的匹配，然后剔除继续匹配，最终返回，匹配的所有模式串
     *
     * @param text
     * @return
     */
    public List<Hit<V>> getMostLongEliminate(String text) {

        List<Hit<V>> res = new ArrayList<>();
        //匹配到的所有模式串
        List<Hit<V>> hits = this.parseText(text);

        if (CollectionUtils.isEmpty(hits)) {
            return res;
        }


        if (hits.size() == 1) {
            res.add(hits.get(0));
            return res;
        }


        hits = hits.stream()
                .sorted(Comparator.<Hit, Integer>comparing(x -> x.begin - x.end).thenComparing(x -> x.begin))
                .collect(Collectors.toList());


        int preEnd = 0;
        int preStart = text.length();
        for (Hit hit : hits) {
            int begin = hit.begin;
            int end = hit.end;
            if (end > preStart && begin < preEnd) {
                continue;
            }
            if (preStart > begin) {
                preStart = hit.begin;
            }
            if (preEnd < end) {
                preEnd = hit.end;
            }

            res.add(hit);
        }


        return res;
    }
}
