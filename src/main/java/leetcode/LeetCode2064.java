package leetcode;

import org.junit.Test;

/**
 * https://leetcode.cn/problems/minimized-maximum-of-products-distributed-to-any-store/description/
 */
public class LeetCode2064 {


    @Test
    public void test(){

        System.out.println(minimizedMaximum(6,new int[]{11,6}));
        System.out.println(minimizedMaximum(7,new int[]{15,10,10}));
        System.out.println(minimizedMaximum(1,new int[]{100000}));
        System.out.println(minimizedMaximum(1,new int[]{1}));
        System.out.println(minimizedMaximum(3,new int[]{2,10,6}));

    }

    public int minimizedMaximum(int n, int[] quantities) {

        int max = 0;
        int sum = 0;
        for (int quantity : quantities) {
            max = Math.max(quantity,max);
            sum += quantity;
        }

        int l = sum / n;
        int r = max;


        while (l <= r){

            int m = l + ((r - l) >>> 1);

            if (this.check(quantities,n,m)){
                r = m - 1;
            }else {
                l = m + 1;
            }

        }

        return l;


    }

    private boolean check(int[] quantities, int n, int m) {
        //m为0代表每个数量都为0，这样店铺数就无穷了，返回false
        if (m == 0){
            return false;
        }
        int cnt = 0;
        for (int quantity : quantities) {
//            cnt += (quantity / m);
//            cnt += (quantity % m > 0) ? 1 : 0;
            //加速，可以通过-1 +1达到非整数和整数同一值,quantity>0可以用
            cnt += (quantity - 1) / m + 1;
            if (cnt > n){
                return false;
            }
        }
        return true;
    }


}
