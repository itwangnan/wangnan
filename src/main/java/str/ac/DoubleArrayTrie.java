package str.ac;

/**
 * DoubleArrayTrie: Java implementation of Darts (Double-ARray Trie System)
 * <p/>
 * <p>
 * Copyright(C) 2001-2007 Taku Kudo &lt;taku@chasen.org&gt;<br />
 * Copyright(C) 2009 MURAWAKI Yugo &lt;murawaki@nlp.kuee.kyoto-u.ac.jp&gt;
 * Copyright(C) 2012 KOMIYA Atsushi &lt;komiya.atsushi@gmail.com&gt;
 * </p>
 * <p/>
 * <p>
 * The contents of this file may be used under the terms of either of the GNU
 * Lesser General Public License Version 2.1 or later (the "LGPL"), or the BSD
 * License (the "BSD").
 * </p>
 */

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;

import java.io.Serializable;
import java.util.*;

/**
 * 双数组Trie树
 * 理论是check[base[s] + c] = s 但是这里是check[base[s] + c] = base[s] ,存的直接base值而非索引（可以简介减少一次访问）
 *
 * check[base[s] + c] = base[s]
 * base[s] + c = base[t] or 结束是key的下标
 */
public class DoubleArrayTrie<V> implements Serializable {
    private final static int BUF_SIZE = 16384;
    private final static int UNIT_SIZE = 8; // size of int + int

    private static class Node {
        int code;
        int depth;
        int left;
        int right;

