package cf;

import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

/**
 * https://codeforces.com/contest/1993/problem/D
 */
public class CF1993D {

    @Test
    public void test(){
        //23,11,33,24,3,4
//        System.out.println(exec2(new int[]{1,2,3,4,5,2,1},5));//1
        System.out.println(exec2(new int[]{11,7,3,4,5,10,1,2},2));//2
//        System.out.println(exec2(new int[]{3,1,9,9,2,10,23,11,33,24,12,4},3));
//        System.out.println(exec2(new int[]{3,9,9,2},3));
//        System.out.println(exec2(new int[]{3,2,5,6,4},3));
//        System.out.println(exec2(new int[]{5,9,2,6,5,4,6},1));
//        System.out.println(exec2(new int[]{7,1,2,6,8,3,4,5},2));
//        System.out.println(exec2(new int[]{3,4,5,6},5));
        /**
         * 3
         * 4
         * 9
         * 6
         * 4
         */
        System.out.println("-----------");
    }

    public int exec(int[] nums,int k){
        return this.filter(nums,k);
    }

    /**
     * 暴力做法，(n-k)!/(k-1)! * klogk
     * @param nums
     * @param k
     * @return
     */
    private int filter(int[] nums, int k) {

        int n = nums.length;
        if (n <= k){
            Arrays.sort(nums);
            return nums[(n-1)/2];
        }

        //否则继续切分

        int max = 0;
        for (int i = 0; i + k <= n; i++) {
            int newN = n - k;
            int[] newNums = new int[newN];

            if (i > 0){
                System.arraycopy(nums, 0, newNums, 0, i);
            }
            if (i + k < n){
                System.arraycopy(nums, i + k, newNums, 0, newN - i);
            }

            int res = filter(newNums, k);
            max = Math.max(max,res);
        }

        return max;
    }


    /**
     *
     * @param nums
     * @param k
     * @return
     */
    private int exec2(int[] nums, int k) {
        return this.end(nums,k);
    }

    /**
     * nlogU , U: Max - Min
     * @param nums
     * @param k
     * @return
     */
    private int end(int[] nums,int k) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;;
        for (int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        //二分从获取最值
        int l = min;
        int r = max;
//        int res = -1;
        while (l <= r){

            int m = l + ((r - l) >>> 1);

            if (check(nums,m,k)){
//                res = m;
                l = m + 1;
            }else {
                r = m - 1;
            }

        }

        return r;
    }

    /**
     * f(n)
     * @param nums
     * @param m
     * @param k
     * @return
     */
    private boolean check(int[] nums, int m,int k) {
        int n = nums.length;
//        int cnt = 0;

        //
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = nums[i] >= m ? 1 : -1;
        }

        String[] strs = new String[n];
        strs[0] = nums[0]+"";
        int[] f = new int[n];
        f[0] = b[0];
        for (int i = 1; i < n; i++) {

            if(i % k == 0){
                //记录组合
                if (f[i - k] > b[i]){
                    //i - k + 1 ... i 删除
                    strs[i] = strs[i - k];
                }else {
                    //前面的都删除
                    strs[i] = nums[i]+"";
                }

                f[i] = Math.max(f[i - k], b[i]);
                //Math.max(Math.max(f[i - k], b[i]),f[i - 1] + b[i])
            }else {
                f[i] = f[i - 1] + b[i];
                strs[i] = strs[i-1] +","+ nums[i];

                if (i / k > 0){
                    //记录组合
                    if (f[i] < f[i - k]){
                        //i - k + 1 ... i 删除
                        strs[i] = strs[i - k];
                    }
                    f[i] = Math.max(f[i],f[i - k]);
                }
            }
        }
        System.out.println(strs[n-1]);
//        if (k >= m){
//
//        }else {
//            cnt = remove(nums, k, m);
//        }

        //大于等于1代表需要往右找
        return f[n - 1] >= 1;
    }

    private void popK(Stack<Integer> stack, int k) {
        for (int i = 0; i < k; i++) {
            stack.pop();
        }
    }


//    private int remove(int[] nums, int k, int m) {
//
//        int[] f = new int[nums.length];
//        f[0] =
//        for (int i = 0; i < nums.length; i++) {
//            if (i == 0){
//                f[i] = nums[i] >= m ? 1 : -1;
//            }else if(i % k == 0){
//                f[i] = Math.max(f[i - k],nums[i] >= m ? 1 : -1);
//            }else {
//                f[i] = f[i - 1] + (nums[i] >= m ? 1 : -1);
//                if (i / k > 0){
//                    f[i] = Math.max(f[i],f[i - k]);
//                }
//            }
//        }
//
//        return f[nums.length - 1];
//    }


}
