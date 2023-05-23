package my_t;

import java.util.ArrayList;
import java.util.List;

public class Trie {

    TrieNode root;//记录前缀树的根
    /** Initialize your data structure here. */
    public Trie() {
        root = new TrieNode();
    }

    /**
     * 插入
     * word 单词
     */
    public void insert(String word){

        TrieNode ptr = root;

        char[] chars = word.toCharArray();


        for (char c : chars) {
            TrieNode node = findWord(ptr.child, c);
            if (node == null){
                node = new TrieNode(c);
                ptr.child.add(node);
            }
            ptr = node;
        }

        ptr.is_end = true;

    }

    /**
     * 查询单词是否在树中
     * @param word
     * @return
     */
    public boolean search(String word) {

        TrieNode ptr = root;

        char[] chars = word.toCharArray();

        for (char c : chars) {

            TrieNode node = findWord(ptr.child, c);
            if (node == null){
                return false;
            }
            ptr = node;
        }


        return ptr.is_end;

    }


    /**
     * 查询前缀是否在树中
     * @param prefix
     * @return
     */
    public boolean startsWith(String prefix) {

        TrieNode ptr = root;

        char[] chars = prefix.toCharArray();

        for (char c : chars) {

            TrieNode node = findWord(ptr.child, c);
            if (node == null){
                return false;
            }
            ptr = node;
        }


        return true;

    }




    private TrieNode findWord(List<TrieNode> child,char c){
        for (TrieNode trieNode : child) {
            if (trieNode.word == c){
                return trieNode;
            }
        }
        return null;
    }








    class TrieNode{
        List<TrieNode> child;//记录孩子节点
        char word;
        boolean is_end;//记录当前节点是不是一个单词的结束字母
        public TrieNode(char c){//
            this();
            word = c;
        }

        public TrieNode(){//
            child = new ArrayList<>();//子节点数组长度26，0：‘a’，1：‘b’.....
            is_end = false;
        }
    }


    public static void main(String[] args) {
        Trie trie = new Trie();

        trie.insert("word");
        trie.insert("text");
        trie.insert("car");
        trie.insert("worded");


        System.out.println(trie.search("tex"));
        System.out.println(trie.startsWith("wo"));

    }


}
