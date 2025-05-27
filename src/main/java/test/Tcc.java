package test;

import org.apache.lucene.util.RamUsageEstimator;
import str.ac.DoubleArrayTrie;
import str.ac.MyDoubleArrayTrie;
import str.ac.MyTripleArrayTrie;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static str.ac.AcTest.readFile;

public class Tcc {

    private static void myDoubleTest() throws Exception {

        TreeSet<String> set = new TreeSet<>();

        readFile("/图号2.csv", x -> set.add(x));
        long start = System.currentTimeMillis();
        MyDoubleArrayTrie trie = new MyDoubleArrayTrie(set);
        long end = System.currentTimeMillis();
        System.err.println(end - start);

        set.forEach(x -> {
            if (trie.search(x)){
                System.out.println("set.add(\""+x+"\");");
            }

        });
        //查看占用内存大小
//        System.out.println(RamUsageEstimator.humanSizeOf(map));
        System.out.println(RamUsageEstimator.sizeOf(trie));
    }

    private static void hanlpTest() throws Exception {

        TreeMap<String,String> map = new TreeMap<>();

        readFile("/图号2.csv", x -> map.put(x,x));
        long start = System.currentTimeMillis();
        DoubleArrayTrie<String> trie = new DoubleArrayTrie<>(map);
        long end = System.currentTimeMillis();
        System.err.println(end - start);


        map.forEach((x,y) -> {
            if (trie.exactMatchSearch(x) < 0){
                System.out.println("set.add(\""+x+"\");");
            }
        });
        //查看占用内存大小
//        System.out.println(RamUsageEstimator.humanSizeOf(map));
        System.out.println(RamUsageEstimator.sizeOf(trie));
    }

    private static void myTest() throws Exception {
//
        TreeSet<String> map = new TreeSet<>();

        long start = System.currentTimeMillis();
        readFile("/图号2.csv", x -> map.add(x));
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
