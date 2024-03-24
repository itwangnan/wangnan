package test;

import org.apache.lucene.util.RamUsageEstimator;
import str.ac.DoubleArrayTrie;
import str.ac.MyTripleArrayTrie;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;

import static str.ac.AcTest.readFile;

public class Tcc {

    public static void main(String[] args) throws Exception {

        myTest();

//        TreeSet<String> set = new TreeSet<>();
//        set.add("原神芙宁娜贰JR1");
//        MyTripleArrayTrie trie = new MyTripleArrayTrie(set);
//        System.out.println(trie.search("原神芙宁娜贰JR1"));
    }

    private static void hanlpTest() throws Exception {

        TreeMap<String,String> map = new TreeMap<>();

        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv", x -> map.put(x,x));
        long start = System.currentTimeMillis();
        DoubleArrayTrie<String> trie = new DoubleArrayTrie<>(map);
        long end = System.currentTimeMillis();
        System.err.println(end - start);


        map.forEach((x,y) -> {
            System.out.println(trie.containsKey(x));
        });
        //查看占用内存大小
        System.out.println(RamUsageEstimator.humanSizeOf(map));
        System.out.println(RamUsageEstimator.humanSizeOf(trie));
    }

    private static void myTest() throws Exception {
//
        TreeSet<String> map = new TreeSet<>();

        long start = System.currentTimeMillis();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv", x -> map.add(x));
        long end = System.currentTimeMillis();
        System.err.println(end - start);

        MyTripleArrayTrie trie = new MyTripleArrayTrie(map);

//        System.out.println(trie.search("龙年龙宝宝线条KDX1"));

        int count = 0;
        for (String x : map) {
            if (!trie.search(x)){
                System.out.println("set.add(\""+x+"\");");
                count++;
            }
        }

        System.out.println(count);

        //查看占用内存大小
//        System.out.println(RamUsageEstimator.humanSizeOf(map));
        System.out.println(RamUsageEstimator.humanSizeOf(trie));
    }

}
