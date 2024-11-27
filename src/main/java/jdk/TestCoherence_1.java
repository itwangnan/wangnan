package jdk;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;


/**
 * 测试相干性,plain
 */
@JCStressTest
@State
@Outcome(id = "1, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
@Outcome(id = "0, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
@Outcome(id = "1, 0",expect = Expect.ACCEPTABLE_INTERESTING, desc = "reordered")
@Outcome(id = "0, 0",expect = Expect.ACCEPTABLE, desc = "ordered")
public class TestCoherence_1 {


    int x;

    @Actor
    public void actor1(){
        x = 1;
    }


    @Actor
    public void actor2(II_Result result){
        result.r1 = x;
        result.r2 = x;
    }

}