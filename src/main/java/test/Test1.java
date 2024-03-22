package test;

import org.apache.lucene.util.RamUsageEstimator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import str.ac.AcDoubleArrayTrie;
import str.ac.AcTree;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static str.ac.AcTest.readFile;


public class Test1 {

    public static void main(String[] args) throws Exception {
        AcTree acAutomaton = new AcTree();
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv", x -> acAutomaton.add(x));
        acAutomaton.buildFailurePointer();


        for (int i = 3; i <= 3 ; i++) {
            String fileName = String.format("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t%s.csv", i);
            readFile(fileName, x -> {
                String text = x.split(",")[1];
                List<String> match = acAutomaton.match(text);
                System.out.println(text);
                System.out.println(" Found pattern:  " + match);
                System.out.println();
            });
        }
        System.out.println(RamUsageEstimator.humanSizeOf(acAutomaton));
    }

}
