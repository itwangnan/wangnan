package jdk;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.III_Result;
import org.openjdk.jcstress.infra.results.II_Result;

import java.lang.invoke.VarHandle;

@JCStressTest
@State
@Outcome(id = "1, 2, 3",expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(id = "-1, -1, -1",expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(expect = Expect.ACCEPTABLE_INTERESTING, desc = "error")
public class TestFinal {



    public class MyTest{
        int x;
        int y;
        int z;
        public MyTest(int i) {
            this.x = i;
            this.y = x + 1;
            this.z = y + 1;
        }
    }

    MyTest o;

    /**
     * 等arm芯片测试
     */
//    final MyTest o = new MyTest(1);
//    MyTest o = new MyTest(1);

    @Actor
    public void actor1(){
        o = new MyTest(1);
    }

    @Actor
    public void actor2(III_Result result){
        MyTest myTest = this.o;
        if (myTest != null){
            result.r1 = myTest.x;
            result.r2 = myTest.y;
            result.r3 = myTest.z;

        }else {
            result.r1 = -1;
            result.r2 = -1;
            result.r3 = -1;
        }

    }
}
