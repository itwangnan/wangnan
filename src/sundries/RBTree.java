package sundries;

public class RBTree<K extends Comparable<K>,V> {


    private static final boolean RED = true;

    private static final boolean BLACK = false;

    private Node<K,V> root;

    private Node parentOf(Node node){
        if (node != null){
            return node.pNode;
        }
        return null;
    }

    private void setBlack(Node node){
        if (node != null){
            node.color = BLACK;
        }
    }

    private void setRed(Node node){
        if (node != null){
            node.color = RED;
        }
    }

    private boolean isBlack(Node node){
        if (node != null){
           return node.color == BLACK;
        }
        return false;
    }

    private boolean isRed(Node node){
        if (node != null){
            return node.color == RED;
        }
        return false;
    }

    //左旋
    private void leftRotate(Node node){
        if (node == null){
            return;
        }

        Node rNode = node.rNode;
        node.rNode = rNode.lNode;
        if (rNode != null && rNode.lNode != null){
            rNode.lNode.pNode = node;
        }
//        node.rNode = rNode.lNode;

        rNode.pNode = node.pNode;
        if (node.pNode != null){
            if(node.pNode.lNode == node){
                node.pNode.lNode = rNode;
            }else {
                node.pNode.rNode = rNode;
            }
        }else {
            root = rNode;
        }

        node.pNode = rNode;
        rNode.lNode = node;
    }

    /**
     * 右旋
     *       x               l
     *     /  \            /  \
     *    l     r         ll  x
     *  / \   /  \           / \
     * ll lr rl  rr         lr  r
     * @param node
     * 1.现将x的左子树改成lr，lr的父节点改成x
     * 2.将l的父节点改成x的父节点，x的父节点左/右子树改成l
     * 3。x的父节点改成l，l的右节点改成x
     */
    private void rightRotate(Node node){

        if (node == null){
            return;
        }

        Node lNode = node.lNode;
        node.lNode = lNode.rNode;
        if (lNode != null && lNode.rNode != null){
            lNode.rNode.pNode = node;
        }

        Node pNode = node.pNode;
        lNode.pNode = pNode;
        if (pNode != null){
            if (pNode.lNode == node){
                pNode.lNode = lNode;
            }else {
                pNode.rNode = lNode;
            }

        }else {
            root = lNode;
        }

        node.pNode = lNode;
        lNode.rNode = node;


    }

    public void insert(K k,V v){

        insert(new Node<>(k,v,null,null,null,RED));

    }

    private void insert(Node node){

        //根节点初始化
        if (root == null){
            node.color = BLACK;
            root = node;
            return;
        }


        Node parent = null;
        Node next = root;
        while (next != null){
            parent = next;
            if (next.key.compareTo(node.key) > 0){
                next = next.lNode;
            }else {
                next = next.rNode;
            }
        }

        if (parent.key.compareTo(node.key) > 0){
            parent.lNode = node;
        }else {
            parent.rNode = node;
        }
        node.pNode = parent;

        //平衡
        inOrderPrint(node);



    }


    /**
     *
     * 1.如果节点的父节点和父节点的兄弟节点都是红色，改成黑色，爷节点改成红色
     * 2。如果父兄弟节点为黑色或null，父节点为左节点，当前节点为左节点，
     * red ll：以爷节点进行右旋
     * 2。如果父兄弟节点为黑色或null，父节点为左节点，当前节点为右节点，
     * red lr：先将父节点左旋，以爷节点进行右旋
     * 2。如果父兄弟节点为黑色或null，父节点为右节点，当前节点为左节点，
     * red rl：先将父节点右旋，以爷节点进行左旋
     * 2。如果父兄弟节点为黑色或null，父节点为右节点，当前节点为右节点，
     * red rr：以爷节点进行左旋
     *
     * @param node
     */
    private void inOrderPrint(Node node) {
        while (!(node == null || node.pNode == null || node.pNode.pNode == null || node.pNode.color == BLACK)){

            Node pNode = node.pNode;
            Node ppNode = node.pNode.pNode;

            if (ppNode.lNode != null && ppNode.rNode != null && ppNode.lNode.color == RED && ppNode.rNode.color == RED){
                ppNode.lNode.color = BLACK;
                ppNode.rNode.color = BLACK;
                ppNode.color = RED;
            }
            else if (ppNode.lNode == pNode && (ppNode.rNode == null || ppNode.rNode.color == BLACK)){

                if (pNode.rNode == node){
                    ppNode.color = RED;
                    pNode.color = BLACK;
                    this.leftRotate(pNode);
                }

                ppNode.color = RED;
                pNode.color = BLACK;
                this.rightRotate(ppNode);
            }
            else if (ppNode.rNode == pNode && (ppNode.lNode == null || ppNode.lNode.color == BLACK)){

                if (pNode.lNode == node){
                    ppNode.color = RED;
                    pNode.color = BLACK;
                    this.rightRotate(pNode);
                }
                ppNode.color = RED;
                pNode.color = BLACK;
                this.leftRotate(ppNode);
            }else {
                return;
            }
            node = ppNode;
            this.root.color = BLACK;
        }
    }


    public void find(){
        if (root == null){
            return;
        }
        this.find(root);


    }

    public void find(Node node){
        Stack<Node> stack = initStack(node);
        Node next = root;
        while (true){
            if (next != null){
                stack.insert(next);
                next = next.lNode;
            }else {
                Node rNode = stack.get();
                next = rNode.rNode;
                System.out.println(rNode.key);
            }
            if (next == null && !stack.exist()){
                return;
            }

        }

    }

    private Stack initStack(Node node) {
        Stack<Node> stack = new Stack();
        return stack;
    }



    private class Node<K extends Comparable<K>,V>{

        private K key;

        private V value;

        private Node lNode;

        private Node rNode;

        private Node pNode;

        private boolean color;

        public Node(K key, V value, Node lNode, Node rNode, Node pNode, boolean color) {
            this.key = key;
            this.value = value;
            this.lNode = lNode;
            this.rNode = rNode;
            this.pNode = pNode;
            this.color = color;
        }
    }





}
