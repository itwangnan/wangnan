package leetcode;

import org.junit.Test;

import java.util.Arrays;

/**
 * 买卖股票的最佳时机 IV
 * https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/solution/shi-pin-jiao-ni-yi-bu-bu-si-kao-dong-tai-kksg/
 */
public class LeetCode188 {

    @Test
    public void test() {
        System.out.println(maxProfitLeast(2,new int[]{2,4,1}));
        System.out.println(maxProfitLeast(2,new int[]{3,2,6,5,0,3}));
//        System.out.println(maxProfitLeast(2,new int[]{1}));
    }

    int[] prices;
    public int maxProfitDfs(int k,int[] prices) {
        this.prices = prices;
        return dfs(prices.length-1,0,k);
    }
    private int dfs(int i, int c,int k) {
        if (k < 0) {
            // 除 2 防止溢出
            return Integer.MIN_VALUE / 2;
        }
        if (i < 0) {
            return c == 0 ? 0 : Integer.MIN_VALUE/2;
        }
//        if (k <= 0) {
//            //物体容量超过c了只能不选
//            return c == 1 ? dfs(i-1,1,k) : dfs(i-1,0,k);
//        }
        if (c == 1){
            return Math.max(dfs(i-1,1,k), dfs(i-1,0,k-1) - prices[i]);
        }else {
            return Math.max(dfs(i-1,0,k), dfs(i-1,1,k) + prices[i]);
        }
    }

    private int dfsExactly(int i, int c,int k) {
        if (k < 0) {
            // 除 2 防止溢出
            return Integer.MIN_VALUE / 2;
        }
        if (i < 0) {
            return c == 0 && k == 0 ? 0 : Integer.MIN_VALUE/2;
        }
//        if (k <= 0) {
//            //物体容量超过c了只能不选
//            return c == 1 ? dfs(i-1,1,k) : dfs(i-1,0,k);
//        }
        if (c == 1){
            return Math.max(dfsExactly(i-1,1,k), dfsExactly(i-1,0,k-1) - prices[i]);
        }else {
            return Math.max(dfsExactly(i-1,0,k), dfsExactly(i-1,1,k) + prices[i]);
        }
    }

    public int maxProfit(int k,int[] prices) {
        int n = prices.length;
        /**
         * 注意 0 ... n-1 我们移动到了 1 ... n
         * 而k是 0 ... k 因为剩余次数一开始就是k，没有就是0，所以移动后就是 1 ... k+1
         */
        int[][][] dp = new int[n+1][k+2][2];

        /**
         * 首先
         * 1.dp[*][0][*] = Integer.MIN_VALUE 因为0就是-1的k，按dfs可以看到是Integer.MIN_VALUE
         * 2.dp[0][*][0] = 0
         * 3.dp[0][*][1] = Integer.MIN_VALUE
         * 2和3是i < 0原本的判断
         */
        for (int[][] mat : dp) {
            for (int[] row : mat) {
                Arrays.fill(row, Integer.MIN_VALUE / 2); // 防止溢出
            }
        }
        for (int j = 1; j <= k; j++) {
            dp[0][j][0] = 0;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k+1; j++) {
                dp[i][j][0] = Math.max(dp[i-1][j][0], dp[i-1][j][1] + prices[i-1]);
                dp[i][j][1] = Math.max(dp[i-1][j][1], dp[i-1][j-1][0] - prices[i-1]);
            }

        }

        return dp[n][k+1][0];
    }


    public int maxProfit2(int k,int[] prices) {
        int n = prices.length;
        /**
         * 注意 0 ... n-1 我们移动到了 1 ... n
         * 而k是 0 ... k 因为剩余次数一开始就是k，没有就是0，所以移动后就是 1 ... k+1
         */
        int[][] dp = new int[k+2][2];

        /**
         * 首先
         * 1.dp[*][0][*] = Integer.MIN_VALUE 因为0就是-1的k，按dfs可以看到是Integer.MIN_VALUE
         * 2.dp[0][*][0] = 0
         * 3.dp[0][*][1] = Integer.MIN_VALUE
         * 2和3是i < 0原本的判断
         */
        for (int[] arr : dp) {
            Arrays.fill(arr, Integer.MIN_VALUE / 2);
        }
        for (int j = 1; j <= k + 1; j++) {
            dp[j][0] = 0;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = k+1; j > 0; j--) {
                dp[j][0] = Math.max(dp[j][0], dp[j][1] + prices[i-1]);
                dp[j][1] = Math.max(dp[j][1], dp[j-1][0] - prices[i-1]);
            }

        }

        return dp[k + 1][0];
    }

    public int maxProfitExactly(int k,int[] prices) {
        int n = prices.length;

        int[][][] dp = new int[n+1][k+2][2];

        for (int[][] mat : dp) {
            for (int[] row : mat) {
                Arrays.fill(row, Integer.MIN_VALUE / 2); // 防止溢出
            }
        }

        //只有k == 0 && c == 0 才返回0
        dp[0][1][0] = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k+1; j++) {
                dp[i][j][0] = Math.max(dp[i-1][j][0], dp[i-1][j][1] + prices[i-1]);
                dp[i][j][1] = Math.max(dp[i-1][j][1], dp[i-1][j-1][0] - prices[i-1]);
            }

        }

        return dp[n][k+1][0];
    }

    private int dfsLeast(int i, int c,int k) {
//        if (k < 0) {
//            // 除 2 防止溢出
//            return Integer.MIN_VALUE / 2;
//        }
        if (i < 0) {
            return c == 0 && k <= 0 ? 0 : Integer.MIN_VALUE/2;
        }
//        if (k <= 0) {
//            //物体容量超过c了只能不选
//            return c == 1 ? dfs(i-1,1,k) : dfs(i-1,0,k);
//        }
        if (c == 1){
            return Math.max(dfsLeast(i-1,1,k), dfsLeast(i-1,0,k-1) - prices[i]);
        }else {
            return Math.max(dfsLeast(i-1,0,k), dfsLeast(i-1,1,k) + prices[i]);
        }
    }

    public int maxProfitLeast(int k,int[] prices) {
        int n = prices.length;

        int[][][] dp = new int[n+1][k+2][2];

        for (int[][] mat : dp) {
            for (int[] row : mat) {
                Arrays.fill(row, Integer.MIN_VALUE / 2); // 防止溢出
            }
        }

        //c == 0 && k <= 0 ? 0 : Integer.MIN_VALUE/2
        dp[0][1][0] = 0;
        dp[0][0][0] = 0;
//        for (int j = 1; j <= k + 1; j++) {
//            dp[0][j][0] = 0;
//        }

        for (int i = 1; i <= n; i++) {
            //0 也要计算在dp中有记载，因为k至少-1和0 代表没限制 所以只要初始化-1就行，相当于
            dp[i][0][0] = Math.max(dp[i-1][0][0], dp[i-1][0][1] + prices[i-1]);
            dp[i][0][1] = Math.max(dp[i-1][0][1], dp[i-1][0][0] - prices[i-1]);
            for (int j = 1; j <= k+1; j++) {
                dp[i][j][0] = Math.max(dp[i-1][j][0], dp[i-1][j][1] + prices[i-1]);
                dp[i][j][1] = Math.max(dp[i-1][j][1], dp[i-1][j-1][0] - prices[i-1]);
            }

        }

        return Math.max(dp[n][k + 1][0], 0);
    }
}
