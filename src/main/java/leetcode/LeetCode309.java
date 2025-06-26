package leetcode;

import org.junit.Test;

/**
 * 309. 最佳买卖股票时机含冷冻期
 * https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/solution/shi-pin-jiao-ni-yi-bu-bu-si-kao-dong-tai-0k0l/
 */
public class LeetCode309 {

    @Test
    public void test() {
        System.out.println(maxProfit(new int[]{7,1,5,3,6,4}));
        System.out.println(maxProfit(new int[]{1,2,3,4,5}));
    }

    public int maxProfit(int[] prices) {
        int n = prices.length;
//        dp[i][0] 第i天未持有的最大利润，dp[i][1] 第i天持有的最大利润
        int[][] dp = new int[n+2][2];
        dp[0][0] = 0;
        //第0天不能持有股票，因为不持有肯定更多
        dp[0][1] = Integer.MIN_VALUE/2;
        for (int i = 1; i <= n; i++) {
            //dp[i-1][1] 第i-1天持有的最大利润 + 卖出prices[i-1] = 第i天未持有的最大利润
            dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i-1]);
            //dp[i-2][0] 第i-2天未持有的最大利润 冻结1天 + 买入prices[i-1] = 第i天持有的最大利润
            dp[i][1] = Math.max(dp[i-1][1], dp[Math.max(i-2,0)][0] - prices[i-1]);
        }

        return dp[n][0];
    }

    public int maxProfit2(int[] prices) {
        int n = prices.length;
//        dp[i][0] 第i天未持有的最大利润，dp[i][1] 第i天持有的最大利润
        int[][] dp = new int[n+2][2];
        //第0天不能持有股票，因为不持有肯定更多
        dp[1][1] = Integer.MIN_VALUE/2;
        for (int i = 0; i < n; i++) {
            //dp[i-1][1] 第i-1天持有的最大利润 + 卖出prices[i-1] = 第i天未持有的最大利润
            dp[i+2][0] = Math.max(dp[i+1][0], dp[i+1][1] + prices[i]);
            //dp[i-2][0] 第i-2天未持有的最大利润 冻结1天 + 买入prices[i-1] = 第i天持有的最大利润
            dp[i+2][1] = Math.max(dp[i+1][1], dp[i][0] - prices[i]);
        }

        return dp[n+1][0];
    }
}
