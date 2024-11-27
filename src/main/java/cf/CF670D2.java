package cf;

import org.junit.Test;

/**
 * https://codeforces.com/problemset/problem/670/D2
 */
public class CF670D2 {

    @Test
    public void test(){

//        System.out.println(exec(1,1000000000,
//                new int[]{1},
//                new int[]{1000000000}
//        ));
//
//        System.out.println(exec(10,1,
//                new int[]{1000000000,1000000000,1000000000,1000000000,1000000000,1000000000,1000000000,1000000000,1000000000,1000000000},
//                new int[]{1,1,1,1,1,1,1,1,1,1}
//        ));

        System.out.println(exec(3,1,
                new int[]{2,1,4},
                new int[]{11,3,16}
                ));
//        System.out.println(exec(4,3,
//                new int[]{4,3,5,6},
//                new int[]{11,12,14,20}
//        ));

    }

    public int exec(int n,int k,int[] needs,int[] haves){
        //k克魔法粉末
        //n个原材料
        int max = 0;
        for (int i = 0; i < n; i++) {

            int num =  (haves[i] + k) / needs[i];
            max = Math.max(num,max);
        }




        int l = 0;
        int r = max;
        while (l <= r){

            //时间
            int mid = l + ((r - l) >>> 1);

            if (this.get(mid,k,needs,haves)){
                //右边未知，左边符合
                l = mid + 1;

            }else {

                r = mid - 1;
            }

        }

        //因为找最大值符合flag的都在右边部分会往右找，最大值right，而最小值是往左边找，这样最小值就是left
        //当右部分是符合的时候最后r，左边的时候l,就看l==r时候不符合动的是那个指针就行,因为不符合动的就是最后答案
        return r;
    }

    private boolean get(int mid, int k, int[] needs, int[] haves) {


        for (int i = 0; i < needs.length; i++) {

            int sum = mid * needs[i];

            if (haves[i] - sum < 0){
                //不够
                k += (haves[i] - sum);
                if (k < 0){
                    return false;
                }
            }

        }



        return true;
    }

}
