package leetcode.refine.binary_tree;

import org.junit.Test;

//https://leetcode.cn/problems/subarray-product-less-than-k/description/
//713. 乘积小于 K 的子数组
public class LeetCode713 {

    @Test
    public void test(){
//        int[] nums = {10,9,10,4,3,8,3,3,6,2,10,10,9,3};
//        int k = 19;

        int[] nums = {57,44,92,28,66,60,37,33,52,38,29,76,8,75,22};
        int k = 0;
        //  给你一个整数数组 nums 和一个整数 k ，请你返回子数组内所有元素的乘积严格小于 k 的连续子数组的数目。
        System.out.println(this.numSubarrayProductLessThanK(nums,k));
    }


    /**
     * 思考
     * nums=57,44,92,28,66,60,37,33,52,38,29,76,8,75,22 k=18
     * nums=10,9,10,4,3,8,3,3,6,2,10,10,9,3 k=19
     * 要先右端点向右一直乘直到不满足为止,会发现left-right+1就是子数组数（left开头直到right结尾的子数组个数)
     * 如果超过了，就将左边的右移动
     *
     */
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int res = 0;
        int left = 0;
        int flag = 1;
        //ans += right - left + 1

        for (int right = 0; right < nums.length; right++) {

            flag *= nums[right];

            while (left <= right && flag >= k){
                flag /= nums[left];
                left++;
            }

            //flag < k可以省去，因为考虑flag >= k,则left == right + 1，说明当前right元素不满足要求刚好 right-right-1+1=0
//            if (flag < k){
//                res += (right - left + 1);
//            }
            res += (right - left + 1);
        }

        return res;
    }


    /**
     * 首先因为nums中的元素都是>=1的，那就直接判断k<=1就返回0
     * 这样left > right的情况就不存在了，因为flag >= k，当left == right时，flag = 1,而k<=1已经返回*
     */
    public int numSubarrayProductLessThanK2(int[] nums, int k) {
        if (k <= 1) {
            return 0;
        }
        int ans = 0;
        int prod = 1;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            prod *= nums[right];
            while (prod >= k) { // 不满足要求
                prod /= nums[left++];
            }
            ans += right - left + 1;
        }
        return ans;
    }
}
