package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 46. 全排列
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 * https://leetcode.cn/problems/permutations/description/
 */
public class LeetCode46 {


    @Test
    public void test() {
        int[] nums = {1,2,3};
        for (List<Integer> list : permute(nums)) {
            System.out.println(list);
        }
    }

    boolean[] flags;

    int[] nums;

    List<List<Integer>> res;

    public List<List<Integer>> permute(int[] nums) {

        this.flags = new boolean[nums.length];
        this.nums = nums;
        this.res = new ArrayList<>();
        this.exec(new ArrayList<>());
        return res;
    }

    private void exec(List<Integer> list) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        //选哪个
        for (int i = 0; i < nums.length; i++) {
            if (flags[i]){
                continue;
            }
            list.add(nums[i]);
            flags[i] = true;
            exec(list);
            flags[i] = false;
            list.remove(list.size() - 1);
        }
    }
}
