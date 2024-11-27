package leetcode.refine.binary_tree;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.math3.analysis.function.Min;
import org.junit.Test;

import java.util.*;

public class BinaryTreeTest {


    @Test
    public void leetCode100() {

        //https://leetcode.cn/problems/same-tree/description/
        BinaryTree<Integer> tree2 = new BinaryTree<>(2, null, null);
        BinaryTree<Integer> tree3 = new BinaryTree<>(3, null, null);
        BinaryTree<Integer> treeA = new BinaryTree<>(1, tree2, tree3);


        BinaryTree<Integer> tree4 = new BinaryTree<>(3, null, null);
        BinaryTree<Integer> treeB = new BinaryTree<>(1, tree4, null);

        //比较

        boolean flag = this.compare(treeA, treeB);


        System.out.println(flag);


    }

    private boolean compare(BinaryTree<Integer> treeA, BinaryTree<Integer> treeB) {
        if (treeA == null || treeB == null) {
            return treeA == null && treeB == null;
        }

        Integer valueA = treeA.value;
        Integer valueB = treeB.value;
        boolean equals = Objects.equals(valueA, valueB);
        if (!equals) {
            return equals;
        }

        boolean l = compare(treeA.left, treeB.left);
        boolean r = compare(treeA.right, treeB.right);
        return l && r;
    }

    @Test
    public void leetCode101() {

        //https://leetcode.cn/problems/symmetric-tree/description/
        BinaryTree<Integer> tree4 = new BinaryTree<>(3, null, null);

        BinaryTree<Integer> tree2 = new BinaryTree<>(2, null, tree4);


        BinaryTree<Integer> tree3 = new BinaryTree<>(2, tree4, null);

        BinaryTree<Integer> treeA = new BinaryTree<>(1, tree2, tree3);


        //比较

        boolean flag = this.symmetry(treeA.left, treeA.right);


        System.out.println(flag);


    }

    private boolean symmetry(BinaryTree<Integer> treeA, BinaryTree<Integer> treeB) {

        if (treeA == null || treeB == null) {
            return treeA == null && treeB == null;
        }

        boolean l = compare(treeA.left, treeB.right);
        boolean r = compare(treeA.right, treeB.left);
        return l && r;
    }


    @Test
    public void leetCode110() {

        //https://leetcode.cn/problems/balanced-binary-tree/description/

        BinaryTree<Integer> tree4 = new BinaryTree<>(3, null, null);

        BinaryTree<Integer> tree2 = new BinaryTree<>(2, null, tree4);


        BinaryTree<Integer> tree3 = new BinaryTree<>(2, tree4, null);

        BinaryTree<Integer> treeA = new BinaryTree<>(1, tree2, tree3);


        //比较

        boolean isBalance = this.isBalance(treeA) != -1;


        System.out.println(isBalance);


    }

    private int isBalance(BinaryTree<Integer> node) {
        if (node == null) {
            return 0;
        }
        int leftNum = 0;
        if (node.left != null) {
            leftNum = isBalance(node.left);
        }

        if (leftNum == -1) {
            return -1;
        }

        int rightNum = 0;
        if (node.right != null) {
            rightNum = isBalance(node.right);
        }

        //特殊情况 特殊返回
        if (rightNum == -1 || Math.abs(leftNum - rightNum) > 0) {
            return -1;
        }

        return Math.max(leftNum, rightNum) + 1;

    }


    @Test
    public void leetCode199() {

        //https://leetcode.cn/problems/binary-tree-right-side-view/solutions/2015061/ru-he-ling-huo-yun-yong-di-gui-lai-kan-s-r1nc/

        BinaryTree<Integer> tree7 = new BinaryTree<>(7, null, null);


        BinaryTree<Integer> tree5 = new BinaryTree<>(5, tree7, null);
        BinaryTree<Integer> tree6 = new BinaryTree<>(6, null, null);
        BinaryTree<Integer> tree4 = new BinaryTree<>(4, null, null);

        BinaryTree<Integer> tree2 = new BinaryTree<>(2, tree6, tree5);


        BinaryTree<Integer> tree3 = new BinaryTree<>(3, null, tree4);

        BinaryTree<Integer> tree = new BinaryTree<>(1, tree2, tree3);


        //比较

        List<Integer> out1 = this.rightView1(tree);
        List<Integer> out2 = new ArrayList<>();
        this.rightView2(tree, out2, 0);


        System.out.println(out2);


    }

    private void rightView2(BinaryTree<Integer> tree, List<Integer> out2, int depth) {
        /**
         * 如果在递归前处理则由顶入底，参数得是入参
         *
         * 如果在递归后处理则由底入顶，参数得是返回值
         */
        if (tree == null) {
            return;
        }
        if (out2.size() <= depth) {
            out2.add(tree.value);
        }
        rightView2(tree.right, out2, depth + 1);
        rightView2(tree.left, out2, depth + 1);
        return;
    }

