package sundries;

public class Stack<K> {
    StackNode head;

    public class StackNode<K> {
        K node;
        StackNode next;

        public StackNode(K node, StackNode next) {
            this.node = node;
            this.next = next;
        }
    }

    void insert(K node) {
        StackNode stackNode = new StackNode(node,null);
        if (head == null) {
            head = stackNode;
            return;
        }
        stackNode.next = head;
        head = stackNode;
    }

     K get() {
        if (head == null) {
            return null;
        }

        StackNode<K> stackNode = head;
        StackNode next = stackNode.next;
        head = next;
        stackNode.next = null;
        return stackNode.node;
    }

    boolean exist(){
        return head != null;
    }
}
