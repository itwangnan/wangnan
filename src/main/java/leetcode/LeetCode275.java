package leetcode;

import arr.Arr;
import org.junit.Test;

import java.util.*;


public class LeetCode275 {

    /**
     * 给你一个整数数组 citations ，其中 citations[i] 表示研究者的第 i 篇论文被引用的次数，citations 已经按照 升序排列 。计算并返回该研究者的 h 指数。
     *
     * h 指数的定义：h 代表“高引用次数”（high citations），一名科研人员的 h 指数是指他（她）的 （n 篇论文中）
     * 至少 有 h 篇论文分别被引用了至少 h 次。
     *
     * 请你设计并实现对数时间复杂度的算法解决此问题。
     */
    @Test
    public void test(){

//        int[] citations = new int[]{0,1,3,5,6};
//        int[] citations = new int[]{1,2,1000};
//        int[] citations = new int[]{100}; //1
//        int[] citations = new int[]{0,1,3,5,6}; //0
        int[] citations = new int[]{0,0}; //1
//        int[] citations = new int[]{0,1}; //1
//        int[] citations = new int[]{11,15}; //2
//        System.out.println(hIndex(citations));
        System.out.println(hIndex2(citations));
    }


    /**
     * 思路就是
     * 如果citations[m] >= noMoreNum,移动右端点,说明可能有更大的值在左部分
     * citations[m] < noMoreNum,移动左端点,说明更大的值在右边部分
     *
     * 返回值为什么是citations.length - l，因为最多（至少的值）是由剩余元素决定的
     * [11,15] 如果11符合，后面一定符合因为，单调递增11后面至少12，而下标也至少加1
     * @param citations
     * @return
     */
    public int hIndex(int[] citations) {
        int n = citations.length;
        int l = 0;
        int r = n - 1;


        while (l <= r){
            int m = l + ((r - l) >>> 1);

            //n - m可以的需要的至少数量,citations[m] 引用的次数
            //citations[m] >= n - m 说明m的引用的次数达到至少数量，说明右边都符合，m要去左边找
            if (citations[m] >= n - m){
                r = m - 1;
            }else {
                l = m + 1;
            }
        }

        //n - l 至少数量
        return n - l;
    }

    public int hIndex2(int[] citations) {
        int n = citations.length;
        int l = 1;
        int r = n;
        // n - m ,m在1-n之间，n-m在0-n-1刚好就对应上hIndex了
        while (l <= r){
            int m = l + ((r - l) >>> 1);

            //citations[n - 2] >= 2 证明后面m格数量都至少为m
            //citations[n - 1] >= 1肯定符合，那就找m更大的值
            if (citations[n - m] >= m){
                l = m + 1; //[m+1,right]
            }else {
                r = m - 1; //[left,m-1]
            }
        }


        //同理返回n - m相反的值,l对应r
        return r;
    }
    //citations[n-m] 代表

}
