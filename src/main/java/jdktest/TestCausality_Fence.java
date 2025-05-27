package jdktest;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

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
public class TestCausality_Fence {


    int x;
    int y;
    static final VarHandle VALUE_1;
    static final VarHandle VALUE_2;

    static {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            VALUE_1 = l.findVarHandle(TestCausality_Fence.class,"x",int.class);
            VALUE_2 = l.findVarHandle(TestCausality_Fence.class,"y",int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Actor
    public void actor1_setRelease() {
        // 使用 setRelease()，控制单一变量的写入顺序
        //等ARM看看是否存在0,2，存在的话就说明发生重排序导致问题而VarHandle.releaseFence();不会
        VALUE_1.setRelease(this, 1);  // 写入 a = 1
//        x = 1;
//        VarHandle.releaseFence();
        y = 2;                        // 写入 b = 2
    }


    @Actor
    public void actor2(II_Result result){
//        VarHandle.acquireFence();
//        result.r1 = z;
        // 0, 2 是异常的
        result.r2 = (int) VALUE_2.getOpaque(this);  // 读取 b
        VarHandle.acquireFence();
        result.r1 = (int) VALUE_1.getOpaque(this);  // 读取 a


    }

}