package test;

import org.junit.Test;

import java.util.Random;

/**
 * 三门问题计算
 */
public class ThreeDoor {


    @Test
    public void test(){
        Random rd = new Random();
        int len = 3;
        int successCount = 0;
        int total = 20000;
//        int all = (1 << 3) - 1;
        for (int i = 0; i < total; i++) {
            //车索引
            int index = rd.nextInt(len);

            int initDoor = 1 << index;

            //选择的门
            int select = rd.nextInt(len);
            int selectDoor = 1 << select;

            //主持人知道
            int flag = ~(initDoor | selectDoor);

            //主持人不知道
//            int flag = ~(selectDoor);
            int error = Integer.numberOfTrailingZeros(flag);

            int select2 = len - select - error;
//            int i1 = Integer.numberOfTrailingZeros((all ^ selectDoor) ^ (1 << error));
            //选择另一个结果
            if (index == select2){
                successCount++;
            }

//            if (select2 != i1){
//                System.out.println();
//            }

        }

        double num =  ((double)successCount / total);
        System.out.println(num);



    }
}
