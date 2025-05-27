package jdktest;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;


/**
 * 测试相干性,plain
 * 骗过 Java 编译器造成这种乱序
 */
@JCStressTest
@State
@Outcome(id = "1, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
@Outcome(id = "0, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
@Outcome(id = "1, 0",expect = Expect.ACCEPTABLE_INTERESTING, desc = "reordered")
@Outcome(id = "0, 0",expect = Expect.ACCEPTABLE, desc = "ordered")
public class TestCoherence_2 {


    private final Holder h1 = new Holder();
    private final Holder h2 = h1;


    @Actor
    public void actor1(){
        h1.a = 1;
    }


    //C2编译器优化导致
    @Actor
    public void actor2(II_Result result){

        final Holder h1 = this.h1;
        final Holder h2 = this.h2;

        //初始化，后面h1、h2的load就会是非互相依赖的load
        h1.trap = 1;
        h2.trap = 1;


        result.r1 = h1.a;
        result.r2 = h2.a;
    }

}