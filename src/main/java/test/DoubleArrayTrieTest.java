package test;

import str.ac.DoubleArrayTrie;
import str.ac.MyDoubleArrayTrie;

import java.util.TreeMap;
import java.util.TreeSet;

import static str.ac.AcTest.readFile;

public class DoubleArrayTrieTest {

    public static void main(String[] args) throws Exception {

//        TreeMap<String, String> map = new TreeMap<>();
//        readFile("/图号2.csv",x -> {
//            map.put(x,x);
//        });
//        long start = System.currentTimeMillis();
//        DoubleArrayTrie<String> trie = new DoubleArrayTrie<>();
//        trie.build(map);
//        long end = System.currentTimeMillis();
//        System.err.println(end-start);
//
//
//        long start2 = System.currentTimeMillis();
//        readFile("/t11.csv",x -> trie.parseText(x.split(",")[1],
//                (a,b,c) -> {
//                    System.out.println(c);
//        }));
//        long end2 = System.currentTimeMillis();
//        System.err.println(end2-start2);
//
//        System.out.println(trie.getSize());


//        TreeMap<String, String> map = new TreeMap<>();
//        map.put("as1","as1");
//        map.put("as2","as2");
//        DoubleArrayTrie<String> trie1 = new DoubleArrayTrie<>(map);
//        System.out.println(trie1.exactMatchSearch("as1"));

        TreeSet<String> set = new TreeSet<>();
        set.add("aaa");
        set.add("abcdc");
        set.add("abd");
        set.add("acd");
        set.add("ba");
        MyDoubleArrayTrie trie1 = new MyDoubleArrayTrie(set);

        TreeMap<String,String> map = new TreeMap<>();
        map.put("aaa","aaa");
//        map.put("abcdc","abcdc");
//        map.put("abd","abd");
//        map.put("acd","acd");
//        map.put("ba","ba");
        DoubleArrayTrie trie2 = new DoubleArrayTrie<>(map);

        System.out.println(trie1.search("aaa"));
        System.out.println(trie1.search("abcdc"));
        System.out.println(trie1.search("abd"));
        System.out.println(trie1.search("acd"));
        System.out.println(trie1.search("ba"));

//        System.out.println(trie2.exactMatchSearch("aaa"));

//        System.out.println(trie.search("ab"));

//        System.out.println(trie.search("abcd"));
//        System.out.println(trie.search("abd"));
//        System.out.println(trie.search("acd"));
//        System.out.println(trie.search("asdf"));


    }
}
