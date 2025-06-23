package tree;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * AVL 平衡二叉树
 * 所有节点的左子树和右子树 高度差 <= 1
 */
public class AvlTree {

    Node root;

    class Node {
        int v;
        int h;
        Node left;
        Node right;
//        Node father;

        public Node(int v) {
            this.v = v;
        }
    }

    // 查找
    public boolean search(int key) {
        return search(root, key) != null;
    }

    private Node search(Node node, int v) {
        if (node == null) {
            return null;
        }
        if (node.v < v) {
            return search(node.right,v);
        }else if (node.v > v) {
            return search(node.left,v);
        }else {
            return node;
        }
    }

//    public void insert(int v) {
//        Node node = root;
//        if (node == null) {
//            root = new Node(v);
//        }else {
//            this.insertNode(node,v);
//        }
//    }
//
//    private int insertNode(Node node,int v) {
//        if (node == null){
//            return 0;
//        }
//        int balance = 0;
//        if (v <= node.v){
//            if (node.left == null) {
//                Node cur = new Node(v);
//                node.left = cur;
////                cur.father = node;
//                this.setHResBalance(node);
//                return 0;
//            }
//            balance = this.insertNode(node.left, v);
//        }else {
//            if (node.right == null) {
//                Node cur = new Node(v);
//                node.right = cur;
////                cur.father = node;
//                this.setHResBalance(node);
//                return 0;
//            }
//            balance = this.insertNode(node.right, v);
//        }
//        int curBalance = this.setHResBalance(node);
//        if (curBalance > 1 && balance > 0){
//            /**
//             * LL
//             *       3              2
//             *      / \            /\
//             *     2   0   ->     1 3(1)
//             *    /\             /\   \
//             *   1 0            0  0   0
//             *  /
//             * 0
//             * 右旋
//             */
//            //开始右旋
//            node = this.right(node);
//            node.right.h = 1;
//            curBalance = 0;
//        }else if (curBalance > 1 && balance < 0){
//            /**
//             * LR
//             *       3              1(2)
//             *      / \            /\
//             *     2   0   ->   2(1) 3(1)
//             *    /\             /\   \
//             *   0 1            0 0   0
//             *    /
//             *   0
//             * 先将左旋左子树，在对当前节点右旋
//             */
//            node = this.left(node.left);
//            node = this.right(node.father);
//
//            node.h = 2;
//            node.left.h = 1;
//            node.right.h = 1;
//            curBalance = 0;
//        }else if (curBalance < 1 && balance < 0){
//            /**
//             * RR
//             *       3              2
//             *      / \            /\
//             *     0   2   ->   3(1) 1
//             *        /\         /\   \
//             *       0  1       0 0    0
//             *           \
//             *            0
//             * 左旋
//             */
//            node = this.left(node);
//            node.left.h = 1;
//            curBalance = 0;
//
//        }else if (curBalance < 1 && balance > 0){
//            /**
//             * RL
//             *       3              1(2)
//             *      / \            /\
//             *     0   2   ->   3(1) 2(1)
//             *        /\         /\   \
//             *       0 1        0 0   0
//             *        /
//             *       0
//             * 先将右旋右子树，在对当前节点左旋
//             */
//            node = this.right(node.right);
//            node = this.left(node.father);
//
//            node.h = 2;
//            node.left.h = 1;
//            node.right.h = 1;
//            curBalance = 0;
//        }
//
//        return curBalance;
//    }

    public void insert(int v) {
        root = this.insertNode(root,v);
    }

    private Node insertNode(Node node,int v) {
        if (node == null){
            return new Node(v);
        }

        if (v <= node.v){
            node.left = this.insertNode(node.left, v);
        }else {
            node.right = this.insertNode(node.right, v);
        }
        node.h = Math.max(this.height(node.left),this.height(node.right)) + 1;
        int balance = this.getBalance(node);

        if (balance > 1 && this.getBalance(node.left) >= 0){
            /**
             * LL
             *       3              2
             *      / \            /\
             *     2   0   ->     1 3(1)
             *    /\             /\   \
             *   1 0            0  0   0
             *  /
             * 0
             * 右旋
             */
            //开始右旋
            node = this.right(node);
        }else if (balance > 1 && this.getBalance(node.left) < 0){
            /**
             * LR
             *       3              1(2)
             *      / \            /\
             *     2   0   ->   2(1) 3(1)
             *    /\             /\   \
             *   0 1            0 0   0
             *    /
             *   0
             * 先将左旋左子树，在对当前节点右旋
             */
            node.left = this.left(node.left);
            node = this.right(node);
        }else if (balance < -1 && this.getBalance(node.right) <= 0){
            /**
             * RR
             *       3              2
             *      / \            /\
             *     0   2   ->   3(1) 1
             *        /\         /\   \
             *       0  1       0 0    0
             *           \
             *            0
             * 左旋
             */
            node = this.left(node);
        }else if (balance < -1 && this.getBalance(node.right) > 0){
            /**
             * RL
             *       3              1(2)
             *      / \            /\
             *     0   2   ->   3(1) 2(1)
             *        /\         /\   \
             *       0 1        0 0   0
             *        /
             *       0
             * 先将右旋右子树，在对当前节点左旋
             */
            node.right = this.right(node.right);
            node = this.left(node);
        }

        return node;
    }

