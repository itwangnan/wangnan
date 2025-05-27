package jdktest;


import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
//import java.lang.invoke.VarHandle;

/**
 * Opaque禁止 Java 编译器优化，但是没有涉及任何的内存屏障。
 */
@JCStressTest
@State
@Outcome(id = "1, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
@Outcome(id = "0, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
@Outcome(id = "1, 0",expect = Expect.ACCEPTABLE_INTERESTING, desc = "reordered")
@Outcome(id = "0, 0",expect = Expect.ACCEPTABLE, desc = "ordered")
public class TestCoherenceSuccess_1 {


    int x;

    static final VarHandle VALUE;

    static {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            VALUE = l.findVarHandle(TestCoherenceSuccess_1.class,"x",int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Actor
    public void actor1(){
        x = 1;
    }


    @Actor
    public void actor2(II_Result result){
        result.r1 = (int)VALUE.getOpaque(this);
        result.r2 = (int)VALUE.getOpaque(this);
    }

}