    private List<Integer> rightView1(BinaryTree<Integer> tree) {

        if (tree == null) {
            return new ArrayList<>();
        }
        List<Integer> rightList = rightView1(tree.right);
        List<Integer> leftList = rightView1(tree.left);

        int max = Math.max(rightList.size(), leftList.size());
        List<Integer> res = new ArrayList<>();
        res.add(tree.value);
        for (int i = 0; i < max; i++) {

            if (i < rightList.size()) {
                res.add(rightList.get(i));
            } else {
                res.add(leftList.get(i));
            }

        }

        return res;
    }


    @Test
    public void leetCode98() {

        //https://leetcode.cn/problems/validate-binary-search-tree/description/

        BinaryTree<Integer> tree2 = new BinaryTree<>(2, new BinaryTree<>(1, null, null), new BinaryTree<>(4, new BinaryTree<>(3, null, null), null));


        BinaryTree<Integer> tree3 = new BinaryTree<>(6, null, null);

        BinaryTree<Integer> tree = new BinaryTree<>(5, tree2, tree3);


        //比较

//        boolean flag = this.isSearchQian(tree,Integer.MIN_VALUE,Integer.MAX_VALUE);
//        System.out.println(flag);

//        boolean flag = this.isSearchZhong(tree,Integer.MIN_VALUE);
//        System.out.println(flag);

        int[] arr = this.isSearchHou(tree);
        System.out.println(arr[1] != Integer.MAX_VALUE);


    }

    private boolean isSearchQian(BinaryTree<Integer> tree, int left, int right) {

        if (tree == null) {
            return true;
        }


        return tree.value > left && tree.value < right
                && isSearchQian(tree.left, left, tree.value)
                && isSearchQian(tree.right, tree.value, right);
    }

    private boolean isSearchZhong(BinaryTree<Integer> tree, int pre) {

        if (tree == null) {
            return true;
        }

        boolean l = isSearchZhong(tree.left, pre);
        if (!l) {
            return false;
        }

        if (tree.left != null && tree.value <= tree.left.value) {
            return false;
        }

        boolean r = isSearchZhong(tree.right, tree.value);
        if (!r) {
            return false;
        }

        return tree.value > pre;
    }


    /**
     * 思想返回最小值和最大值，然后当前节点比较是否大于左边最大值，小于右边最小值
     *
     * @param tree
     * @return
     */
    private int[] isSearchHou(BinaryTree<Integer> tree) {

        if (tree == null) {
            return new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE};
        }

        int[] r = isSearchHou(tree.right);
        if (r[0] <= tree.value) {
            return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE};
        }
        int[] l = isSearchHou(tree.left);
        if (l[1] >= tree.value) {
            return new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE};
        }

        return new int[]{Math.min(l[0], tree.value), Math.max(r[1], tree.value)};
    }


    @Test
    public void leetCode236() {

        //https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/description/

        BinaryTree<Integer> tree2 = new BinaryTree<>(2, new BinaryTree<>(1, null, null), new BinaryTree<>(4, new BinaryTree<>(3, null, null), null));


        BinaryTree<Integer> tree3 = new BinaryTree<>(6, null, null);

        BinaryTree<Integer> tree = new BinaryTree<>(5, tree2, tree3);


        System.out.println(this.commonAncestor(tree, 1, 3));

//        System.out.println(flag);


    }

    private int commonAncestor(BinaryTree<Integer> tree, int p1, int p2) {

        if (tree == null) {
            return 0;
        }

        int flag1 = commonAncestor(tree.right, p1, p2);
        int flag2 = commonAncestor(tree.left, p1, p2);

        if (tree.value == p1 || tree.value == p2) {
            return tree.value;
        }

        if (flag1 + flag2 == p1 + p2) {
            return tree.value;
        }

        return flag1 + flag2;
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //这里因为不知道具体是在左子树还是右子树，需要判断null
        if (root == null || root == p || root == q) {
            return root;
        }

        TreeNode flag1 = lowestCommonAncestor(root.right, p, q);
        TreeNode flag2 = lowestCommonAncestor(root.left, p, q);

        if (flag1 != null && flag2 != null) {
            return root;
        }

        return flag1 != null ? flag1 : flag2;
    }


    @Test
    public void leetCode235() {

        //https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-search-tree/description/
        searchCommonAncestor(null, null, null);


    }


    public TreeNode searchCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //这里因为知道具体是在左子树还是右子树，不需要判断null，因为p和q一定存在
        int val = root.val;
        if (val < p.val && val < q.val) {
            return searchCommonAncestor(root.right, p, q);
        } else if (val > p.val && val > q.val) {
            return searchCommonAncestor(root.left, p, q);
        } else {
            //只要是在 区间内的就一定是结果
            return root;
        }
    }