    private Node right(Node node) {
//        boolean isHead = isHead(node);
//        Node father = node.father;
        Node l = node.left;
        Node lr = node.left.right;

//        l.father = node.father;
        l.right = node;
//        node.father = l;
        node.left = lr;
        // 更新高度
        node.h = Math.max(height(node.left), height(node.right)) + 1;
        l.h = Math.max(height(l.left), height(l.right)) + 1;
//        if (lr != null){
//            lr.father = node;
//        }
//        if (isHead){
//            root = l;
//        }else {
//            father.right = l;
//        }
        return l;
    }


    private Node left(Node node) {
//        boolean isHead = isHead(node);
//        Node father = node.father;

        Node r = node.right;
        Node rl = node.right.left;

//        r.father = node.father;
        r.left = node;
//        node.father = r;
        node.right = rl;

        node.h = Math.max(height(node.left), height(node.right)) + 1;
        r.h = Math.max(height(r.left), height(r.right)) + 1;
//        if (rl != null){
//            rl.father = node;
//        }
//        if (isHead){
//            root = r;
//        }else {
//            father.left = r;
//        }
        return r;
    }

    public void delete(int v) {
        root = deleteNode(root,v);
    }

    private Node deleteNode(Node node,int v) {
        if (node == null){
            return null;
        }

        if (v < node.v){
            node.left = deleteNode(node.left,v);
        }else if (v > node.v){
            node.right = deleteNode(node.right,v);
        }else {
            //判断是否有左右树
            Node left = node.left;
            Node right = node.right;
            if (left == null && right == null){
                //可以直接删除
//                node.father = null;
                node = null;
            }else if (left != null && right != null){
                //需要把
                //找前序遍历前一个节点
                Node post = this.minValueNode(node.right);
                node.v = post.v;
//                Node preFather = pre.father;
//                preFather.h--;
//                preFather.right = null;
//                pre.father = null;
                node.right = deleteNode(node.right, post.v);
            } else if (left != null){
//                node.v = left.v;
//                node.h = left.h;
//                left.father = null;
//                node.left = null;

                node = node.left;
            }else {
//                node.v = right.v;
//                node.h = right.h;
//                node.right = null;

                node = node.right;
            }
        }
        if (node == null){
            return null;
        }

        node.h = Math.max(this.height(node.left),this.height(node.right)) + 1;
        int balance = getBalance(node);

        // ll
        if (balance > 1 && getBalance(node.left) >= 0) {
            return right(node);
        }
        //lr
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = left(node.left);
            return right(node);
        }
        //rr
        if (balance < -1 && getBalance(node.right) <= 0) {
            return left(node);
        }
        //rl
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = right(node.right);
            return left(node);
        }
        return node;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }
    private int height(Node node) {
        return node == null ? 0 : node.h;
    }

    private Node minValueNode(Node node) {
        while (node != null){
            node = node.left;
        }
        return node;
    }

    //层次遍历
    public void levelOrder() {
        Node node = root;
        if (node == null){
            System.out.println("[]");
            return;
        }
        int maxH = node.h;
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);
        StringBuffer sb = new StringBuffer("[");
        int count = (int)(Math.pow(2, maxH+1) - 1);
        while (!queue.isEmpty() && count > 0) {
            node = queue.poll();
            if (node == null){
                sb.append("null,");
            }else {
                sb.append(node.v).append(",");
                queue.add(node.left);
                queue.add(node.right);
            }
            count--;
        }
        sb.delete(sb.length()-1,sb.length());
        sb.append("]");
        System.out.println(sb);
    }

    //前序遍历
    public void preorder() {
        preorder(root);
        System.out.println();
    }
    private void preorder(Node node) {
        if (node != null) {
            System.out.print(node.v + " ");
            preorder(node.left);
            preorder(node.right);
        }
    }

    //中序遍历
    public void inorder() {
        inorder(root);
        System.out.println();
    }
    private void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.v + " ");
            inorder(node.right);
        }
    }

    //后序遍历
    public void postorder() {
        postorder(root);
        System.out.println();
    }
    private void postorder(Node node) {
        if (node != null) {
            postorder(node.right);
            postorder(node.left);
            System.out.print(node.v + " ");
        }
    }


    public static void main(String[] args) {
        AvlTree tree = new AvlTree();
//        int[] arr = {14,6,17,5,9,3}; //ll
//        int[] arr = {5,3,9,6,14,17}; //rr
//        int[] arr = {9,5,14,3,8,6}; //lr
//        int[] arr = {5,3,9,6,14,8}; //rl
//        int[] arr = {16, 3, 7, 11, 9, 26, 18, 14, 15};
        int[] arr = {4, 2, 6, 1, 3, 5, 15, 7, 16, 14};
        for (int i1 = 0; i1 < arr.length; i1++) {
            tree.insert(arr[i1]);
        }
//        tree.levelOrder();
//        tree.preorder();
        tree.inorder();
//        tree.postorder();

    }



}
