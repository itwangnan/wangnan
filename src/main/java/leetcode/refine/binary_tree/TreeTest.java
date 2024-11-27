package leetcode.refine.binary_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TreeTest {

    private List<List<Integer>> res = new ArrayList<>();
    private final List<Integer> path = new ArrayList<>();

    @Test
    public void leetCode78() {

        this.subset3(new int[]{1,2,3},0);

        for (List<Integer> l : res) {
            System.out.println(l);
        }


    }

    public List<List<Integer>> subsets(int[] nums) {

        res.add(Collections.emptyList());
        this.subset(nums,0);

        return res;
    }

    /**
     * 自己写的，思路是在之前的结果上，每个加入新的元素
     */
    private void subset(int[] nums,int level) {
        if (nums.length == level){
            return;
        }

        int size = res.size();

        Integer newInt = nums[level];

        for (int i = 0; i < size; i++) {
            List<Integer> item = res.get(i);

            List<Integer> newItem = new ArrayList<>(item);
            newItem.add(newInt);
            res.add(newItem);
        }

        subset(nums,level + 1);
    }

    /**
     * 输入的视角，可以每次选和不选两种结果
     */
    private void subset2(int[] nums,int level) {

        if (level == nums.length) {
            res.add(new ArrayList<>(path)); // 固定答案
            return;
        }
        // 不选 nums[i]
        subset2(nums,level + 1);
        // 选 nums[i]
        path.add(nums[level]);
        subset2(nums,level + 1);

        //其实就是删除上面path.add(nums[level]);的元素 因为没增加一次下一次会累加
        path.remove(path.size() - 1); // 恢复现场
    }


    /**
     * 答案的视角（选哪个数）,选择大于当前索引的值（避免重复）
     */
    private void subset3(int[] nums,int level) {
        res.add(new ArrayList<>(path));

        for (int i = level; i < nums.length; i++) {
            // 不选 nums[i]
            path.add(nums[i]);

            subset3(nums,i + 1);


            path.remove(path.size() - 1); // 恢复现场
        }

    }


    private List<List<String>> strRes = new ArrayList<>();
    private List<List<String>> strPaths = new ArrayList<>();
    private final List<String> strPath = new ArrayList<>();


    @Test
    public void leetCode131() {

        //https://leetcode.cn/problems/palindrome-partitioning/description/
        String aab = "aab";
        List<List<String>> partition = this.partition(aab);

        for (List<String> l : partition) {
            System.out.println(l);
        }

    }

    public List<List<String>> partition(String s) {


//        strSubset2(s,0,0);
//        strSubset3(s,0);

        strRes.add(Collections.singletonList(s));
        strSubset1(s,1);

        return strRes;
    }

    /**
     * 自己写的，思路是在之前的结果上，每个加入新的元素
     */
    private void strSubset1(String s,int level) {
        if (s.length() == level){
            return;
        }

        int size = strRes.size();


        for (int i = 0; i < size; i++) {
            List<String> item = strRes.get(i);

            List<String> list = new ArrayList<>();

            int count = 0;

            for (String s1 : item) {
                count += s1.length();


                if (count >= level){
                    String c1 = s1.substring(0, s1.length() - (count - level));
                    String c2 = s1.substring(s1.length() - (count - level));
                    list.add(c1);
                    list.add(c2);
                }else {
                    list.add(s1);
                }

            }

            strRes.add(list);
            List<String> itemList = list.stream().filter(this::palindrome).collect(Collectors.toList());
            strPaths.add(itemList);
        }

        strSubset1(s,level + 1);
    }

    /**
     * 输入的视角，可以每次选和不选两种结果
     */
    private void strSubset2(String s,int start,int level) {

        if (level == s.length()) {
            strRes.add(new ArrayList<>(strPath)); // 固定答案
            return;
        }

        //不选 i 和 i+1 之间 level == n-1 一定要选了，因为结束了
        if (level < s.length() - 1){
            strSubset2(s,start,level + 1);
        }

        String substring = s.substring(start, level + 1);
        if (palindrome(substring)){
            //选择
            strPath.add(substring);

            //因为上面是start - level + 1,因为是结束节点，而下一次要从level+1开始，因为[0,2),[2,3)
            strSubset2(s,level+1,level + 1);

            //其实就是删除上面path.add(nums[level]);的元素 因为没增加一次下一次会累加
            strPath.remove(strPath.size() - 1); // 恢复现场
        }

    }

    private boolean palindrome(String str){
        int left = 0;
        int right = str.length() - 1;
        while (left <= right){

            if (str.charAt(left) != str.charAt(right)){
                return false;
            }


            left++;
            right--;
        }
        return true;
    }


    /**
     * 答案的视角（选哪个数）,枚举子串结束位置
     */
    private void strSubset3(String s,int level) {

        if (level == s.length()) {
            strRes.add(new ArrayList<>(strPath));
            return;
        }

        for (int i = level; i < s.toCharArray().length; i++) {

            String substring = s.substring(level, i + 1);
            if (palindrome(substring)){
                // 不选 nums[i]
                strPath.add(substring);

                strSubset3(s,i + 1);

                strPath.remove(strPath.size() - 1); // 恢复现场
            }
        }

    }
}
