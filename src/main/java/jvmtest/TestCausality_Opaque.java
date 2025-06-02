package jvmtest;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
//import java.lang.invoke.VarHandle;


/**
 * 测试因果性
 */
@JCStressTest
@State
@Outcome(id = "1, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
@Outcome(id = "0, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
@Outcome(id = "1, 0",expect = Expect.ACCEPTABLE_INTERESTING, desc = "reordered")
@Outcome(id = "0, 0",expect = Expect.ACCEPTABLE, desc = "ordered")
public class TestCausality_Opaque {


    int x;
    int y;
    static final VarHandle VALUE_1;
    static final VarHandle VALUE_2;

    static {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            VALUE_1 = l.findVarHandle(TestCausality_Opaque.class,"x",int.class);
            VALUE_2 = l.findVarHandle(TestCausality_Opaque.class,"y",int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    @Actor
    public void actor1(){
        VALUE_1.setOpaque(this,1);
        VALUE_2.setOpaque(this,1);
    }


    @Actor
    public void actor2(II_Result result){
        result.r1 = (int)VALUE_1.getOpaque(this);
        result.r2 = (int)VALUE_2.getOpaque(this);
    }

}