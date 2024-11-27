package leetcode.refine;

import arr.Arr;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TwoWayPoint {

    public static void main(String[] args) {





//        exec1();
//        exec2();
//        exec3();
//        exec4();
//        exec5();



    }


    private static void exec4() {

        /**
         * 接雨水
         * https://leetcode.cn/problems/trapping-rain-water/solutions/692342/jie-yu-shui-by-leetcode-solution-tuvc/
         * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
         *
         * 首先接雨水中如何统计当前i点可以容多少水，其实是min(left最大高度,right最大高度) - 当前高度
         * 第一种做法，统计左边最大高度，和右边最大高度，在循环减当前高度
         * 空间复杂度O(n),时间复杂度O(n)
         *
         * 第二种做法:双向双指针
         *
         * 空间复杂度O(1),时间复杂度O(n)
         */

        //
        List<Integer> arr = Arrays.asList(0,1,0,2,1,0,1,3,2,1,2,1);

        int left = 0;
        int right = arr.size() - 1;

        int leftMax = 0;
        int rightMax = 0;

        int capacitySum = 0;
        while (left <= right){

            Integer leftNum = arr.get(left);
            Integer rightNum = arr.get(right);

            leftMax = Math.max(leftMax,leftNum);
            rightMax = Math.max(rightMax,rightNum);

            if (leftMax < rightMax){
                capacitySum += (leftMax - leftNum);
                left++;
            }else {
                capacitySum += (rightMax - rightNum);
                right--;
            }

        }

        System.out.println(capacitySum);
    }

    private static void exec3() {

        /**
         * 盛最多水的容器
         * https://leetcode.cn/problems/container-with-most-water/description/
         * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
         *
         * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
         *
         * 返回容器可以储存的最大水量。
         *
         * 说明：你不能倾斜容器。
         */

//        List<Integer> numbers  = Arrays.asList(1,8,6,2,5,4,8,3,7);
        List<Integer> numbers  = Arrays.asList(1,8,6,88,5,88,8,3,7);
        int n = numbers.size();

        int x = 0;
        int y = n - 1;

        int numX = 0;
        int numY = 0;
        int s = 0;
        while (x != y){
            if (numY > numbers.get(y)){
                y--;
                continue;
            }
            if (numX > numbers.get(x)){
                x++;
                continue;
            }

            numX = numbers.get(x);
            numY = numbers.get(y);

            if (numX > numY){
                s = numY * (y - x) > s ? numY * (y - x) : s;
                y--;
            }else  {
                s = numX * (y - x) > s ? numX * (y - x) : s;
                x++;
            }

        }


        System.out.println(s);


    }

    private static void exec1() {
        /**
         * 三数之和
         * https://leetcode.cn/problems/3sum/description/
         *
         * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请
         *
         * 你返回所有和为 0 且不重复的三元组。
         *
         * 注意：答案中不可以包含重复的三元组。
         *
         * 示例 1：
         *
         * 输入：nums = [-1,0,1,2,-1,-4]
         * 输出：[[-1,-1,2],[-1,0,1]]
         * 解释：
         * nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
         * nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
         * nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
         * 不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
         * 注意，输出的顺序和三元组的顺序并不重要。
         */

        /**
         * 双向双指针
         * 首先要保证数组顺序，然后头尾指针i、k，进行判断移动，但是因为这题是三个指针，所以有个j指针要从i+1开始比较
         *
         * 思路:先固定一个，在双向双指针两个
         */
        List<Integer> numbers  = Arrays.asList(-1,0,1,2,-1,-4);
        if (numbers.size() < 3){
            return;
        }
        numbers.sort(Comparator.comparing(Integer::intValue));
        int target = 0;

        int n = numbers.size();
        if (numbers.get(n - 3) + numbers.get(n - 2) + numbers.get(n - 1) < 0){
            return;
        }
        if (numbers.get(0) + numbers.get(1) + numbers.get(2) > 0){
            return;
        }

        for (int i = 0; i < n - 2; i++) {

            int x = i;

            int y = x + 1;

            int z = n - 1;

            //优化1 如果 最前面的3个元素都>target,则后续不可能有等于的机会了
            if (numbers.get(i) + numbers.get(i + 1) + numbers.get(i + 2) > 0){
                break;
            }

            //优化2 如果 num 和后2个元素都<target,则该i可以下循环
            if (numbers.get(i) + numbers.get(n - 1) + numbers.get(n - 2) > 0){
                continue;
            }

            while (y < z){
                int sum = numbers.get(x) + numbers.get(y) + numbers.get(z);

                if (sum < target){

                    y++;

                }else if (sum > target){

                    z--;

                }else {
                    System.out.println(numbers.get(x) + "_" + numbers.get(y) + "_" + numbers.get(z));

                    //比较后面的是否直接和当前相同，如果y相同直接后置
                    y++;
                    while (y < z && numbers.get(y).equals(numbers.get(y-1))){
                        y++;
                    }

                    //比较后面的是否直接和当前相同，如果z相同直接前
                    z--;
                    while (z > y && numbers.get(z).equals(numbers.get(z+1))){
                        z--;
                    }
                }
            }

        }



    }

    private static void exec2() {

        /**
         * 两数之和
         * 给你一个下标从 1 开始的整数数组 numbers ，该数组已按 非递减顺序排列  ，
         * 请你从数组中找出满足相加之和等于目标数 target 的两个数。
         * 如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，则 1 <= index1 < index2 <= numbers.length 。
         *
         * 以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。
         *
         * 你可以假设每个输入 只对应唯一的答案 ，而且你 不可以 重复使用相同的元素
         *
         */
        List<Integer> numbers  = Arrays.asList(-1,0,1,2,-1,-4);
        numbers.sort(Comparator.comparing(Integer::intValue));
        int left = 0;
        int right = numbers.size() - 1;

        int target = 2;
        while (true){

            int s = numbers.get(left) + numbers.get(right);

            if (s < target){

                left++;

            }else if (s > target){

                right--;

            }else {
                break;
            }

        }


        System.out.println(numbers.get(left));
        System.out.println(numbers.get(right));
    }
}
