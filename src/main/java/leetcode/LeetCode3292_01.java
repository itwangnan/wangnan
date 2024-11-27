package leetcode;

import org.apache.commons.math3.analysis.function.Min;
import org.junit.Test;

import java.util.*;

/**
 * https://leetcode.cn/problems/minimum-number-of-valid-strings-to-form-target-ii/description/
 * 给你一个字符串数组 words 和一个字符串 target。
 *
 * 如果字符串 x 是 words 中 任意 字符串的
 * 前缀
 * ，则认为 x 是一个 有效 字符串。
 *
 * 现计划通过 连接 有效字符串形成 target ，请你计算并返回需要连接的 最少 字符串数量。如果无法通过这种方式形成 target，则返回 -1。
 */
public class LeetCode3292_01 {

    @Test
    public void test(){

        /**
         * 为什么使用Ac自动机，
         * 是因为可以思考这类问题想跳步55这种问题，每步后获取该步能走最远的距离，这样如果可以到达最后就一定是最少步
         * 那么就要想如何获得最长步，首先字典树可以判断前缀是否存在，如果dp的话看treeSplit方法，时间n^2
         *
         *
         *
         * 1.正常转移
         *  f(i) = min(f(j) + 1) ,
         *  j ∈ [0 ... i-1] , f[j+1 ... i]得是words中的前缀， i>0
         *  接着我们注意到
         *  f[i]代表着当前target[i]所需要最小组成数,f[i]一定大于等于f[i-1],因为增加了一个尾字符，则组成数一定大于等于f(i-1),
         *  所以 i_1 <= i_2 , 一定有f(i_1) <= f(i_2) 说明递增序列
         *
         *  这时min(f(j) + 1) = f(j) + 1 且，j是target[j+1 ... i]最长的前缀。
         *  证明: target[j_1+1 ... i] > target[j_2+1 ... i] , j_1 < j_2
         *  f(j_1) + 1 < f(j_2) + 1
         *  则 f(j_1) + 1更小，f(i) = f(j_1) + 1
         *  因此得到必须target[j+1 ... i]是匹配最长的前缀，这时如何得到最长匹配前缀呢？
         *  Ac自动机可以满足，每次匹配到当前字符时就是最长匹配（并且因为前缀的原因每个节点都是结束节点），
         *  这时就可以获得匹配长度，用 f(i + 1) = f(i - len + 1) + 1
         *
         *
         * 2.不能，则结束-1
         *
         */
        System.out.println(minValidStrings(new String[]{"abc","aaaaa","bcdef"},"aabcdabc"));
        System.out.println(minValidStrings(new String[]{"abababab","ab"},"ababaababa"));
        System.out.println(minValidStrings(new String[]{"abcdef"},"xyz"));
        System.out.println(minValidStrings(new String[]{"a","babc"},"aacab"));

        PriorityQueue<Node> res = new PriorityQueue<>((a,b) -> a.level - b.level);
        res.offer(new Node(2));
        res.offer(new Node(3));
        res.offer(new Node(1));
        System.out.println(res.poll().level);
    }

    static class Node{

        Node[] tranArr = new Node[26];

        int level;

        Node fail;

        public Node(int level) {
            this.level = level;
        }
    }

    public int minValidStrings(String[] words, String target) {
        //1.首先构造字典数
        Node head = this.build(words);

        //2.构建fail指针
        this.buildFail(head);

        //3.查询
        return this.search(head,target);
    }

    private int search(Node head, String target) {

        Node next = head;
        int[] f = new int[target.length() + 1];

        char[] chars = target.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            next = next.tranArr[chars[i] - 'a'];
            if (next == head){
                //失败
                return -1;
            }


            f[i + 1] = f[i - next.level + 1] + 1;

        }
        return f[target.length()];

    }

    private void buildFail(Node head) {
        //层次遍历 bfs

        //第一次遍历因为 head.fail.tranArr[i] 无法为head 所以单独逻辑
        Queue<Node> queue = new ArrayDeque<>();
        for (int i = 0; i < head.tranArr.length; i++) {
            if (head.tranArr[i] == null){
                head.tranArr[i] = head;
            }else {
                head.tranArr[i].fail = head;
                queue.add(head.tranArr[i]);
            }
        }
        while (!queue.isEmpty()){

            Node node = queue.poll();

            for (int i = 0; i < node.tranArr.length; i++) {

                if (node.tranArr[i] == null){
                    //不存在 直接跳父节点fail下一个字符
                    node.tranArr[i] = node.fail.tranArr[i];
                }else {
                    //存在   节点fail指向父节点fail下一个字符
                    node.tranArr[i].fail = node.fail.tranArr[i];
                    queue.add(node.tranArr[i]);
                }

            }
        }
    }

    private Node build(String[] words) {
        Node head = new Node(0);
        for (String word : words) {

            Node next = head;

            for (char c : word.toCharArray()) {
                Node node = next.tranArr[c - 'a'];
                if (node == null){
                    next.tranArr[c - 'a'] = new Node(next.level + 1);
                }
                next = next.tranArr[c - 'a'];
            }
        }
        return head;
    }



}
