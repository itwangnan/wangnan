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
//        myTest();

        TreeSet<String> set = new TreeSet<>();




        set.add("原神大眼表情包KT8");
        set.add("原神搞怪Q版HS1");
        set.add("原神搞怪Q版HS10");

        set.add("原神派蒙七七HS1");
//        set.add("原神派蒙七七HS10");
//        set.add("原神派蒙七七HS11");
//        set.add("原神派蒙七七HS12");
//        set.add("原神派蒙七七HS13");
//        set.add("原神派蒙七七HS14");
//        set.add("原神派蒙七七HS15");
//        set.add("原神派蒙七七HS16");
//        set.add("原神派蒙七七HS17");
//        set.add("原神派蒙七七HS18");
//        set.add("原神派蒙七七HS19");
//        set.add("原神派蒙七七HS2");
//        set.add("原神派蒙七七HS20");
//        set.add("原神派蒙七七HS21");
//        set.add("原神派蒙七七HS22");
//        set.add("原神派蒙七七HS23");
//        set.add("原神派蒙七七HS24");
//        set.add("原神派蒙七七HS25");
//        set.add("原神派蒙七七HS26");
//        set.add("原神派蒙七七HS27");
//        set.add("原神派蒙七七HS28");
//        set.add("原神派蒙七七HS29");
//        set.add("原神派蒙七七HS3");



        MyTripleArrayTrie trie = new MyTripleArrayTrie(set);

        System.out.println(trie.search("原神派蒙七七HS10"));
    }

    private static void hanlpTest() throws Exception {
//


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
//        System.out.println(RamUsageEstimator.humanSizeOf(trie));
    }

}
