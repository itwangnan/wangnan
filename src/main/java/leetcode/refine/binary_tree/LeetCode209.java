package leetcode.refine.binary_tree;

import org.junit.Test;

//https://leetcode.cn/problems/minimum-size-subarray-sum/description/
//209. 长度最小的子数组
public class LeetCode209 {

    @Test
    public void test(){

        int[] arr = {2,3,2,1,4,3};
        int target = 7;


        int len = this.exec2(arr,target);
        System.out.println(len);
    }



    private int exec(int[] nums, int target) {

        int res = 0;
        int sum = 0;

        int right = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (sum >= target){
                right = i;
                res = i + 1;
                break;
            }
        }

        for (int left = 0; left < nums.length; left++) {

            sum -= nums[left];

            int flag = right - left;
            if (sum >= target){
                res = flag < res ? flag : res;
            } else {
                right++;
                if (right < nums.length){
                    sum += nums[right];
                }
            }
        }


        return res;
    }


    /**
     *
     * 思路:想这种左右点移动的，不管是同向还是双向，要考虑边界问题
     * 比如这题: 先sum达到大于等于target,这时左端点向右移动减少sum值，而达到小于时，右端点在增加，直到尾端点
     * 注意这时左端点向右移动减少sum值，是while直接找到，不需要for每次判断
     * 另外可以注意
     *
     *             这种预先判断，在减少思路是常用
     *             while (sum - nums[left] >= target){
     *                 sum -= nums[left];
     *                 left++;
     *             }
     */
    private int exec2(int[] nums, int target) {

        int res = Integer.MAX_VALUE;
        int sum = 0;
        int left = 0;

        for (int right = 0; right < nums.length; right++) {

            //右端点增加
            sum += nums[right];

            //如果左端点减少还>= target，则可以left移动（这样长度更短）
            //#left <= right不用判断，因为题目中target是正数left == right时sum - nums[left] == 0
            while (sum - nums[left] >= target){
                sum -= nums[left];
                left++;
            }
            //这个判断是 右端点增加过程不需要判断
            if (sum >= target){
                res = Math.min(res,right - left + 1);
            }


//            while (sum >= target){
//                res = Math.min(res,right - left + 1);
//                //sum - nums[left] >= target 隐含
//                sum -= nums[left];
//                left++;
//            }


        }


        return res;
    }
}
