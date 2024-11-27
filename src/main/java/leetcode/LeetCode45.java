package leetcode;

import org.junit.Test;

/**
 45. 跳跃游戏 II
 中等
 相关标签
 相关企业
 给定一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。

 每个元素 nums[i] 表示从索引 i 向前跳转的最大长度。换句话说，如果你在 nums[i] 处，你可以跳转到任意 nums[i + j] 处:

 0 <= j <= nums[i]
 i + j < n
 返回到达 nums[n - 1] 的最小跳跃次数。生成的测试用例可以到达 nums[n - 1]。
 https://leetcode.cn/problems/jump-game-ii/description/

 贪心走尽可能远
 */
public class LeetCode45 {


    @Test
    public void test(){

//        canJump();
        System.out.println(jump1(new int[]{1,2,1,1,1}));
        System.out.println(jump1(new int[]{2,3,1,1,4}));
        System.out.println(jump1(new int[]{3,2,1,0,4}));
        System.out.println(jump1(new int[]{1,1,1,0}));
        System.out.println(jump1(new int[]{0}));
        System.out.println(jump1(new int[]{1,2,3}));
        System.out.println(jump1(new int[]{2,0,2,0,1}));
        System.out.println(jump1(new int[]{2,3,0,1,4}));
        System.out.println("------------------");
        System.out.println(jump2(new int[]{1,2,1,1,1}));
        System.out.println(jump2(new int[]{2,3,1,1,4}));
        System.out.println(jump2(new int[]{3,2,1,0,4}));
        System.out.println(jump2(new int[]{1,1,1,0}));
        System.out.println(jump2(new int[]{0}));
        System.out.println(jump2(new int[]{1,2,3}));
        System.out.println(jump2(new int[]{2,0,2,0,1}));
        System.out.println(jump2(new int[]{2,3,0,1,4}));

    }
    //        while (r < end){
//            c++;
//            int max = 0;
//            for (int i = l; i <= r; i++) {
//                max = Math.max(i + nums[i],max);
//                if (max >= end){
//                    break;
//                }
//            }
//            l++;
//            r = max;
//            if (l > r){
//                return -1;
//            }
//        }
    public int jump1(int[] nums) {
        int end = 0;
        int r = 0;
        int c = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            r = Math.max(i + nums[i],r);
            if (i == r && nums[i] == 0){
                c = -1;
                break;
            }
//            else if (r >= nums.length - 1){
//                c++;
//                break;
//            }
            else if (i == end){
                c++;
                end = r;
            }

        }

        return c;
    }


    public int jump2(int[] nums) {
        int length = nums.length;
        int end = 0;
        int maxPosition = 0;
        int steps = 0;
        /**
         * O(n) 时间
         * O（1）空间
         * 思路:首先0 ...... length - 2 (length-1是最后一点不用到达之前就可以判定)
         *      贪心:每一步都是最远则最终就是最少步
         *      每一次获取能走到最远的距离end，然后在下次到达end时就可以获取下次最远的距离end，
         *      本质是在每次跳步的过程中获取下一次最远距离，并+1步，在循环
         *
         *      如果相反用贪心的话，因为要找离终点最远的地方就要遍历所有得到end，下次0...end在找
         *      在11111的情况下会导致O(n^2)
         */

        for (int i = 0; i < length - 1; i++) {
            maxPosition = Math.max(maxPosition, i + nums[i]);
            if (i == maxPosition && nums[i] == 0){
                //如果i走到最大值且值为0，证明无法在向前一步
                steps = -1;
                break;
            }else if (i == end) {
                //i == end，说明已经走到end点了要选择新end，并步长+1
                end = maxPosition;
                steps++;
            }
        }
        return steps;
    }
}
