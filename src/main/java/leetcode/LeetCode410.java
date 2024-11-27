package leetcode;

import org.junit.Test;

/**
 * https://leetcode.cn/problems/split-array-largest-sum/description/
 */
public class LeetCode410 {

    @Test
    public void test(){


        System.out.println(splitArray(new int[]{7,2,5,10,8},2));
//        System.out.println(splitArray(new int[]{1,2,3,4,5},2));
        System.out.println(splitArray(new int[]{1,4,4},4));



    }


    /**
     * 思路:
     * 1.首先求 k 个子数组各自和的最大值最小，那返回的就是最小值（和）。
     * 所以要对这个结果二分，二分范围[Math.max(sum/k,max),sum]
     *
     * 1 <= k <= min(50, nums.length)
     * 2.判断check，因为最多k段，因为如果k-1段可以找随便一个2个以上的拆分
     *
     * 问题splitArray(new int[]{7,2,5,10,8},2)为什么不是19，因为19符合会去找更小的，而更小的就一定会到 <=的=就是18了
     * @param nums
     * @param k
     * @return
     */
    public int splitArray(int[] nums, int k) {

        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max,num);
        }

        int l = Math.max(sum/k,max);
        int r = sum;

        while (l <= r){

            int m = l + ((r - l) >>> 1);


            if (this.check(m,nums,k)){

                r = m - 1;

            }else {

                l = m + 1;

            }

        }

        return l;
    }

    private boolean check(int m, int[] nums, int k) {

        int cnt = 1;
        int s = 0;

        for (int num : nums) {
            if (s + num <= m){
                s += num;
            }else {
                if (cnt == k) {
                    return false;
                }
                cnt++;
                s = num;
            }

        }

        return true;
    }

}
