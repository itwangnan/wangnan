package leetcode.refine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MonotonicQueue {



    public static void main(String[] args) {

        /**
         *
         * 单调队列
         * https://leetcode.cn/problems/sliding-window-maximum/solutions/2499715/shi-pin-yi-ge-shi-pin-miao-dong-dan-diao-ezj6/
         * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
         *
         * 返回 滑动窗口中的最大值 。
         *
         *
         *
         * 示例 1：
         *
         * 输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
         * 输出：[3,3,5,5,6,7]
         * 解释：
         * 滑动窗口的位置                最大值
         * ---------------               -----
         * [1  3  -1] -3  5  3  6  7       3
         *  1 [3  -1  -3] 5  3  6  7       3
         *  1  3 [-1  -3  5] 3  6  7       5
         *  1  3  -1 [-3  5  3] 6  7       5
         *  1  3  -1  -3 [5  3  6] 7       6
         *  1  3  -1  -3  5 [3  6  7]      7
         * 示例 2：
         *
         * 输入：nums = [1], k = 1
         * 输出：[1]
         *
         */

        /**
         *
         * 首先观察滑动窗口中统计最大值，首先我想到的是记录窗口中元素，如何每次出队入队统计比较最大值
         * 但是有更好的思路，就是单调队列
         * 我们 出队默认出第一个元素，入队会将队列中小于该元素的元素都去掉，达到大 -> 小的队列
         * 每次统计最大值就是拿队首元素
         */

        List<Integer> nums = Arrays.asList(1, 3, -1, -3, 5, 3, 6, 7);
        int k = 3;
        System.out.println(exec(nums,k));

        nums = Arrays.asList(1);
        k = 1;
        System.out.println(exec(nums,k));

        /**
         * 总结：首先如果暴力破解复杂度是n^2 ，
         * 使用单调队列，
         * 时间复杂度O(n):for和while，nums每个元素最多入队一次出队一次，最多2n,所以是O(n)
         * 空间复杂度O(k):flag中最多有k个元素，每次出队入队都会剔除（其实是不重复个数，因为我们会只保留队尾处的数字）
         */

    }

    private static List<Integer> exec(List<Integer> nums, int k) {
        LinkedList<Integer> flagIndex = new LinkedList<>();

        List<Integer> out = new ArrayList<>();

        for (int i = 0; i < nums.size(); i++) {

            Integer num = nums.get(i);

            //入（元素进入队尾，同时维护队列单调性）
            while (!flagIndex.isEmpty() && flagIndex.peekLast() <= num){
                flagIndex.pollLast();
            }
            flagIndex.addLast(i);

            //出（元素离开队首）,满足flag中的index是i - k 有效的出队索引
            if (i - k >= flagIndex.peekFirst() ){
                flagIndex.pollFirst();
            }

            //记录/维护答案（根据队首）
            if (i >= k - 1){
                out.add(nums.get(flagIndex.peekFirst()));
            }

        }
        return out;
    }
}
