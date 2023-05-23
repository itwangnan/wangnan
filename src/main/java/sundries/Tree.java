package sundries;

import java.util.LinkedList;

public class Tree {

    //头节点
    private Node head;

    //黑高
    private int blackCount;

    private static final short RED = 0;

    private static final short BLACK = 1;

    //增加
    private void add(int date) {

        Node next = head,parent = null;

        while (next != null) {
            parent = next;
            if (next.date > date) {
                next = next.lNode;
            }else {
                next = next.rNode;
            }
        }
        Node node = new Node(date, null, null, parent, RED);
        if (parent == null){
            head = node;
            node.color = BLACK;
            return;
        }

        if (parent.date > date){
            parent.lNode = node;
        }else {
            parent.rNode = node;
        }

        //染色 旋转
        if (parent.color == BLACK){
            return;
        }

        Node ppNode = parent.pNode;
        if (ppNode == null){
            return;
        }

        //变色
        if (parent == ppNode.lNode && (ppNode.rNode == null || ppNode.rNode.color == BLACK) && ppNode.lNode.color == RED){


            if (parent.rNode == node){
                //先左旋
                ppNode.lNode = node;
                node.pNode = ppNode;

                parent.pNode = node;
                node.rNode = node.lNode;

                parent = node;
                node = parent;
            }




        }else if (parent == ppNode.rNode && (ppNode.rNode == null || ppNode.rNode.color == BLACK) && ppNode.lNode.color == BLACK){
            if (parent.lNode == node){
                //先右旋


            }
        }else {
            System.out.println("猜猜我是谁");
        }



    }

    //删除
    private void del(int date){




    }

    //查询 中序
    private void select(int date){


    }

    //查询 中序
    private void update(int date){




    }




    private final class Node{

        private int date;

        private Node lNode;

        private Node rNode;

        private Node pNode;

        // 0为红 1为黑
        private short color;

        public Node(int date, Node lNode, Node rNode, Node pNode, short color) {
            this.date = date;
            this.lNode = lNode;
            this.rNode = rNode;
            this.pNode = pNode;
            this.color = color;
        }
    }

}
