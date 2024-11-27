package leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * https://leetcode.cn/problems/alternating-groups-ii/description/
 *
 * 给你一个整数数组 colors 和一个整数 k ，colors表示一个由红色和蓝色瓷砖组成的环，第 i 块瓷砖的颜色为 colors[i] ：
 *
 * colors[i] == 0 表示第 i 块瓷砖的颜色是 红色 。
 * colors[i] == 1 表示第 i 块瓷砖的颜色是 蓝色 。
 * 环中连续 k 块瓷砖的颜色如果是 交替 颜色（也就是说除了第一块和最后一块瓷砖以外，中间瓷砖的颜色与它 左边 和 右边 的颜色都不同），那么它被称为一个 交替 组。
 *
 * 请你返回 交替 组的数目。
 *
 * 注意 ，由于 colors 表示一个 环 ，第一块 瓷砖和 最后一块 瓷砖是相邻的。
 *
 * 3 <= colors.length <= 105
 * 0 <= colors[i] <= 1
 * 3 <= k <= colors.length
 */
public class LeetCode3208 {


    @Test
    public void test(){
//        int[] colors = {0,1,0,1,0};
//        int k = 3;

        int[] colors = {0,1,0,0,1,0,1};
        int k = 6;

        System.out.println(numberOfAlternatingGroups(colors, k));


    }

    /**
     * 思路：
     * 1。其实cnt-k+1就是命中个数
     * 2。colors[i % n] 可以直接获取值，不用创建2n数组
     * 3。3 <= k <= colors.length 确保可以用两个数组合并得到结果
     * @param colors
     * @param k
     * @return
     */
    public int numberOfAlternatingGroups2(int[] colors, int k) {
        int n = colors.length;
        int ans = 0;
        int cnt = 0;
        for (int i = 0; i < n * 2; i++) {
            //如果有一样的值将cnt归零
            if (i > 0 && colors[i % n] == colors[(i - 1) % n]) {
                cnt = 0;
            }
            cnt++;
            //因为2n的原因有两片区域，如果不算i>=0要算start<=n，并且cnt >= k时候+1
            if (i >= n && cnt >= k) {
                ans++;
            }
        }
        return ans;
    }

    public int numberOfAlternatingGroups(int[] colors, int k) {


//        int[] arr = new int[colors.length * 2];
//
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = colors[i % colors.length];
//        }


//        int pre = -1;
//        int cnt = 0;
//        int res = 0;

//        int start = 0;
//        int end = 0;
//        while (start < colors.length && end < arr.length){
//
//            if (arr[end] != pre){
//                cnt++;
//                if (cnt == k){
//                    res++;
//                    start++;
//                }
//            }else {
//                cnt = 1;
//                start = end;
//            }
//
//            pre = arr[end];
//
//            end++;
//        }

        int cnt = 0;
        int res = 0;
//        int end = 0;
//        int pre = -1;
//        int start = 0;
//
//
//        while (start < colors.length && end < arr.length){
//
//            while (end < arr.length && arr[end] != pre){
//                cnt++;
//                pre = arr[end];
//                end++;
//            }
//
//            if (cnt >= k){
//                res += (cnt-k+1);
//            }
//
//            start = end;
//            cnt = 1;
//            end++;
//        }

        int n = colors.length;
        for (int i = 0; i < 2 * n; i++) {

            if (i > 0 && colors[(i-1) % n] == colors[i % n]){
                cnt = 0;
            }
            cnt++;

            if (i >= n && cnt >= k){
                res++;
            }

        }


        return res;
    }
}
