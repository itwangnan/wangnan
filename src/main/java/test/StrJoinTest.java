package test;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;



@BenchmarkMode(Mode.AverageTime) // 吞吐量
@OutputTimeUnit(TimeUnit.MICROSECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程分配一个实例
public class StrJoinTest {


    @Benchmark
    public String test1() {
        String a1 = "1";
        String a2 = "2";
        String a3 = "3";
        String a4 = "4";
        String a5 = "5";
        String a6 = "6";

        String str = a1 + a2 + a3 + a4 + a5 + a6;
        return str;
    }

    @Benchmark
    public String test2() {
        String a1 = "1";
        String a2 = "2";
        String a3 = "3";
        String a4 = "4";
        String a5 = "5";
        String a6 = "6";
        StringBuilder sb = new StringBuilder();
        sb.append(a1).append(a2).append(a3).append(a4).append(a5).append(a6);
        return sb.toString();
    }

    @Benchmark
    public String test3() {
        String a1 = "1";
        String a2 = "2";
        String a3 = "3";
        String a4 = "4";
        String a5 = "5";
        String a6 = "6";
        String res = new StringBuilder().append(a1).append(a2).append(a3).append(a4).append(a5).append(a6).toString();
        return res;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(StrJoinTest.class.getSimpleName()).forks(1).warmupIterations(5)
                .measurementIterations(5).build();
        new Runner(options).run();
    }
}
