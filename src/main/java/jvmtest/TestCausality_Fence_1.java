package jvmtest;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.III_Result;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
//import java.lang.invoke.VarHandle;


/**
 * 测试因果性,使用屏障可以解决
 */
@JCStressTest
@State
//@Outcome(id = "1, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
//@Outcome(id = "0, 1",expect = Expect.ACCEPTABLE, desc = "ordered")
//@Outcome(id = "1, 1, 0",expect = Expect.ACCEPTABLE_INTERESTING, desc = "reordered")
@Outcome(expect = Expect.ACCEPTABLE, desc = "ordered")
public class TestCausality_Fence_1 {


    int x;
    int y;
    int z;
    static final VarHandle VALUE_1;
//    static final VarHandle VALUE_2;

    static {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            VALUE_1 = l.findVarHandle(TestCausality_Fence_1.class,"z",int.class);
//            VALUE_2 = l.findVarHandle(TestCausality_Fence.class,"y",int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Actor
    public void actor1(){
        //y = 1 x一定为1
        x = 1;
        //storestrore loadstore
        VarHandle.releaseFence();
        y = 1;

        z = 1;

    }


    @Actor
    public void actor2(III_Result result){
        result.r1 = z;//1

        result.r2 = y;//1
        //loadLoad loadStore
        VarHandle.acquireFence();

        result.r3 = x;//1


    }

}