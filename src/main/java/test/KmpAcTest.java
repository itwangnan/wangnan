package test;

import org.apache.lucene.util.RamUsageEstimator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import str.KMP_Str;
import str.ac.AcDoubleArrayTrie;
import str.ac.AcTree;
import str.ac.DoubleArrayTrie;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static str.ac.AcTest.readFile;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程分配一个实例
public class KmpAcTest {

    /**
     * 10 * 5 + 4 * 10 = 90w 的商品标题
     * 11.5w                 的图号
     */
    @Benchmark
    public void acTest() throws Exception {
        AcTree acAutomaton = new AcTree();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv", x -> acAutomaton.add(x));
        acAutomaton.buildFailurePointer();


        for (int i = 3; i <= 16 ; i++) {
            String fileName = String.format("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t%s.csv", i);
            readFile(fileName, x -> {
                String text = x.split(",")[1];
                List<String> match = acAutomaton.match(text);
//                System.out.println(text+" Found pattern:  " + match);
            });
        }
//        System.out.println(RamUsageEstimator.humanSizeOf(acAutomaton));
    }

    @Benchmark
    public void acDoubleTest() throws Exception {
        TreeMap<String, String> map = new TreeMap<>();

        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv",x -> {
            map.put(x,x);
        });
//        long start = System.currentTimeMillis();
        AcDoubleArrayTrie<String> trie = new AcDoubleArrayTrie<>();
        trie.build(map);
//        long end = System.currentTimeMillis();
//        System.err.println("ac构造时间:" + (end-start));


//        long start2 = System.currentTimeMillis();
        for (int i = 3; i <= 16 ; i++) {
            String fileName = String.format("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t%s.csv", i);
            readFile(fileName,x -> trie.parseText(x.split(",")[1],
                    (a,b,c) -> {
//                    System.out.println(c);
                    }));
        }
//        long end2 = System.currentTimeMillis();
//        System.err.println("ac构造匹配:" + (end2-start2));
//        System.out.println(RamUsageEstimator.humanSizeOf(trie));
    }
//
//    @Benchmark
//    public void kmpTest() throws Exception {
//        List<String> patternList = new ArrayList<>();
//        List<String> textList = new ArrayList<>();
//
//        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv", patternList::add);
//        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t11.csv",x -> textList.add(x.split(",")[1]));
//
//        for (String p : patternList) {
//            int[] next = KMP_Str.computePrefixFunction(p.toCharArray());
//            for (String t : textList) {
//                KMP_Str.kmp(t, p,next);
//            }
//        }
//
//
//    }




    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(KmpAcTest.class.getSimpleName()).forks(1)
                //预热3次
                .warmupIterations(3)
                // 度量5轮
                .measurementIterations(5).build();
        new Runner(options).run();
    }

}
