package leetcode.refine.binary_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankTest {

    private List<List<Integer>> res = new ArrayList<>();
    private List<Integer> path = new ArrayList<>();
    private boolean[] flag;
    @Test
    public void leetCode46(){
        //全排列
        //https://leetcode.cn/problems/permutations/description/

        int[] arr = {1, 2, 3};
        flag = new boolean[arr.length];
        permute(arr);

        for (List<Integer> re : res) {
            System.out.println(re);
        }
    }

    public void permute(int[] nums) {

        if (path.size() == nums.length){
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (!flag[i]){
                int num = nums[i];
                path.add(num);
                flag[i] = true;
                permute(nums);
                flag[i] = false;
                path.remove(path.size() - 1);
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        int[] newArray = new int[nums.length - 1];
        int i = 1;
        System.arraycopy(nums, 0, newArray, 0, i);
        System.arraycopy(nums, i+1, newArray, i, nums.length - i - 1);

        for (int num : newArray) {
            System.out.println(num);
        }
    }

}
