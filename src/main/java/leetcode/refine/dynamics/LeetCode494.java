package leetcode.refine.dynamics;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * https://leetcode.cn/problems/target-sum/description/
 给你一个非负整数数组 nums 和一个整数 target 。

 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：

 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。



 示例 1：

 输入：nums = [1,1,1,1,1], target = 3
 输出：5
 解释：一共有 5 种方法让最终目标和为 3 。
 -1 + 1 + 1 + 1 + 1 = 3
 +1 - 1 + 1 + 1 + 1 = 3
 +1 + 1 - 1 + 1 + 1 = 3
 +1 + 1 + 1 - 1 + 1 = 3
 +1 + 1 + 1 + 1 - 1 = 3
 示例 2：

 输入：nums = [1], target = 1
 输出：1


 提示：

 1 <= nums.length <= 20
 0 <= nums[i] <= 1000
 0 <= sum(nums[i]) <= 1000
 -1000 <= target <= 1000
 */
public class LeetCode494 {



    @Test
    public void test() throws InterruptedException {

        List<Long> list = new ArrayList<>();
        System.out.println(list.isEmpty());
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//
//        for (int i = 0; i < 2; i++) {
//            int finalI = i;
//            executorService.execute(() -> {
//
//                try {
//                    while (!Thread.currentThread().isInterrupted()){
//
//                            Thread.sleep(200);
//
//                        System.out.println("cc"+ finalI);
//                    }
//                } catch (InterruptedException e) {
//                    System.out.println("终止");
//                }
//            });
//        }
//        Thread.sleep(500);
//        executorService.shutdownNow();
//        try {
//            executorService.awaitTermination(30, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//        }
//
//        System.out.println();

//        int[] arr = {2,7,9,3,1};
//
//        System.out.println(this.rob(arr));

    }
    public int findTargetSumWays(int[] nums, int target) {
        return 1;
    }


}
