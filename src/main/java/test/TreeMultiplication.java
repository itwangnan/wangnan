package test;

import leetcode.refine.binary_tree.TreeNode;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.*;

public class TreeMultiplication {


    @Test
    public void test() {
        int[][] vals = {{0,1},{1,2},{1,3},{1,6},{3,4},{3,5},{0,7},{7,8},{7,9}};
        k(10,vals,0);

        int[][] vals2 = {{1,2},{1,3},{2,4},{2,5},{3,6},{5,7},{7,8},{7,9},{8,10},{9,11},{11,12},{12,13},{13,14}};
        k(15,vals2,1);
//        System.out.println(ln(9));
//        System.out.println(ln(8));
//        System.out.println(ln(7));
    }

    public void k(int n,int[][] vals,int init) {
//        int[] arr = new int[n];

        Map<Integer, Node> map = new HashMap<>();
        int maxDepth = 0;
        for (int[] val : vals) {
            //父节点
            Node node = map.getOrDefault(val[0], new Node(val[0], 1));
            Node childrenNode = map.getOrDefault(val[1], new Node(val[1], node.depth + 1));
            node.children.add(childrenNode);
            childrenNode.parent = node.val;
            map.putIfAbsent(val[0], node);
            map.putIfAbsent(val[1], childrenNode);
//            arr[val[1]] = val[0];
            maxDepth = Math.max(maxDepth, childrenNode.depth);
        }

        //DFS 深度遍历
        Stack<Node> stack = new Stack<>();
        stack.add(map.get(init));
        //顺便存储倍增
        int len = ln(maxDepth);
        int[][] flag = new int[n][len];
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if (CollectionUtils.isEmpty(node.children)) {
                //存储
                continue;
            }
            for (Node child : node.children) {
//                flag[child.val][0] = node.val;
                int num = ln(child.depth);
                Integer parent = child.val;
                int pre = 0;
                for (int i = 0; i < num ; i++) {
                    int pow = (int) Math.pow(2, i);
                    int cnt = pow - pre;
                    pre = pow;
                    for (int j = 0; j < cnt; j++) {
                        parent = map.get(parent).parent;
                    }
                    flag[child.val][i] = parent;
                }
                stack.push(child);
            }
        }
        System.out.println();
    }


    class Node {
        int val;
        int depth;
        Integer parent;
        List<Node> children = new ArrayList<>();

        public Node(int val, int depth) {
            this.val = val;
            this.depth = depth;
        }
    }

    //
    public static int ln(double num) {
        return (int) (Math.ceil(Math.log(num) / Math.log(2)));
    }
}
