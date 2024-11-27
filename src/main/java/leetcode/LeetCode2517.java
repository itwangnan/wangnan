package leetcode;

import org.junit.Test;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/maximum-tastiness-of-candy-basket/description/
 */
public class LeetCode2517 {

    @Test
    public void test(){


        System.out.println(maximumTastiness(new int[]{13,5,1,8,21,2},3));
        System.out.println(maximumTastiness(new int[]{1,3,1},2));
        System.out.println(maximumTastiness(new int[]{7,7,7,7},2));



    }


    /**
     * 思路：
     * 首先返回的是最大 甜蜜度，所以就要甜蜜度二分，l从1开始，而r则是最大差/(k-1)个区域
     *
     * 1.这里判断任意两种糖果价格绝对差的最小值 变成了 排序后，任意两种相邻糖果价格绝对差的最小值
     * 然后通过贪心，将开始到第一个距离d以上的记录并继续，最后看看一共能有几个距离
     * 为什么这样可以？
     * 因为 排好序的元素，中间满足距离则开始到对应节点的距离也满足例如 [2,10]和[1,10] 所以就按顺序记录就行，不满足的一定不存在
     *
     * 2 <= k <= price.length <= 105
     * 1 <= price[i] <= 109
     * @param price
     * @param k
     * @return
     */
    public int maximumTastiness(int[] price, int k) {

        Arrays.sort(price);

        int max_d = price[price.length - 1] - price[0];

        int l = 1;
        //因为是分块所以k-1
        int r = max_d / (k-1);

        while (l <= r){

            int m = l + ((r - l) >>> 1);

            if (this.check(m,price,k)){

                l = m + 1;

            }else {
                r = m - 1;
            }

        }

        return r;
    }

    private boolean check(int m, int[] price, int k) {
        //分块k-1，所以从1开始才到k
        int cnt = 1;
        int x = price[0];

        for (int num : price) {
            if (num - x >= m){
                x = num;
                cnt++;
            }

        }

        return cnt >= k;
    }

}
