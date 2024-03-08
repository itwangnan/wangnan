package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode2552 {

    /**
     * 给你一个长度为 n 下标从 0 开始的整数数组 nums ，它包含 1 到 n 的所有数字，请你返回上升四元组的数目。
     *
     * 如果一个四元组 (i, j, k, l) 满足以下条件，我们称它是上升的：
     *
     * 0 <= i < j < k < l < n 且
     * nums[i] < nums[k] < nums[j] < nums[l] 。
     */
    public static void main(String[] args) {

        int[] nums = new int[]{1,3,4,2,5};
//        int[] nums = new int[]{1,4,2,5,3};

//        int count = 0;
//
//        int[][] great = new int[nums.length][nums.length];
//        int[][] less = new int[nums.length][nums.length];
//
//        for (int k = 0; k < nums.length - 1; k++) {
//
//            for (int x = 0; x < nums[k + 1]; x++) {
//
//                great[k][x]++;
//            }
//
//        }
//
//        for (int k = 0; k < nums.length - 1; k++) {
//
//            for (int x = 0; x < nums[k + 1]; x++) {
//
//                great[k][x]++;
//            }
//
//        }

//        System.out.println(Arrays.toString(great));


        System.out.println(countQuadruplets1(nums));
        System.out.println(countQuadruplets2(nums));



    }

    public static long countQuadruplets(int[] nums) {
        int n = nums.length;
        int[][] great = new int[n][n + 1];
        for (int k = n - 2; k > 0; k--) {
            great[k] = great[k+1].clone();
            for (int x = nums[k + 1] - 1; x > 0; x--) {
                great[k][x]++; // x < nums[k+1]
            }
        }

//        for (int j = 1; j < n - 2; j++) {
//            less[j] = less[j-1].clone();
//            for (int x = nums[j-1] + 1; x <= n; x++) {
//                less[j][x]++; // x > nums[j-1]
//            }
//        }
//
//
//        int count = 0;
//        for (int j = 1; j < n - 2; j++) {
//            for (int k = j + 1; k < n - 1; k++) {
//                if (nums[j] >= nums[k] ){
//                    continue;
//                }
//                count += (great[k][nums[j]] * less[j][nums[k]]);
//            }
//        }

        long count = 0;
//        int[] less = new int[n + 1];

        for (int j = 1; j < n; j++) {
//            for (int x = nums[j - 1] + 1; x <= n; x++) {
//                less[x]++; // x > nums[j-1]
//            }
            for (int k = j + 1; k < n - 1; k++) {
                if (nums[j] > nums[k] ){
                    int rLess = n - j - 1 - great[j][nums[k]];
                    int lLess = nums[k] - rLess;
                    count += (great[k][nums[j]] * lLess);
                }
            }
        }


        return count;
    }





    public static long countQuadruplets1(int[] nums) {
        int n = nums.length;
        int[][] great = new int[n][n + 1];
        for (int k = n - 2; k >= 2; k--) {
            great[k] = great[k + 1].clone();
            for (int x = nums[k + 1] - 1; x > 0; x--)
                great[k][x]++; // x < nums[k+1] ，对于 x，大于它的数的个数 +1
        }

        int ans = 0;
        int[] less = new int[n + 1];
        for (int j = 1; j < n - 2; j++) {
            for (int x = nums[j - 1] + 1; x <= n; x++)
                less[x]++; // x > nums[j-1]，对于 x，小于它的数的个数 +1
            for (int k = j + 1; k < n - 1; k++)
                if (nums[j] > nums[k])
                    ans += (less[nums[k]] * great[k][nums[j]]);
        }
        return ans;
    }


    public static int countQuadruplets2(int[] nums) {
        int n = nums.length;
        int[][] great = new int[n][n + 1];
        for (int k = n - 2; k > 0; k--) {
            great[k] = great[k + 1].clone();
            for (int x = nums[k + 1] - 1; x > 0; x--)
                great[k][x]++;
        }

        int count = 0;
        for (int j = 1; j < n - 1; j++)
            for (int k = j + 1; k < n - 1; k++) {
                if (nums[j] > nums[k]){
                    //(n总数 - j索引 - j自己) = j右边个数 - j右边大于nums[k]的个数 = 右边小于等于nums[k]的个数
                    int rLess = n - j - 1 - great[j][nums[k]];//这里减1是将j去掉因为只比较右边
                    //nums[k]代表1-nums[k]也是总数 - 右边小于等于nums[k]的个数 = 右边大于等于nums[k]的个数
                    int lLess = nums[k]  - rLess;             //rLess中包括了nums[k]本身，所以-rLess就是左边小于的
                    count += (great[k][nums[j]] * lLess);
                }
            }
        return count;
    }
}
