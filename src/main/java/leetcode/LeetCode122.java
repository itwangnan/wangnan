package leetcode;

import org.junit.Test;

/**
 * 122. 买卖股票的最佳时机 II
 * https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/solution/shi-pin-jiao-ni-yi-bu-bu-si-kao-dong-tai-o3y4/
 */
public class LeetCode122 {

    @Test
    public void test() {
        System.out.println(maxProfit(new int[]{7,1,5,3,6,4}));
        System.out.println(maxProfit(new int[]{1,2,3,4,5}));
        System.out.println(maxProfitDp(new int[]{7,1,5,3,6,4}));
        System.out.println(maxProfitDp(new int[]{1,2,3,4,5}));
        System.out.println(maxProfitDfs(new int[]{7,1,5,3,6,4}));
        System.out.println(maxProfitDfs(new int[]{1,2,3,4,5}));
    }

    int[] prices;
    public int maxProfitDfs(int[] prices) {
        this.prices = prices;
        return dfs(prices.length-1,0);
    }
    public int dfs(int i, int c) {
        if (i < 0) {
            return c == 0 ? 0 : Integer.MIN_VALUE/2;
        }
//        if (c < ws[i]) {
//            //物体容量超过c了只能不选
//            return dfs2(i - 1, c);
//        }
        if (c == 1){
            return Math.max(dfs(i-1,1), dfs(i-1,0) - prices[i]);
        }else {
            return Math.max(dfs(i-1,0), dfs(i-1,1) + prices[i]);
        }
    }

    public int maxProfit(int[] prices) {
        int res = 0;
        if (prices == null || prices.length == 0) {
            return res;
        }
        int n = prices.length;

        int flag = prices[0];

        for (int i = 1; i < n; i++) {
            if (flag < prices[i]) {
                res += (prices[i] - flag);
            }
            flag = prices[i];
        }
        return res;
    }

    public int maxProfitDp(int[] prices) {
        int n = prices.length;
        //dp[i][0] 第i天未持有的最大利润，dp[i][1] 第i天持有的最大利润
//        int[][] dp = new int[n+1][2];
//        dp[0][0] = 0;
//        //第0天不能持有股票，因为不持有肯定更多
//        dp[0][1] = Integer.MIN_VALUE/2;
//        for (int i = 1; i <= n; i++) {
//            for (int j = 0; j < 2; j++) {
//                if (j == 0){
//                    //dp[i-1][1] 第i-1天持有的最大利润 + 卖出prices[i-1] = 第i天未持有的最大利润
//                    dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i-1]);
//                }else {
//                    //dp[i-1][0] 第i-1天未持有的最大利润 + 买入prices[i-1] = 第i天持有的最大利润
//                    dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i-1]);
//                }
//            }
//        }
//
//        return dp[n][0];

        //优化
        int[] dp = new int[2];
        dp[0] = 0;
        //第0天不能持有股票，因为不持有肯定更多
        dp[1] = Integer.MIN_VALUE/2;
        for (int i = 1; i <= n; i++) {
            int d0 = dp[0];
            int d1 = dp[1];
            dp[0] = Math.max(d0, d1 + prices[i-1]);
            dp[1] = Math.max(d1, d0 - prices[i-1]);
        }
        return dp[0];
    }
}
