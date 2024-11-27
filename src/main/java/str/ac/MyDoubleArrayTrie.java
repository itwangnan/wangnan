package str.ac;

import arr.Arr;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * 双数组字典树
 * check[base[s] + c] = s
 * base[s] + c = t
 * <p>
 * 相当于base[s]需要找到一个空位并满足加c集合都是空位，则赋值，可以看到base[s]需要考虑到base数组中，
 * 而之前只要在base中索引自增就行，只有next才找有效下标，现在都要找空位，也说明了节点状态不在连续。
 */
public class MyDoubleArrayTrie {

    private int[] base;

    private int[] check;
    //目前数组大小
    private int allocSize;

    private int nextCheckPos;

    //65536 hash是UTF16
    private static final int HASH_SIZE = 1 << 16;

    //32 * 65536 内存对齐
    private static final int RSIZE = HASH_SIZE * 32;


    private int size;

    /**
     * 该方式将递归 变成 while-stack实现
     * @param treeSet
     */
    public MyDoubleArrayTrie(TreeSet<String> treeSet) {
        resize(RSIZE);

        List<String> key = new ArrayList<>(treeSet);

        this.base[0] = 1;

        Node root_node = new Node();
        root_node.left = 0;
        root_node.depth = 0;
        root_node.right = treeSet.size();
        List<Node> list = fetch(root_node, key,null);

        Stack<List<Node>> stack = new Stack<>();
        Stack<Node> nodeStack = new Stack<>();

        stack.push(list);
        nodeStack.push(root_node);
//        boolean whileFirst = true;
        BitSet used = new BitSet();

        while (!stack.isEmpty()){
            List<Node> siblings = stack.pop();
            Node preNode = nodeStack.pop();
            //base[s]
            int begin = 0;

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
                    //第一次记录pos 位置,其实就是记录下最开始有位置的地方
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


            if (preNode.depth != 0){
                base[preNode.begin + preNode.code] = begin;
            }

            for (int i = 0; i < siblings.size(); i++) {
                Node node = siblings.get(i);
                //check[base[s] + c] = base[s]
                check[begin + node.code] = begin;

                List<Node> newSiblings = fetch(node, key, begin);

                if (CollectionUtils.isEmpty(newSiblings)) {
                    base[begin + node.code] = - node.left - 1;
                } else {
                    node.begin = begin;
                    nodeStack.push(node);
                    stack.push(newSiblings);
                }
            }


        }


        shrink();
    }

    private static class Node {
        int code;
        int depth;
        int left;
        int right;
        Integer begin;
    }

    /**
     * 获取直接相连的子节点
     *
     * @return 兄弟节点个数
     */
    private List<Node> fetch(Node parent, List<String> key,Integer begin) {

        List<Node> siblings = new ArrayList<>();

        int prev = 0;

        for (int i = parent.left; i < parent.right; i++) {

            if (key.get(i).length() < parent.depth) {
                continue;
            }

            String tmp = key.get(i);

            int cur = 0;
            if (tmp.length() != parent.depth) {
                cur = (int) tmp.charAt(parent.depth) + 1;
            }

            if (cur != prev || siblings.size() == 0) {

                Node tmp_node = new Node();
                tmp_node.depth = parent.depth + 1;
                tmp_node.code = cur;
                tmp_node.left = i;
                tmp_node.begin = begin;

                if (siblings.size() != 0)
                    siblings.get(siblings.size() - 1).right = i;

                siblings.add(tmp_node);
            }

            prev = cur;
        }

        if (siblings.size() != 0) {
            siblings.get(siblings.size() - 1).right = parent.right;
        }


        return siblings;
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
        if (allocSize > 0) {
            System.arraycopy(base, 0, base2, 0, allocSize);
            System.arraycopy(check, 0, check2, 0, allocSize);
        }

        base = base2;
        check = check2;

        return allocSize = newSize;
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


    public boolean search(String word) {
        if (word == null || word.length() == 0){
            return false;
        }

        int bs = base[0];

        for (int i = 0; i < word.length(); i++) {
            //base[s] + c
            int index = bs + (int) word.charAt(i) + 1;

            if (check[index] != bs) {
                // 不存在该字符串
                return false;
            }

            //next[base[s] + c] = t
            bs = base[index];
        }

        //base[next[base[s] + 0]] = t
        int n = base[bs]; //查询base

        //check[base[s] + 0] = base[s]
        if (bs == check[bs] && n < 0){ //状态转移成功且对应词语结尾
//            int index = -n - 1; //获得字典序
            return true;
        }
        // 检查是否到达字符串末尾
        return false;
    }

}
