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

    public int get(int key){

        Node<Integer> node = map.get(key);

        if (node == null){
            return -1;
        }

        return node.value;
    }

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
        if (updateAfterNode != null){
            updateAfterNode.value = value;
            //之后节点
            Node<Integer> afterNode = updateAfterNode.afterNode;
            //之前节点
            Node<Integer> beforeNode = updateAfterNode.beforeNode;

            if (beforeNode != null){
                beforeNode.afterNode = afterNode;
            }
            if (afterNode != null){
                afterNode.beforeNode = beforeNode;
            }
            //证明 尾节点要替换
            if (updateAfterNode == lastNode){
                this.lastNode = beforeNode;
            }

            updateAfterNode.beforeNode = null;
            updateAfterNode.afterNode = firstNode;
            this.firstNode = updateAfterNode;

            firstNode.beforeNode = updateAfterNode;


            return;
        }



        Node<Integer> node = new Node<>(key,value,null,null);

        if (firstNode == null && lastNode == null){
            this.firstNode = node;
            this.lastNode = node;
        }else {
            //不是第一次
            node.afterNode = firstNode;
            node.beforeNode = null;

            this.firstNode = node;
            firstNode.beforeNode = node;
        }

        if (this.map.size() >= capacity){

            map.remove(lastNode.key);
            map.put(key,node);


            this.lastNode = lastNode.beforeNode;
            if (lastNode.beforeNode != null){
                lastNode.beforeNode.afterNode = null;
            }

            lastNode.beforeNode = null;

        }else {
            this.map.putIfAbsent(key, node);
        }
    }


    class Node<T>{

        T key;

        T value;

        Node<T> afterNode;

        Node<T> beforeNode;

        public Node(T key,T value, Node<T> afterNode, Node<T> beforeNode) {
            this.key = key;
            this.value = value;
            this.afterNode = afterNode;
            this.beforeNode = beforeNode;
        }
    }

}
