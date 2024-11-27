package leetcode.refine.binary_tree;

public class BinaryTree<T> {

    T value;

    BinaryTree<T> left;

    BinaryTree<T> right;

    public BinaryTree(T value, BinaryTree<T> left, BinaryTree<T> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public BinaryTree() {
    }
}
