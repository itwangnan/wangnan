package str.ac;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.TreeSet;

/**
 * 三数组字典树
 *
 * check[base[s] + c] = s  -> base[s]
 * next[base[s] + c] = t   -> base[t]
 * base[t] = begin
 *
 * 怎么算
 */
public class TripleArrayTrie {

    //需要三个数组

    //偏移量基准数组
    private int[] base;

    //状态转移数组   用法 next[base[s] + c] = t，代表s状态+c字符=t状态
    private int[] next;

    //父节点检测数组 用法 check[t] = s，代表t状态父是s状态
    private int[] check;

    //目前数组大小
    private int allocSize;

    //最大有值的长度
    private int size;

    //65536 hash是UTF16
    private static final int HASH_SIZE = 1 << 16;

    //32 * 65536 内存对齐
    private static final int RSIZE =  HASH_SIZE * 32;


//    private static final char END = '\0';

    /**
     * 是否开启快速构建
     */
    private boolean enableFastBuild;


    private int nextCheckPos;


    private int baseIndex;

    /**
     * 树节点，用来dfs构造三数组数据使用
     */
    private static class Node{
        //字符
        int code;
        //深度
        int depth;
        //左节点范围
        int left;
        //右节点范围
        int right;
    }


    private int fetch(Node parent, List<Node> siblings, List<String> key) {

        int prev = 0;

        for (int i = parent.left; i < parent.right; i++) {
            String str = key.get(i);

            //短的串直接结束了
            if (str.length() < parent.depth)
                continue;


            int cur = 0;

            //刚好等于就代表
            if (str.length() != parent.depth){
                //对应第depth个字符 + 1
                cur = str.charAt(parent.depth) + 1;
            }

            //前不能 prev 前不能大于cur，为了避免冲突才这样设计
            if (prev > cur) {
                return 0;
            }

            /**
             * 一举
             * 一举一动
             * 一举成名
             * 一举成名天下知
             * 万能
             * 万能胶
             *
             * cur == prev 是一，直到万才产生下一个新节点
             * 并left = i，而right是下一个节点的开始
             */
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

        //
        if (siblings.size() != 0){
            //上面最后一个节点右节点 是 父节点结束
            siblings.get(siblings.size() - 1).right = parent.right;
        }


        //子节点返回数量
        return siblings.size();
    }




    public TripleArrayTrie(TreeSet<String> treeSet) {
        this.build(new ArrayList<>(treeSet));
    }

    /**
     * 构造
     * @param treeSet
     */
    private void build(List<String> treeSet){

        resize(RSIZE);

        base[0] = 0;
        check[0] = 0;
        next[0] = 0;

        nextCheckPos = 0;

        //1.创建根节点
        Node root = new Node();
        root.left = 0;
        root.right = treeSet.size();

        List<Node> siblings = new ArrayList<>();
        fetch(root,siblings,treeSet);
        insert(siblings,new BitSet(),treeSet);
        shrink();




    }

    /**
     * 插入节点
     *
     * @param siblings 等待插入的兄弟节点
     * @return 插入位置
     */
    private int insert(List<Node> siblings, BitSet used,List<String> key) {

        //base[root]
        int begin = 0;
        //找check[begin + a1...an]的当前位置
        // siblings.get(0).code + 1
        //enableFastBuild 开启了快速构建则nextCheckPos + 1 否则nextCheckPos
        //最后在-1
        int pos = Math.max(siblings.get(0).code + 1, enableFastBuild ? (nextCheckPos + 1) : nextCheckPos) - 1;
        int nonzero_num = 0;
        int first = 0;

        if (allocSize <= pos){
            resize(pos + 1);
        }

        outer:
        // 此循环体的目标是找出满足check[begin + a1...an]  == 0的n个空闲空间,a1...an是siblings中的n个节点
        while (true) {
            pos++;

            if (allocSize <= pos){
                resize(pos + 1);
            }

            //check[pos] 是第一个下标的选取，如果直接有值，直接重找
            if (check[pos] != 0) {
                nonzero_num++;
                continue;
            } else if (first == 0) {
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
        size = (size > begin + siblings.get(siblings.size() - 1).code + 1) ? size : begin + siblings.get(siblings.size() - 1).code + 1;

        //这里的begin满足check[begin + a1...an]==0。直接进行修改
        //check[base[s] + c] = base[s]
        //next[base[s] + c] = t
        //base[t] = base[s]



//        for (int i = 0; i < siblings.size(); i++) {
//            //check[base[s] + c] = s
//            check[begin + siblings.get(i).code] = begin;
//            //next[base[s] + c] = t,base[t] = ?
//            //next[base[0] + 49] = t , next[base[t] + 48] = k
//            next[begin + siblings.get(i).code] = begin + siblings.get(i).code;
//
//            base[next[begin + siblings.get(i).code]] = begin;
//        }
        for (int i = 0; i < siblings.size(); i++) {
            //check[base[s] + c] = base[s]
            check[begin + siblings.get(i).code] = begin;

        }


        //处理子节点的子节点
        for (int i = 0; i < siblings.size(); i++) {
            List<Node> new_siblings = new ArrayList<>();
            Node sonNode = siblings.get(i);
            // 一个词的终止且不为其他词的前缀
            if (fetch(sonNode, new_siblings,key) == 0) {
                //base[base[s] + c] = t 下个节点的基准点位置 结束存的是-siblings.get(i).left - 1
                //结束
                next[begin + siblings.get(i).code] = -siblings.get(i).left - 1;
                //
                base[begin + siblings.get(i).code] = -siblings.get(i).left - 1;

            } else {
                //节点的子节点，h为子节点第一个点的开始
                int h = insert(new_siblings, used,key);   // dfs
                //base[base[s] + c] = base[t] 下个节点的基准点位置
                next[begin + siblings.get(i).code] = h;
                base[begin] = h;

            }
        }

        //第一个点的开始
        return begin;
    }




    public String getItem(String key){

        //s
        int s = 0;

        //因为'\0'的原因，所以在过程中不能结束
        for (char c : key.toCharArray()) {
            //p = base[s] + c
            int p = trans(c,s);
            // base[s] == check[base[s] + c]
            if (s == check[p]){
                //s = base[s] + c
                s = next[p];
            }else {
                return null;
            }

        }

        // next[base[s] + 0]
        int p = next[s]; //按字符'\0'进行状态转移

        //base[next[base[s] + 0]] = t
        int n = next[p]; //查询base

        //check[base[s] + 0] = base[s]
        if (s == check[p] && n < 0){ //状态转移成功且对应词语结尾
//            int index = -n - 1; //获得字典序
            return key;
        }

        return null;
    }


    /**
     * 状态转移函数
     * @param s s状态
     * @param c 加上c字符
     * @return s+c的状态
     */
    private Integer trans(char c,int s){
        int p = base[s] + (int)c + 1;
//        System.out.println(String.format("exactMatchSearch b:%s p:%s",base[s],p));
        //check[base[s] + c] = s
        if (s == check[p]){
            //证明有效
            return p;
        }else {
            //无效节点
            return -1;
        }
    }

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
     * 数组扩容方法
     * @param size 新数组大小
     */
    private void resize(int size){
        int[] newBase = new int[size];
        int[] newNext = new int[size];
        int[] newCheck = new int[size];

        if (allocSize > 0) {
            //如果已经初始化过了，就要迁移
            System.arraycopy(base, 0, newBase, 0, allocSize);
            System.arraycopy(check, 0, newCheck, 0, allocSize);
            System.arraycopy(next, 0, newNext, 0, allocSize);
        }

        base = newBase;
        check = newCheck;
        next = newNext;

        allocSize = size;
    }


//    /**
//     * 释放空闲的内存
//     */
    private void shrink() {
        int[] nbase = new int[size + 65535];
        System.arraycopy(base, 0, nbase, 0, size);
        base = nbase;

        int[] ncheck = new int[size + 65535];
        System.arraycopy(check, 0, ncheck, 0, size);
        check = ncheck;


        int[] nNext = new int[size + 65535];
        System.arraycopy(next, 0, nNext, 0, size);
        next = nNext;
    }



    //    /**
//     * 深度遍历
//     * @param p        父节点
//     * @param sonList  父节点p的子节点 [s1...sn]
//     */
//    public void dfs(Node p){
//
//        /**
//         * 1。寻找一个起始下标b，使得所有check[base[b] + si.code] == 0。
//         * 也就是说，找到n个空闲下标插入这群子节点。
//         */
//        int b = this.getB(p.left);
//
//        /**
//         * 2。执行check[base[b] + si.code] = base[b],也即将这n个空闲空间分配给这群子节点。
//         * 这样n个子节点base[b] + si.code 就链接到了父节点b，建立了子与父多对一的关系。
//         */
//        for (Node node : p.sonList) {
//            //base[b] + node.code 的父节点是base[b]
//            check[base[b] + node.code] = base[b];
//
//            /**
//             * 3。检查每个子节点si，若它没有孩子。（叶子节点）
//             * 将base设置为负值，以存储它所对应单词的字典序，即base[base[b] + si.code] = -si.index-1;
//             *
//             */
//            if (CollectionUtils.isEmpty(node.sonList)){
//                next[base[b] + node.code] =  -node.sort -1;
//            }else {
//                //否则，跳转到步骤1，递归插入。
//                dfs(node);
//            }
//
//        }
//
//    }


    //    private void dfs(TrieNode parentNode) {
//
//        /**
//         * 1。寻找一个起始下标b，使得所有check[base[b] + si.code] == 0。
//         * 也就是说，找到n个空闲下标插入这群子节点。
//         */
//        Collection<TrieNode> values = parentNode.getMap().values();
//        int b = this.getB2(values);
//
//        /**
//         * 2。执行check[base[b] + si.code] = base[b],也即将这n个空闲空间分配给这群子节点。
//         * 这样n个子节点base[b] + si.code 就链接到了父节点b，建立了子与父多对一的关系。
//         */
//        for (TrieNode node : values) {
//            //base[b] + node.code 的父节点是base[b]
//            check[base[b] + node.value] = base[b];
//
//            /**
//             * 3。检查每个子节点si，若它没有孩子。（叶子节点）
//             * 将base设置为负值，以存储它所对应单词的字典序，即base[base[b] + si.code] = -si.index-1;
//             *
//             */
//            if (CollectionUtils.isEmpty(values)){
//                next[base[b] + node.value] =  - node.sort - 1;
//            }else {
//                //否则，跳转到步骤1，递归插入。
//                dfs(node);
//            }
//
//        }
//
//    }


//    private int getB2(Collection<TrieNode> sonList) {
//        int b = 1;
//
//        while (true){
//            for (TrieNode node : sonList) {
//                if (check[base[b] + node.value] != 0){
//                    b++;
//                    break;
//                }
//            }
//            break;
//        }
//
//        return b;
//    }
//
//
//    private int getB(Collection<Node> sonList) {
//        int b = 1;
//
//        while (true){
//            for (Node node : sonList) {
//                if (check[base[b] + node.code] != 0){
//                    b++;
//                    break;
//                }
//            }
//            break;
//        }
//
//        return b;
//    }


    /**
     *
     * @param str
     * @return
     */
    public boolean exactMatchSearch(String str) {
        int pre = 0;
        for (char c : str.toCharArray()) {
            Integer p = trans(c,pre);
            if (p != -1){
                //没有匹配上
                pre = p;
            }else {
                return false;
            }
        }

        int p = base[pre];
        int n = base[p];
        if (p == check[p] && n < 0){
            return true;
        }

        return false;
    }
}
