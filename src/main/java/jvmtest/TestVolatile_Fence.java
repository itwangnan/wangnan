package jvmtest;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import java.lang.invoke.VarHandle;

@JCStressTest
@State
@Outcome(id = "1, 1",expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(id = "0, 1",expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(id = "1, 0",expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(id = "0, 0",expect = Expect.ACCEPTABLE_INTERESTING, desc = "illegal")
public class TestVolatile_Fence {

    int x;
    int y;


    @Actor
    public void actor1(II_Result result){
        VarHandle.releaseFence();
        x = 1;
        VarHandle.fullFence();
        result.r2 = y;
        VarHandle.acquireFence();
    }

    @Actor
    public void actor2(II_Result result){
        VarHandle.releaseFence();
        y = 1;
        VarHandle.fullFence();
        result.r1 = x;
        VarHandle.acquireFence();

    }
}
