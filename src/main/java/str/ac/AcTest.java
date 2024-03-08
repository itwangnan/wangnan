package str.ac;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import org.apache.lucene.util.RamUsageEstimator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;
import java.util.function.Consumer;

public class AcTest {


    // 示例用法
    public static void main(String[] args) throws Exception {
        acTest();

//        acdtTest();
    }

    private static void acdtTest() throws Exception {
        TreeMap<String, String> map = new TreeMap<>();

        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv",x -> {
            map.put(x,x);
        });
        long start = System.currentTimeMillis();
        AhoCorasickDoubleArrayTrie<String> trie = new AhoCorasickDoubleArrayTrie<>();
        trie.build(map);
        long end = System.currentTimeMillis();
        System.err.println(end-start);


        long start2 = System.currentTimeMillis();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t11.csv",x -> trie.parseText(x.split(",")[1]));
        long end2 = System.currentTimeMillis();
        System.err.println(end2-start2);
        System.out.println(trie.size());
    }

    private static void acTest() throws Exception {
        AcTree acAutomaton = new AcTree();
        long start = System.currentTimeMillis();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv",x -> acAutomaton.add(x));
        acAutomaton.buildFailurePointer();
        long end = System.currentTimeMillis();
        System.err.println(end-start);


        long start2 = System.currentTimeMillis();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t11.csv",x -> acAutomaton.match(x.split(",")[1]));
        long end2 = System.currentTimeMillis();
        System.err.println(end2-start2);

        System.out.println(RamUsageEstimator.humanSizeOf(acAutomaton));


    }

    public static void readFile(String path, Consumer<String> consumer) throws Exception {
        FileInputStream inputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        while((str = bufferedReader.readLine()) != null) {
            consumer.accept(str);
        }

        //close
        inputStream.close();
        bufferedReader.close();

    }


}



