package leetcode.refine.dynamics;

import org.apache.commons.math3.analysis.function.Max;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/house-robber/description/
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 *
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
 *
 *
 * 示例 1：
 *
 * 输入：[1,2,3,1]
 * 输出：4
 * 解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
 *      偷窃到的最高金额 = 1 + 3 = 4 。
 * 示例 2：
 *
 * 输入：[2,7,9,3,1]
 * 输出：12
 * 解释：偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
 *      偷窃到的最高金额 = 2 + 9 + 1 = 12 。
 */
public class LeetCode198 {



    @Test
    public void test(){

        int[] arr = {2,7,9,3,1};

        System.out.println(this.rob(arr));

    }

    public int rob(int[] nums) {
        int n = nums.length;

        int i_0 = 0;
        int i_1 = 0;
        for (int i = 0; i < n; i++) {
            int max = Math.max(nums[i] + i_0, i_1);

            i_0 = i_1;
            i_1 = max;
        }
        return i_1;
    }


    public int rob(int[] nums,int i,int[] flag) {
        if (i < 0){
            return 0;
        }
        int i2 = i - 2 >= 0 && flag[i - 2] >= 0 ? flag[i - 2] : rob(nums, i - 2, flag);
        int i1 = i - 1 >= 0 && flag[i - 1] >= 0 ? flag[i - 1] : rob(nums, i - 1, flag);
        return Math.max(nums[i] + i2, i1);
    }



}
