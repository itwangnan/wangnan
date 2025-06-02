package jvmtest;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

@JCStressTest
@Outcome(expect = Expect.ACCEPTABLE, desc = "Expected result: a=1, b=2.")
@State
public class ReleaseFenceVsSetReleaseTest {

    private static final VarHandle VALUE_1;

    static {
        try {
            VALUE_1 = MethodHandles.lookup().findVarHandle(ReleaseFenceVsSetReleaseTest.class, "a", int.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    int a = 0;  // 普通 int 变量
    int b = 0;  // 普通 int 变量

    @Actor
    public void actor1_setRelease() {
        // 使用 setRelease()，控制单一变量的写入顺序
        VALUE_1.setRelease(this, 1);  // 写入 a = 1
        b = 2;                        // 写入 b = 2
    }

//    @Actor
//    public void actor2_releaseFence() {
//        // 使用 releaseFence()，全局顺序控制
//        a = 1;                        // 写入 a = 1
//        VarHandle.releaseFence();     // 全局屏障，保证前面的写入不会重排序到后面
//        b = 2;                        // 写入 b = 2
//    }

    @Actor
    public void arbiter(II_Result r) {
        r.r1 = b;  // 读取 b
        r.r2 = (int) VALUE_1.getAcquire(this);  // 读取 a

    }
}

