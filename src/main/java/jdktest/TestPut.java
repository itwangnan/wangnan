package jdktest;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;
import sun.misc.Unsafe;

import java.lang.invoke.VarHandle;
import java.util.concurrent.CountDownLatch;

@JCStressTest
@State
@Outcome(id = "0, 1", expect = Expect.ACCEPTABLE, desc = "y was initially 0, then updated to 1.")
@Outcome(id = "1, 1", expect = Expect.ACCEPTABLE, desc = "y was visible immediately.")
public class TestPut {

    static final Unsafe UNSAFE = UnsafeAccess.getUnsafe();
    static final long Y_OFFSET;

    volatile int y;
    private static final CountDownLatch latch = new CountDownLatch(1);

    static {
        try {
            Y_OFFSET = UNSAFE.objectFieldOffset(TestPut.class.getDeclaredField("y"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Actor1: 尝试读取 y 的值
    @Actor
    public void actor1(II_Result result) {
        VarHandle.releaseFence();
        VarHandle.acquireFence();
        try {
            latch.await();  // 等待 actor2 执行
        } catch (InterruptedException ignored) {}
        VarHandle.releaseFence();
        VarHandle.acquireFence();
        result.r1 = y;  // 第一次读取 y，可能是 0
        VarHandle.loadLoadFence();
        result.r2 = y;  // 第二次读取 y，应该是 1
    }

    // Actor2: 写入 y 并释放 latch
    @Actor
    public void actor2() {
        UNSAFE.putOrderedInt(this, Y_OFFSET, 1);  // 延迟写入 y = 1
        VarHandle.releaseFence();
        VarHandle.acquireFence();
        latch.countDown();

    }
}
