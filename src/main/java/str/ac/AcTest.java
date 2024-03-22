package str.ac;

import org.apache.lucene.util.RamUsageEstimator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;

public class AcTest {


    // 示例用法
    public static void main(String[] args) throws Exception {

        acdtTest();

//        AcTree acAutomaton = new AcTree();
//        acAutomaton.add("abacaba");
//        acAutomaton.add("aba");
//        acAutomaton.add("a");
//        acAutomaton.buildFailurePointer();
//        System.out.println(acAutomaton.match("\"sku_spec\""));

    }

    private static void acdtTest() throws Exception {

        TreeMap<String,String> map = new TreeMap<>();

        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv", x -> map.put(x,x));
        long start = System.currentTimeMillis();
        AcDoubleArrayTrie<String> trie = new AcDoubleArrayTrie<>(map);
        long end = System.currentTimeMillis();
        System.err.println(end - start);


        long start2 = System.currentTimeMillis();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t11.csv", x -> trie.getMostOccurrences(x.split(",")[1]));
        long end2 = System.currentTimeMillis();
        System.err.println(end2 - start2);

        //查看占用内存大小
        System.out.println(RamUsageEstimator.humanSizeOf(map));
        System.out.println(RamUsageEstimator.humanSizeOf(trie));
    }
//
    private static void acTest() throws Exception {
        AcTree acAutomaton = new AcTree();
        long start = System.currentTimeMillis();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv", x -> acAutomaton.add(x));
        acAutomaton.buildFailurePointer();
        long end = System.currentTimeMillis();
        System.err.println(end - start);


        long start2 = System.currentTimeMillis();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t11.csv", x -> {
            String text = x.split(",")[1];
            List<String> match = acAutomaton.match(text);
            System.out.println(text+" Found pattern:  " + match);
        });
        long end2 = System.currentTimeMillis();

        System.err.println(end2 - start2);
        System.out.println(RamUsageEstimator.humanSizeOf(acAutomaton));


    }

    public static void readFile(String path, Consumer<String> consumer) throws Exception {
        FileInputStream inputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            consumer.accept(str);
        }

        //close
        inputStream.close();
        bufferedReader.close();

    }


}



