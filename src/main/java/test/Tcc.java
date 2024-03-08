package test;

import str.ac.DoubleArrayTrie;

import java.util.TreeMap;

public class Tcc {

    private static String PATTERN = "\\s*[0-9]+";

    public static void main(String[] args) {

//        TreeSet<String> treeSet = new TreeSet<>();
//        treeSet.add("入门");
//        treeSet.add("自然");
//        treeSet.add("自然语言");
//        treeSet.add("自语");
//        treeSet.add("自然人");
//
//        for (String s : treeSet) {
//            System.out.println(s);
//        }
//
//        System.out.println(Character.hashCode('然'));
//        System.out.println(Character.hashCode('语'));
//        TripleArrayTrie trie2 = new TripleArrayTrie(treeSet);
//        System.out.println(trie2.getItem("jar"));

//        int maxValue = Character.MAX_VALUE;
//        System.out.println(maxValue);
        TreeMap<String,String> map = new TreeMap<>();
        map.put("入门","入门");
        map.put("自然","自然");
        map.put("自然语言","自然语言");
        map.put("自语","自语");
        map.put("自然人","自然人");

        DoubleArrayTrie<String> trie = new DoubleArrayTrie<>(map);

//        System.out.println(trie.exactMatchSearch(""));
//        treeSet.add("入门");
//        treeSet.add("自然");
//        treeSet.add("自然语言");
//        treeSet.add("自语");
//        treeSet.add("自然人");

////        TripleArrayTrie trie3 = new TripleArrayTrie(treeSet);
//        TripleArrayTrie2 trie2 = new TripleArrayTrie2(treeSet);
//
//
//////
//        System.out.println(trie2.search("自然"));

    }

}
