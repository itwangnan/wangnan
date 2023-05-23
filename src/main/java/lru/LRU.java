package lru;

import java.util.HashMap;
import java.util.Map;

public class LRU {

    private Node<Integer> firstNode;

    private Node<Integer> lastNode;

    private Map<Integer,Node<Integer>> map;

    private int capacity;


    public LRU(int capacity) {
        this.capacity = capacity;
    }

    //如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1
    public int get(int key){

        Node<Integer> node = map.get(key);

        if (node == null){
            return -1;
        }

        return node.t;
    }

    // 如果关键字已经存在，则变更其数据值；如果关键字不存在，则插入该组「关键字-值」。
    public void put(int key, int value){
        Node<Integer> lastNode = this.lastNode;
        Node<Integer> firstNode = this.firstNode;
        Map<Integer, Node<Integer>> map = this.map;

        int capacity = this.capacity;
        if (map == null){
            map = new HashMap<>();
            this.map = map;
        }

        //已经存在就直接修改值
        Node<Integer> updateAfterNode = map.get(key);
        if (map.get(key) != null){
            updateAfterNode.t = value;
            //放到最后一位
            //之后节点
            Node<Integer> afterNode = updateAfterNode.afterNode;
            //之前节点
            Node<Integer> beforeNode = updateAfterNode.beforeNode;

            beforeNode.afterNode = afterNode;


            this.lastNode = updateAfterNode;
            lastNode.afterNode = updateAfterNode;

            return;
        }



        Node<Integer> node = new Node<>(value,null,null);

        if (firstNode == null && lastNode == null){
            this.firstNode = node;
            this.lastNode = node;

        }else {
            //不是第一次
            node.afterNode = firstNode.beforeNode == null ? firstNode : firstNode.beforeNode;
            node.beforeNode = null;

            this.firstNode = node;

            firstNode.afterNode = node;
        }

        if (this.map.size() == capacity){

            map.remove(lastNode.t);
            map.put(key,node);


            this.lastNode = lastNode.beforeNode;

            lastNode.beforeNode = null;
        }




        this.map.putIfAbsent(key, node);

    }


    class Node<T>{

        T t;

        Node<T> afterNode;

        Node<T> beforeNode;

        public Node(T t, Node<T> afterNode, Node<T> beforeNode) {
            this.t = t;
            this.afterNode = afterNode;
            this.beforeNode = beforeNode;
        }
    }

}
