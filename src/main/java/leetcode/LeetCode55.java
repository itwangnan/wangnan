package leetcode;

import org.junit.Test;

/**
 * 55. 跳跃游戏
 * 中等
 * 相关标签
 * 相关企业
 * 给你一个非负整数数组 nums ，你最初位于数组的 第一个下标 。数组中的每个元素代表你在该位置可以跳跃的最大长度。
 *
 * 判断你是否能够到达最后一个下标，如果可以，返回 true ；否则，返回 false 。
 * https://leetcode.cn/problems/jump-game/description/
 */
public class LeetCode55 {


    @Test
    public void test(){

//        canJump();
        System.out.println(canJump(new int[]{2, 3, 1, 1, 4}));
        System.out.println(canJump(new int[]{3,2,1,0,4}));
        System.out.println(canJump(new int[]{1,1,1,0}));
        System.out.println(canJump(new int[]{0}));

    }
    public boolean canJump(int[] nums) {
        int max = 0;
        int end = nums.length - 1;

        for (int i = 0; i <= max; i++) {
            max = Math.max(i + nums[i],max);
            if (max >= end){
                return true;
            }
        }
        return false;
    }

//    public boolean canJump(int index,int[] nums) {
//
//        //划分dp
//        int startStep = nums[index];
//        if (nums.length == 1){
//            return true;
//        }else if (startStep == 0){
//            return false;
//        }else if (index + startStep >= nums.length - 1){
//            return true;
//        }
//        for (int i = index + 1; i <= index + startStep; i++) {
//
//            if (canJump(i,nums)){
//                return true;
//            }
//
//        }
//
//        return false;
//    }
}
