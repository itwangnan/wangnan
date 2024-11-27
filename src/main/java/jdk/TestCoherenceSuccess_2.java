package jdk;

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
public class TestCoherenceSuccess_2 {


    private final Holder h1 = new Holder();
    private final Holder h2 = h1;

    static final VarHandle VALUE;

    static {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            VALUE = l.findVarHandle(Holder.class,"a",int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Actor
    public void actor1(){
        h1.a = 1;
    }


    @Actor
    public void actor2(II_Result result){

        final Holder h1 = this.h1;
        final Holder h2 = this.h2;

        //初始化，后面h1、h2的load就会是非互相依赖的load
        h1.trap = 1;
        h2.trap = 1;


        result.r1 = (int)VALUE.getOpaque(h1);
        result.r2 = (int)VALUE.getOpaque(h2);
    }

}