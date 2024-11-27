package jdk;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

//import java.lang.invoke.VarHandle;

@JCStressTest
@State
@Outcome(id = "1, 1",expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(id = "0, 1",expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(id = "1, 0",expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(id = "0, 0",expect = Expect.ACCEPTABLE_INTERESTING, desc = "illegal")
public class TestVolatile_Volatile {

    volatile int x;
    volatile int y;


    @Actor
    public void actor1(II_Result result){
        x = 1;
        result.r2 = y;
    }

    @Actor
    public void actor2(II_Result result){
        y = 1;
        result.r1 = x;
    }
}
