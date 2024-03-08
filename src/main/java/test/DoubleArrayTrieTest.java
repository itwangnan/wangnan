package test;

import str.ac.DoubleArrayTrie;

import java.util.TreeMap;

import static str.ac.AcTest.readFile;

public class DoubleArrayTrieTest {

    public static void main(String[] args) throws Exception {
        TreeMap<String, String> map = new TreeMap<>();

        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv",x -> {
            map.put(x,x);
        });
        long start = System.currentTimeMillis();
        DoubleArrayTrie<String> trie = new DoubleArrayTrie<>();
        trie.build(map);
        long end = System.currentTimeMillis();
        System.err.println(end-start);


        long start2 = System.currentTimeMillis();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t11.csv",x -> trie.parseText(x.split(",")[1],
                (a,b,c) -> {
                    System.out.println(c);
        }));
        long end2 = System.currentTimeMillis();
        System.err.println(end2-start2);

        System.out.println(trie.getSize());
    }
}