        @Override
        public String toString() {
            return "Node{" +
                    "code=" + code +
                    ", depth=" + depth +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    ;

    protected int check[];
    protected int base[];

    /**
     * base 和 check 的大小
     */
    protected int size;
    private int allocSize;
    private List<String> key;
    private int keySize;
    private int length[];
    private int value[];
    protected V[] v;
    private int progress;
    private int nextCheckPos;
    // boolean no_delete_;
    int error_;

    // int (*progressfunc_) (size_t, size_t);

    // inline _resize expanded

    /**
     * 是否开启快速构建
     */
    private boolean enableFastBuild;

    /**
     * 拓展数组
     *
     * @param newSize
     * @return
     */
    private int resize(int newSize) {
        int[] base2 = new int[newSize];
        int[] check2 = new int[newSize];
        if (allocSize > 0) {
            System.arraycopy(base, 0, base2, 0, allocSize);
            System.arraycopy(check, 0, check2, 0, allocSize);
        }

        base = base2;
        check = check2;

        return allocSize = newSize;
    }

    /**
     * 获取直接相连的子节点
     *
     * @param parent   父节点
     * @param siblings （子）兄弟节点
     * @return 兄弟节点个数
     */
    private int fetch(Node parent, List<Node> siblings) {
        if (error_ < 0)
            return 0;

        int prev = 0;

        for (int i = parent.left; i < parent.right; i++) {
            if ((length != null ? length[i] : key.get(i).length()) < parent.depth)
                continue;

            String tmp = key.get(i);

            int cur = 0;
            if ((length != null ? length[i] : tmp.length()) != parent.depth)
                cur = (int) tmp.charAt(parent.depth) + 1;

            if (prev > cur) {
                error_ = -3;
                return 0;
            }

            if (cur != prev || siblings.size() == 0) {
                Node tmp_node = new Node();
                tmp_node.depth = parent.depth + 1;
                tmp_node.code = cur;
                tmp_node.left = i;
                if (siblings.size() != 0)
                    siblings.get(siblings.size() - 1).right = i;

                siblings.add(tmp_node);
            }

            prev = cur;
        }

        if (siblings.size() != 0)
            siblings.get(siblings.size() - 1).right = parent.right;

        return siblings.size();
    }

    /**
     * 插入节点
     *
     * @param siblings 等待插入的兄弟节点
     * @return 插入位置
     */
    private int insert(List<Node> siblings, BitSet used) {
        if (error_ < 0)
            return 0;

        //base[s]
        int begin = 0;
        //找check[begin + a1...an]的当前位置
        // siblings.get(0).code + 1
        //enableFastBuild 开启了快速构建则nextCheckPos + 1 否则nextCheckPos
        //最后在-1
        int pos = Math.max(siblings.get(0).code + 1, nextCheckPos) - 1;
        int nonzero_num = 0;
        int first = 0;

        if (allocSize <= pos)
            resize(pos + 1);

        outer:
        // 此循环体的目标是找出满足check[begin + a1...an]  == 0的n个空闲空间,a1...an是siblings中的n个节点
        while (true) {
            pos++;

            if (allocSize <= pos)
                resize(pos + 1);

            //check[pos] 是第一个下标的选取，如果直接有值，直接重找
            if (check[pos] != 0) {
                nonzero_num++;
                continue;
            }
            else if (first == 0) {
                //第一次记录pos 位置
                nextCheckPos = pos;
                first = 1;
            }

            //这里为什么要选择第一个节点作为基准点，是因为双数组的设计就是为了让结构更加紧凑，如果选择最后一个节点做基准点，
            // 肯定冲突更少，也更容易找到，但是结构就没那么紧凑了，pos代表第一个兄弟节点尝试check[pos]
            begin = pos - siblings.get(0).code; // 当前位置离第一个兄弟节点的距离

            //距离加上最后一个节点 （最长距离）大于等于目前 数组大小，进行扩容65535
            if (allocSize <= (begin + siblings.get(siblings.size() - 1).code)) {
                resize(begin + siblings.get(siblings.size() - 1).code + Character.MAX_VALUE);
            }

            //if (used[begin])
            //   continue;
            if (used.get(begin)) {
                continue;
            }

            //满足check[begin + a1...an]  == 0
            for (int i = 1; i < siblings.size(); i++)
                if (check[begin + siblings.get(i).code] != 0)
                    continue outer;

            break;
        }

        //--简单启发法--

        //如果索引“next_check_pos”和“check”之间check中非空内容的百分比大于某个常量值（例如0.9），则新的“next_check_pos”索引由“check”写入。
        //这步保证下一次找满足的begin不会太费时
        if (1.0 * nonzero_num / (pos - nextCheckPos + 1) >= 0.95)
            //第一次记录pos 位置
            nextCheckPos = pos; // 从位置 next_check_pos 开始到 pos 间，如果已占用的空间在95%以上，下次插入节点时，直接从 pos 位置处开始查找

        //used[begin] = true;
        //该步骤也是加快找满足的begin，因为check[begin] 就是第一个点
        used.set(begin);

        //更新有值的最大size，
        size = (size > begin + siblings.get(siblings.size() - 1).code + 1) ? size
                : begin + siblings.get(siblings.size() - 1).code + 1;

        //这里的begin满足check[begin + a1...an]==0。直接进行修改

        for (int i = 0; i < siblings.size(); i++) {
            //check[base[s] + c] = base[s]
            check[begin + siblings.get(i).code] = begin;
        }

        //处理子节点的子节点
        for (int i = 0; i < siblings.size(); i++) {
            List<Node> new_siblings = new ArrayList<Node>();


            // 一个词的终止且不为其他词的前缀
            if (fetch(siblings.get(i), new_siblings) == 0) {
                //base[base[s] + c] = t , -siblings.get(i).left - 1 。如果有value[]那就

                //结束存的是匹配串的下标
                base[begin + siblings.get(i).code] =
                        (value != null) ? (-value[siblings.get(i).left] - 1) : (-siblings.get(i).left - 1);

                if (value != null && (- value[siblings.get(i).left] - 1) >= 0) {
                    error_ = -2;
                    return 0;
                }

                progress++;
                // if (progress_func_) (*progress_func_) (progress,
                // keySize);
            } else {
                //节点的子节点，h为子节点第一个点的开始
                int h = insert(new_siblings, used);   // dfs
                //base[base[s] + c] = t 下个节点的基准点位置
                //非结束存的是check(next)的偏移量
                base[begin + siblings.get(i).code] = h;
            }
        }

        //第一个点的开始
        return begin;
    }






    public DoubleArrayTrie() {
        check = null;
        base = null;
        size = 0;
        allocSize = 0;
        // no_delete_ = false;
        error_ = 0;
    }

    /**
     * 开启快速构建，相比普通构建速度更快但内存占用微增，原理详见 https://github.com/hankcs/HanLP/issues/1801
     *
     * @param enableFastBuild 是否开启快速构建
     */
    public DoubleArrayTrie(boolean enableFastBuild) {
        this();
        this.enableFastBuild = enableFastBuild;
    }

    /**
     * 从TreeMap构造
     *
     * @param buildFrom
     */
    public DoubleArrayTrie(TreeMap<String, V> buildFrom) {
        this();
        if (build(buildFrom) != 0) {
            throw new IllegalArgumentException("构造失败");
        }
    }

    // no deconstructor

    // set_result omitted
    // the search methods returns (the list of) the value(s) instead
    // of (the list of) the pair(s) of value(s) and length(s)

    // set_array omitted
    // array omitted

    void clear() {
        // if (! no_delete_)
        check = null;
        base = null;
        allocSize = 0;
        size = 0;
        // no_delete_ = false;
    }

    public int getUnitSize() {
        return UNIT_SIZE;
    }

    public int getSize() {
        return size;
    }

    public int getTotalSize() {
        return size * UNIT_SIZE;
    }

    public int getNonzeroSize() {
        int result = 0;
        for (int i = 0; i < check.length; ++i)
            if (check[i] != 0)
                ++result;
        return result;
    }

    public int build(List<String> key, List<V> value) {
        assert key.size() == value.size() : "键的个数与值的个数不一样！";
        assert key.size() > 0 : "键值个数为0！";
        v = (V[]) value.toArray();
        return build(key, null, null, key.size());
    }

    public int build(List<String> key, V[] value) {
        assert key.size() == value.length : "键的个数与值的个数不一样！";
        assert key.size() > 0 : "键值个数为0！";
        v = value;
        return build(key, null, null, key.size());
    }

    /**
     * 构建DAT
     *
     * @param entrySet 注意此entrySet一定要是字典序的！否则会失败
     * @return
     */
    public int build(Set<Map.Entry<String, V>> entrySet) {
        List<String> keyList = new ArrayList<String>(entrySet.size());
        List<V> valueList = new ArrayList<V>(entrySet.size());
        for (Map.Entry<String, V> entry : entrySet) {
            String key = entry.getKey();
            if (key.isEmpty())
                continue;
            keyList.add(key);
            valueList.add(entry.getValue());
        }

        return build(keyList, valueList);
    }

    /**
     * 方便地构造一个双数组trie树
     *
     * @param keyValueMap 升序键值对map
     * @return 构造结果
     */
    public int build(TreeMap<String, V> keyValueMap) {
        assert keyValueMap != null;
        Set<Map.Entry<String, V>> entrySet = keyValueMap.entrySet();
        return build(entrySet);
    }

    /**
     * 唯一的构建方法
     *
     * @param _key     值set，必须字典序
     * @param _length  对应每个key的长度，留空动态获取
     * @param _value   每个key对应的值，留空使用key的下标作为值
     * @param _keySize key的长度，应该设为_key.size
     * @return 是否出错
     */
    public int build(List<String> _key, int _length[], int _value[],
                     int _keySize) {
        if (_key == null || _keySize > _key.size())
            return 0;

        // progress_func_ = progress_func;
        key = _key;
        length = _length;
        keySize = _keySize;
        value = _value;
        progress = 0;
        allocSize = 0;

        resize(65536 * 32); // 32个双字节

        base[0] = 1;
        nextCheckPos = 0;

        Node root_node = new Node();
        root_node.left = 0;
        root_node.right = keySize;
        root_node.depth = 0;

        List<Node> siblings = new ArrayList<Node>();
        fetch(root_node, siblings);
        insert(siblings, new BitSet());
        shrink();

        // size += (1 << 8 * 2) + 1; // ???
        // if (size >= allocSize) resize (size);

        key = null;
        length = null;

        return error_;
    }


    /**
     * 精确匹配
     *
     * @param key 键
     * @return 值
     */
    public int exactMatchSearch(String key) {
        return exactMatchSearch(key, 0, 0, 0);
    }

    public int exactMatchSearch(String key, int pos, int len, int nodePos) {
        if (len <= 0)
            len = key.length();
        if (nodePos <= 0)
            nodePos = 0;

        int result = -1;

        int b = base[nodePos];
        int p;

        for (int i = pos; i < len; i++) {
            p = b + (int) (key.charAt(i)) + 1;
//            System.out.println(String.format("exactMatchSearch b:%s p:%s",b,p));
            if (b == check[p])
                b = base[p];
            else
                return result;
        }

        p = b;
        int n = base[p];
        if (b == check[p] && n < 0) {
            result = - n - 1;
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
    public int exactMatchSearch(char[] keyChars, int pos, int len, int nodePos) {
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
            result = - n - 1;
        }
        return result;
    }

    public List<Integer> commonPrefixSearch(String key) {
        return commonPrefixSearch(key, 0, 0, 0);
    }

    /**
     * 前缀查询
     *
     * @param key     查询字串
     * @param pos     字串的开始位置
     * @param len     字串长度
     * @param nodePos base中的开始位置
     * @return 一个含有所有下标的list
     */
    public List<Integer> commonPrefixSearch(String key, int pos, int len, int nodePos) {
        if (len <= 0)
            len = key.length();
        if (nodePos <= 0)
            nodePos = 0;

        List<Integer> result = new ArrayList<Integer>();

        char[] keyChars = key.toCharArray();

        int b = base[nodePos];
        int n;
        int p;

        for (int i = pos; i < len; i++) {
            p = b + (int) (keyChars[i]) + 1;    // 状态转移 p = base[char[i-1]] + char[i] + 1
            if (b == check[p])                  // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
                b = base[p];
            else
                return result;
            p = b;
            n = base[p];
            if (b == check[p] && n < 0)         // base[p] == check[p] && base[p] < 0 查到一个词
            {
                result.add(-n - 1);
            }
        }

        return result;
    }

    /**
     * 前缀查询，包含值
     *
     * @param key 键
     * @return 键值对列表
     * @deprecated 最好用优化版的
     */
    public LinkedList<Map.Entry<String, V>> commonPrefixSearchWithValue(String key) {
        int len = key.length();
        LinkedList<Map.Entry<String, V>> result = new LinkedList<Map.Entry<String, V>>();
        char[] keyChars = key.toCharArray();
        int b = base[0];
        int n;
        int p;

        for (int i = 0; i < len; ++i) {
            p = b;
            n = base[p];
            if (b == check[p] && n < 0)         // base[p] == check[p] && base[p] < 0 查到一个词
            {
                result.add(new AbstractMap.SimpleEntry<String, V>(new String(keyChars, 0, i), v[-n - 1]));
            }

            p = b + (int) (keyChars[i]) + 1;    // 状态转移 p = base[char[i-1]] + char[i] + 1
            // 下面这句可能产生下标越界，不如改为if (p < size && b == check[p])，或者多分配一些内存
            if (b == check[p])                  // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
                b = base[p];
            else
                return result;
        }

        p = b;
        n = base[p];

        if (b == check[p] && n < 0) {
            result.add(new AbstractMap.SimpleEntry<String, V>(key, v[-n - 1]));
        }

        return result;
    }

    /**
     * 优化的前缀查询，可以复用字符数组
     *
     * @param keyChars
     * @param begin
     * @return
     */
    public LinkedList<Map.Entry<String, V>> commonPrefixSearchWithValue(char[] keyChars, int begin) {
        int len = keyChars.length;
        LinkedList<Map.Entry<String, V>> result = new LinkedList<Map.Entry<String, V>>();
        int b = base[0];
        int n;
        int p;

        for (int i = begin; i < len; ++i) {
            p = b;
            n = base[p];
            if (b == check[p] && n < 0)         // base[p] == check[p] && base[p] < 0 查到一个词
            {
                result.add(new AbstractMap.SimpleEntry<String, V>(new String(keyChars, begin, i - begin), v[-n - 1]));
            }

            p = b + (int) (keyChars[i]) + 1;    // 状态转移 p = base[char[i-1]] + char[i] + 1
            // 下面这句可能产生下标越界，不如改为if (p < size && b == check[p])，或者多分配一些内存
            if (b == check[p])                  // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
                b = base[p];
            else
                return result;
        }

        p = b;
        n = base[p];

        if (b == check[p] && n < 0) {
            result.add(new AbstractMap.SimpleEntry<String, V>(new String(keyChars, begin, len - begin), v[-n - 1]));
        }

        return result;
    }

    @Override
    public String toString() {
//        String infoIndex    = "i    = ";
//        String infoChar     = "char = ";
//        String infoBase     = "base = ";
//        String infoCheck    = "check= ";
//        for (int i = 0; i < base.length; ++i)
//        {
//            if (base[i] != 0 || check[i] != 0)
//            {
//                infoChar  += "    " + (i == check[i] ? " ×" : (char)(i - check[i] - 1));
//                infoIndex += " " + String.format("%5d", i);
//                infoBase  += " " +  String.format("%5d", base[i]);
//                infoCheck += " " + String.format("%5d", check[i]);
//            }
//        }
        return "DoubleArrayTrie{" +
//                "\n" + infoChar +
//                "\n" + infoIndex +
//                "\n" + infoBase +
//                "\n" + infoCheck + "\n" +
//                "check=" + Arrays.toString(check) +
//                ", base=" + Arrays.toString(base) +
//                ", used=" + Arrays.toString(used) +
                "size=" + size +
                ", allocSize=" + allocSize +
                ", key=" + key +
                ", keySize=" + keySize +
//                ", length=" + Arrays.toString(length) +
//                ", value=" + Arrays.toString(value) +
                ", progress=" + progress +
                ", nextCheckPos=" + nextCheckPos +
                ", error_=" + error_ +
                '}';
    }

    /**
     * 树叶子节点个数
     *
     * @return
     */
    public int size() {
        return v.length;
    }

    /**
     * 获取check数组引用，不要修改check
     *
     * @return
     */
    public int[] getCheck() {
        return check;
    }

    /**
     * 获取base数组引用，不要修改base
     *
     * @return
     */
    public int[] getBase() {
        return base;
    }

    /**
     * 获取index对应的值
     *
     * @param index
     * @return
     */
    public V getValueAt(int index) {
        return v[index];
    }

    /**
     * 精确查询
     *
     * @param key 键
     * @return 值
     */
    public V get(String key) {
        int index = exactMatchSearch(key);
        if (index >= 0) {
            return getValueAt(index);
        }

        return null;
    }

    public V get(char[] key) {
        int index = exactMatchSearch(key, 0, key.length, 0);
        if (index >= 0) {
            return getValueAt(index);
        }

        return null;
    }

    public V[] getValueArray(V[] a) {
        // I hate this but just have to
        int size = v.length;
        if (a.length < size)
            a = (V[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        System.arraycopy(v, 0, a, 0, size);
        return a;
    }

    public boolean containsKey(String key) {
        return exactMatchSearch(key) >= 0;
    }

    /**
     * 沿着路径转移状态
     *
     * @param path
     * @return
     */
    protected int transition(String path) {
        return transition(path.toCharArray());
    }

    /**
     * 沿着节点转移状态
     *
     * @param path
     * @return
     */
    protected int transition(char[] path) {
        int b = base[0];
        int p;

        for (int i = 0; i < path.length; ++i) {
            p = b + (int) (path[i]) + 1;
            if (b == check[p])
                b = base[p];
            else
                return -1;
        }

        p = b;
        return p;
    }

    /**
     * 沿着路径转移状态
     *
     * @param path 路径
     * @param from 起点（根起点为base[0]=1）
     * @return 转移后的状态（双数组下标）
     */
    public int transition(String path, int from) {
        int b = from;
        int p;

        for (int i = 0; i < path.length(); ++i) {
            p = b + (int) (path.charAt(i)) + 1;
            if (b == check[p])
                b = base[p];
            else
                return -1;
        }

        p = b;
        return p;
    }

    /**
     * 转移状态
     *
     * @param c
     * @param from
     * @return
     */
    public int transition(char c, int from) {
        int b = from;
        int p;

        p = b + (int) (c) + 1;
        if (b == check[p])
            b = base[p];
        else
            return -1;

        return b;
    }

    /**
     * 检查状态是否对应输出
     *
     * @param state 双数组下标
     * @return 对应的值，null表示不输出
     */
    public V output(int state) {
        if (state < 0) return null;
        int n = base[state];
        if (state == check[state] && n < 0) {
            return v[-n - 1];
        }
        return null;
    }

    /**
     * 一个搜索工具（注意，当调用next()返回false后不应该继续调用next()，除非reset状态）
     */
    public class Searcher {
        /**
         * key的起点
         */
        public int begin;
        /**
         * key的长度
         */
        public int length;
        /**
         * key的字典序坐标
         */
        public int index;
        /**
         * key对应的value
         */
        public V value;
        /**
         * 传入的字符数组
         */
        private char[] charArray;
        /**
         * 上一个node位置
         */
        private int last;
        /**
         * 上一个字符的下标
         */
        private int i;
        /**
         * charArray的长度，效率起见，开个变量
         */
        private int arrayLength;

        /**
         * 构造一个双数组搜索工具
         *
         * @param offset    搜索的起始位置
         * @param charArray 搜索的目标字符数组
         */
        public Searcher(int offset, char[] charArray) {
            this.charArray = charArray;
            i = offset;
            last = base[0];
            arrayLength = charArray.length;
            // A trick，如果文本长度为0的话，调用next()时，会带来越界的问题。
            // 所以我要在第一次调用next()的时候触发begin == arrayLength进而返回false。
            // 当然也可以改成begin >= arrayLength，不过我觉得操作符>=的效率低于==
            if (arrayLength == 0) begin = -1;
            else begin = offset;
        }

        /**
         * 取出下一个命中输出
         *
         * @return 是否命中，当返回false表示搜索结束，否则使用公开的成员读取命中的详细信息
         */
        public boolean next() {
            int b = last;
            int n;
            int p;

            for (; ; ++i) {
                if (i == arrayLength)               // 指针到头了，将起点往前挪一个，重新开始，状态归零
                {
                    ++begin;
                    if (begin == arrayLength) break;
                    i = begin;
                    b = base[0];
                }
                p = b + (int) (charArray[i]) + 1;   // 状态转移 p = base[char[i-1]] + char[i] + 1
                if (b == check[p])                  // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
                    b = base[p];                    // 转移成功
                else {
                    i = begin;                      // 转移失败，也将起点往前挪一个，重新开始，状态归零
                    ++begin;
                    if (begin == arrayLength) break;
                    b = base[0];
                    continue;
                }
                p = b;
                n = base[p];
                if (b == check[p] && n < 0)         // base[p] == check[p] && base[p] < 0 查到一个词
                {
                    length = i - begin + 1;
                    index = -n - 1;
                    value = v[index];
                    last = b;
                    ++i;
                    return true;
                }
            }

            return false;
        }
    }

    public Searcher getSearcher(String text) {
        return getSearcher(text, 0);
    }

    public Searcher getSearcher(String text, int offset) {
        return new Searcher(offset, text.toCharArray());
    }

    public Searcher getSearcher(char[] text, int offset) {
        return new Searcher(offset, text);
    }

    /**
     * 一个最长搜索工具（注意，当调用next()返回false后不应该继续调用next()，除非reset状态）
     */
    public class LongestSearcher {
        /**
         * key的起点
         */
        public int begin;
        /**
         * key的长度
         */
        public int length;
        /**
         * key的字典序坐标
         */
        public int index;
        /**
         * key对应的value
         */
        public V value;
        /**
         * 传入的字符数组
         */
        private char[] charArray;
        /**
         * 上一个字符的下标
         */
        private int i;
        /**
         * charArray的长度，效率起见，开个变量
         */
        private int arrayLength;

        /**
         * 构造一个双数组搜索工具
         *
         * @param offset    搜索的起始位置
         * @param charArray 搜索的目标字符数组
         */
        public LongestSearcher(int offset, char[] charArray) {
            this.charArray = charArray;
            i = offset;
            arrayLength = charArray.length;
            begin = offset;
        }

        /**
         * 取出下一个命中输出
         *
         * @return 是否命中，当返回false表示搜索结束，否则使用公开的成员读取命中的详细信息
         */
        public boolean next() {
            length = 0;
            begin = i;
            int b = base[0];
            int n;
            int p;

            for (; ; ++i) {
                if (i >= arrayLength)               // 指针到头了，将起点往前挪一个，重新开始，状态归零
                {
                    return length > 0;
                }
                p = b + (int) (charArray[i]) + 1;   // 状态转移 p = base[char[i-1]] + char[i] + 1
                if (b == check[p])                  // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
                    b = base[p];                    // 转移成功
                else {
                    if (begin == arrayLength) break;
                    if (length > 0) {
                        i = begin + length;         // 输出最长词后，从该词语的下一个位置恢复扫描
                        return true;
                    }

                    i = begin;                      // 转移失败，也将起点往前挪一个，重新开始，状态归零
                    ++begin;
                    b = base[0];
                }
                p = b;
                n = base[p];
                if (b == check[p] && n < 0)         // base[p] == check[p] && base[p] < 0 查到一个词
                {
                    length = i - begin + 1;
                    index = -n - 1;
                    value = v[index];
                }
            }

            return false;
        }
    }

    /**
     * 全切分
     *
     * @param text      文本
     * @param processor 处理器
     */
    public void parseText(String text, AhoCorasickDoubleArrayTrie.IHit<V> processor) {
        Searcher searcher = getSearcher(text, 0);
        while (searcher.next()) {
            processor.hit(searcher.begin, searcher.begin + searcher.length, searcher.value);
        }
    }

    public LongestSearcher getLongestSearcher(String text, int offset) {
        return getLongestSearcher(text.toCharArray(), offset);
    }

    public LongestSearcher getLongestSearcher(char[] text, int offset) {
        return new LongestSearcher(offset, text);
    }

    /**
     * 最长匹配
     *
     * @param text      文本
     * @param processor 处理器
     */
    public void parseLongestText(String text, AhoCorasickDoubleArrayTrie.IHit<V> processor) {
        LongestSearcher searcher = getLongestSearcher(text, 0);
        while (searcher.next()) {
            processor.hit(searcher.begin, searcher.begin + searcher.length, searcher.value);
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
        int b = base[current];
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
     * 释放空闲的内存
     */
    private void shrink() {
        int nbase[] = new int[size + 65535];
        System.arraycopy(base, 0, nbase, 0, size);
        base = nbase;

        int ncheck[] = new int[size + 65535];
        System.arraycopy(check, 0, ncheck, 0, size);
        check = ncheck;
    }
}