//    public TreeNode searchCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
//        while ((root.val - (long)p.val) * (root.val - (long)q.val) > 0)
//        {root = p.val < root.val ? root.left : root.right;}
//
//        boolean flag = true;
//        while (flag){
//            if (root.val < p.val && root.val < q.val){
//                root = root.right;
//            }else if (root.val > p.val && root.val > q.val){
//                root = root.left;
//            }else {
//                flag = false;
//            }
//        }
//        return root;
//    }


    @Test
    public void leetCode102() {


//        https://leetcode.cn/problems/binary-tree-level-order-traversal/description/

        TreeNode node = new TreeNode(3);

        node.left = new TreeNode(9);
        node.right = new TreeNode(20);
        node.right.left = new TreeNode(15);
        node.right.right = new TreeNode(7);

        List<List<Integer>> list = levelOrder(node);

        for (List<Integer> num : list) {
            System.out.println(num);
        }


    }

    /**
     * 双数组
     * @param root
     * @return
     */
//    public List<List<Integer>> levelOrder(TreeNode root) {
//
//        List<List<Integer>> res = new ArrayList<>();
//        if (root == null){
//            return res;
//        }
//
//        List<TreeNode> cur = new ArrayList<>();
//        cur.add(root);
//
//
//
//
//        while (!cur.isEmpty()){
//
//            List<Integer> list = new ArrayList<>();
//            List<TreeNode> nxt = new ArrayList<>();
//
//            for (TreeNode node : cur) {
//                list.add(node.val);
//
//                if (node.left != null){
//                    nxt.add(node.left);
//                }
//                if (node.right != null){
//                    nxt.add(node.right);
//                }
//            }
//
//            res.add(list);
//            cur = nxt;
//        }
//
//        return res;
//    }

    /**
     * 队列
     */
    public List<List<Integer>> levelOrder(TreeNode root) {

        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        Queue<TreeNode> que = new ArrayDeque<>();
        que.add(root);
        while (!que.isEmpty()) {

            List<Integer> list = new ArrayList<>();
            int n = que.size();
            while (n-- > 0) {
                TreeNode node = que.poll();
                list.add(node.val);

                if (node.left != null) {
                    que.add(node.left);
                }
                if (node.right != null) {
                    que.add(node.right);
                }
            }

            res.add(list);
        }

        return res;
    }


    @Test
    public void leetCode103() {


//        https://https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/description/

//        TreeNode node = new TreeNode(1);
//
//        node.left = new TreeNode(2);
//        node.right = new TreeNode(3);
//        node.left.left = new TreeNode(4);
//        node.right.right = new TreeNode(5);

        TreeNode node = new TreeNode(3);
//
        node.left = new TreeNode(9);
        node.right = new TreeNode(20);
        node.right.left = new TreeNode(15);
        node.right.right = new TreeNode(7);

        List<List<Integer>> list = zigzagLevelOrder(node);

        for (List<Integer> num : list) {
            System.out.println(num);
        }


    }

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {

        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        Queue<TreeNode> que = new ArrayDeque<>();
        que.add(root);
        boolean flag = false;
        while (!que.isEmpty()) {

            List<Integer> list = new ArrayList<>();
            int n = que.size();
            while (n-- > 0) {
                TreeNode node = que.poll();
                list.add(node.val);

                if (node.left != null) {
                    que.add(node.left);
                }
                if (node.right != null) {
                    que.add(node.right);
                }
            }

            if (flag) {
                Collections.reverse(list);
            }
            res.add(list);
            flag = !flag;
        }

        return res;

    }


    @Test
    public void leetCode513() {


//        https://leetcode.cn/problems/find-bottom-left-tree-value/description/

        TreeNode node = new TreeNode(3);
//
        node.left = new TreeNode(9);
        node.right = new TreeNode(20);
        node.right.left = new TreeNode(15);
        node.right.right = new TreeNode(7);

        List<List<Integer>> list = zigzagLevelOrder(node);

        for (List<Integer> num : list) {
            System.out.println(num);
        }


    }


    private static final String[] MAPPING = new String[]{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    @Test
    public void leetCode57() {


//        https://leetcode.cn/problems/letter-combinations-of-a-phone-number/

//        List<String> strList = new ArrayList<>();
//        List<String> list = letterCombinations("23", 0, strList);
        List<String> list = letterCombinations("23");

        System.out.println(list);

    }

    public List<String> letterCombinations(String digits, int level, List<String> list) {

        if (level >= digits.length()) {
            return list;
        }
        int charAt = digits.charAt(level);
        int index = charAt - '0';
        String s = MAPPING[index];

        List<String> newList = new ArrayList<>();

        for (char c : s.toCharArray()) {
            if (list.size() > 0) {
                for (String s1 : list) {
                    newList.add(s1 + c);
                }
            } else {
                newList.add(String.valueOf(c));
            }
        }


        return letterCombinations(digits, level + 1, newList);

    }


    private final List<String> ans = new ArrayList<>();
    private char[] digits, path;

    public List<String> letterCombinations(String digits) {
        int n = digits.length();
        if (n == 0) {
            return new ArrayList<>();
        }
        this.digits = digits.toCharArray();
        path = new char[n]; // 本题 path 长度固定为 n
        dfs(0);
        return ans;
    }

    private void dfs(int i) {
        if (i == digits.length) {
            ans.add(new String(path));
            return;
        }
        for (char c : MAPPING[digits[i] - '0'].toCharArray()) {
            path[i] = c; // 直接覆盖
            dfs(i + 1);
        }
    }





}
