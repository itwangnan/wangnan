package leetcode.refine;

import org.junit.Test;

import java.util.Arrays;

/**
 * 动态规划思想
 */
public class DP {


    @Test
    public void test() {
//        int[] arr = {1,1,1,1,1};
//        System.out.println(findTargetSumWaysEqs(arr,3));
//        // <= 3 ，-5 -4 -3 -2 -1 0 1 2 3  = 31
//        System.out.println(findTargetSumWaysMosts(arr,3));
//        // >= 3 , 3 4 5 = 6
//        System.out.println(findTargetSumWaysLeasts(arr,3));

//        int[] ws = {2, 1, 3};
//        int[] vs = {4, 2, 3};
//        int[] num = {1, 1, 1};
//        System.out.println(zeroOnePack(3,ws,vs,4));
//        System.out.println(zeroOnePackLeast(3,ws,vs,4));
//        System.out.println(zeroOnePackExactly(3,ws,vs,4));
//        System.out.println(zeroOnePackMost(3,ws,vs,4));
        int[] ws = {3, 2};
        int[] vs = {4, 2};

        System.out.println(zeroOnePack(2,ws,vs,3));
        System.out.println(zeroOnePackLeast(2,ws,vs,3));
        System.out.println(zeroOnePackExactly(2,ws,vs,3));
        System.out.println(zeroOnePackMost(2,ws,vs,3));

//        System.out.println(zeroOnePackFor(3, ws, vs, 0));
//        System.out.println(zeroOnePackFor(3, ws, vs, 1));
//        System.out.println(zeroOnePackFor(3, ws, vs, 2));
//        System.out.println(zeroOnePackFor(3, ws, vs, 3));
//        System.out.println(zeroOnePackFor(3, ws, vs, 4));

//        System.out.println(zeroOnePackMost(3, ws, vs, 4)); // 18

//        System.out.println(zeroOnePackLeast(3, ws, vs, 4));
//        System.out.println(zeroOnePackLeast(3, ws, vs, 4));
//        System.out.println(completelyPack(3, ws, vs, 4));
//        System.out.println(manyPack(3, ws, vs, 4,num));
    }

    int[] ws;
    int[] vs;

    /**
     * capacity 背包容量,n 物品种类，ws 物品容量，vs 物品价格
     * 给定n个物品，一个容量为capacity的背包。每个物品只能选择一次，问在限定背包容量下能放入物品的最大价值。
     * 至多capacity，求最大价值
     * 恰好capacity，求最大价值
     * @param n
     * @param ws
     * @param vs
     * @param capacity
     * @return
     */
    //
    public int zeroOnePack(int n, int[] ws, int[] vs, int capacity) {
        //体积 <= capacity 的 最大价值
        //dfs(i,c) = Math.max(dfs(i-1,c) , dfs(i-1,c-w[i])+vs[i])
        //dfs(0,c) = 0 没得选价值就是0
        this.ws = ws;
        this.vs = vs;
        int k = dfs3(n - 1, capacity);
        return k < 0 ? -1 : k;
    }

    // 恰好
    public int dfs2(int i, int c) {
        if (i < 0) {
            return c == 0 ? 0 : Integer.MIN_VALUE/2;
        }
        if (c < ws[i]) {
            //物体容量超过c了只能不选
            return dfs2(i - 1, c);
        }

        return Math.max(dfs2(i - 1, c), dfs2(i - 1, c - ws[i]) + vs[i]);
    }

    public int zeroOnePackExactly(int n, int[] ws, int[] vs, int capacity) {
        //恰好需要刚好最后 c == 0 时
        int[][] dp = new int[n + 1][capacity + 1];
        Arrays.fill(dp[0],Integer.MIN_VALUE/2);
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            //为什么是从1开始因为dp[i][0]本身就是0，恰好容量为0那价值肯定为0
            for (int c = 1; c <= capacity; c++) {
                if (c < ws[i - 1]) {
                    dp[i][c] = dp[i - 1][c];
                }else {
                    dp[i][c] = Math.max(dp[i - 1][c], dp[i - 1][c - ws[i-1]] + vs[i - 1]);
                }
            }
        }
        return dp[n][capacity] < 0 ? -1 : dp[n][capacity];
    }

    // >= c
    public int dfs3(int i, int c) {
        if (i < 0) {
            return c <= 0 ? 0 : Integer.MIN_VALUE/2;
        }

        return Math.max(dfs3(i - 1, c), dfs3(i - 1, c - ws[i]) + vs[i]);
    }
    public int zeroOnePackLeast(int n, int[] ws, int[] vs, int capacity) {
        int[][] dp = new int[n + 1][capacity + 1];
        Arrays.fill(dp[0],Integer.MIN_VALUE/2);
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            //为什么是从0开始，不是1？
            // 因为dp[i][0]代表 第i次>=0容量的最大价值，可以看到>=0 dp[i][0]最大价值肯定不是0无法用默认值，因此要从新计算
            for (int c = 0; c <= capacity; c++) {
                dp[i][c] = Math.max(dp[i - 1][c], dp[i - 1][Math.max(c - ws[i-1],0)] + vs[i - 1]);
            }
        }
        return dp[n][capacity];
    }

    public int zeroOnePackMost(int n, int[] ws, int[] vs, int capacity) {
        //体积 <= capacity 的 最大价值
        //dfs(i,c) = Math.max(dfs(i-1,c) , dfs(i-1,c-ws[i])+vs[i])
        //dfs(0,c) = 0 没得选价值就是0


//        int[][] dp = new int[n+1][capacity+1];
//        //行优先
//        for (int i = 1; i <= n; i++) {
//            //第i次选择剩余容量c的时候
//            //为什么是从1开始因为dp[i][0]本身就是0，最多容量为0那价值肯定为0
//            for (int c = 1; c <= capacity; c++) {
//                if (c < ws[i-1]){
//                    dp[i][c] = dp[i-1][c];
//                }else {
//                    dp[i][c] = Math.max(dp[i-1][c],dp[i-1][c-ws[i-1]]+vs[i-1]);
//                }
//            }
//        }

//        //则容量为V的背包能够装入物品的最大值为
//        int maxValue = dp[n][capacity];
//        //逆推找出装入背包的所有商品的编号
//        int c = capacity;
//        String numStr="";
//        //倒序 x1 + x2 + ... 右添加
//        for(int i=n; i>0; i--){
//            //若果dp[i][j]>dp[i-1][j], 这说明第i件物品是放入背包的,因为根据特性有变化则
//            if(dp[i][c]>dp[i-1][c]){
//                numStr = i+"->"+numStr;
//                c=c-ws[i-1];
//            }
//            if(c==0)
//                break;
//        }
//        System.out.println(numStr);

        //优化1，从n+1 -> 2,因为每次i只和i-1有关，因为是行优先，所以只要2行即可
        //这种优化虽然节省了空间，但是丢失了过程，没办法回溯得到放入背包的所有商品的编号（物品行数据丢失）
//        int[][] dp = new int[2][capacity+1];
//        //行优先,先i行
//        for (int i = 1; i <= n; i++) {
//            //第i次选择剩余容量c的时候
//            for (int c = 1; c <= capacity; c++) {
//                if (c < ws[i-1]){
//                    dp[i%2][c] = dp[(i-1) % 2][c];
//                }else {
//                    dp[i%2][c] = Math.max(dp[(i-1)%2][c],dp[(i-1)%2][c-ws[i-1]]+vs[i-1]);
//                }
//            }
//        }

        //优化2，用一个数组完成，可以这样想,因为dfs(i,c) = Math.max(dfs(i-1,c) , dfs(i-1,c-ws[i])+vs[i])
        //dfs(1,3) = Math.max(dfs(0,3) , dfs(0,c-ws[3])+vs[3])  c >= c-ws[3] >= 0
        // 可以发现如果升序会导致dfs(1,3)覆盖dfs(0,3)的值，因为只有单数组，所以降序计算从大到小覆盖
        int[] dp = new int[capacity + 1];
        //行优先,先i行
        for (int i = 1; i <= n; i++) {
            //第i次选择剩余容量c的时候
            for (int c = capacity; c >= ws[i - 1]; c--) {
                dp[c] = Math.max(dp[c], dp[c - ws[i - 1]] + vs[i - 1]);
            }
        }


        return dp[capacity];
    }

    public int completelyPack(int n, int[] ws, int[] vs, int capacity) {
//        int[][] dp = new int[n+1][capacity+1];
//        //行优先
//        for (int i = 1; i <= n; i++) {
//            //第i次选择剩余容量c的时候
//            for (int c = 1; c <= capacity; c++) {
//                if (c < ws[i-1]){
//                    dp[i][c] = dp[i-1][c];
//                }else {
//                    dp[i][c] = Math.max(dp[i-1][c],dp[i][c-ws[i-1]]+vs[i-1]);
//                }
//            }
//        }

//        int[][] dp = new int[2][capacity + 1];
//        //行优先,先i行
//        for (int i = 1; i <= n; i++) {
//            //第i次选择剩余容量c的时候
//            for (int c = 1; c <= capacity; c++) {
//                if (c < ws[i - 1]) {
//                    dp[i % 2][c] = dp[(i - 1) % 2][c];
//                } else {
//                    dp[i % 2][c] = Math.max(dp[(i - 1) % 2][c], dp[i % 2][c - ws[i - 1]] + vs[i - 1]);
//                }
//            }
//        }

        int[] dp = new int[capacity + 1];
        //行优先,先i行
        for (int i = 1; i <= n; i++) {
            //第i次选择剩余容量c的时候
            for (int c = ws[i - 1]; c <= capacity; c++) {
                dp[c] = Math.max(dp[c], dp[c - ws[i - 1]] + vs[i - 1]);
            }
        }

        return dp[capacity];
    }

    public int manyPack(int n, int[] ws, int[] vs, int capacity,int[] num) {

        //初始化动态规划数组
        int[][] dp = new int[n+1][capacity+1];

        for(int i = 1; i < n + 1; i++){
            for(int c = 1; c < capacity + 1; c++){

                if(c < ws[i-1])
                    dp[i][c] = dp[i-1][c];
                else{
                    //考虑物品的件数限制 num[i-1]该物件上限，c/ws[i-1]  容量/物件容量 = 物件最大数量
                    int maxV = Math.min(num[i-1],c/ws[i-1]);

                    /**
                     * 1.为什么c - k * ws[i - 1] 不会越界，
                     * 因为 k是maxV <= c/ws[i-1] , 所以 k * ws[i - 1] <= c ，所以 c - k * ws[i - 1] >= 0
                     *
                     * 2.为什么下面要取组合最大值
                     * 因为题目就是总价值最大取最大，如果是最小则Min
                     */
                    int maxValue = dp[i][c];
                    for(int k = 0;k < maxV + 1; k++){
                        maxValue = Math.max(maxValue, Math.max(dp[i - 1][c], dp[i - 1][c - k * ws[i - 1]] + k * vs[i - 1]));
                    }
                    dp[i][c] = maxValue;
                }
            }
        }

        return dp[n][capacity];
    }


    /**
     * 深度优先搜索的暴力算法
     *
     * @param i 第i次选择
     * @param c 剩余容量
     * @return
     */
    public int dfs(int i, int c) {
        if (i < 0) {
            return 0;
        }
        if (c < ws[i]) {
            //物体容量超过c了只能不选
            return dfs(i - 1, c);
        }

        return Math.max(dfs(i - 1, c), dfs(i - 1, c - ws[i]) + vs[i]);
    }



    public int findTargetSumWaysEqs(int[] nums, int target) {
        int s = 0;
        for (int x : nums) {
            s += x;
        }
        s -= Math.abs(target);
        if (s < 0 || s % 2 == 1) {
            return 0;
        }
        int m = s / 2; // 背包容量

        int[] f = new int[m + 1];
        f[0] = 1;
        for (int x : nums) {
            for (int c = m; c >= x; c--) {
                f[c] += f[c - x];
            }
        }
        return f[m];
    }


    // <= target
    public int findTargetSumWaysMosts(int[] nums, int target) {
        int s = 0;
        for (int x : nums) {
            s += x;
        }
        s -= Math.abs(target);
        if (s < 0 || s % 2 == 1) {
            return 0;
        }
        int m = s / 2; // 背包容量

        int[] f = new int[m + 1];
        Arrays.fill(f, 1);
        for (int x : nums) {
            for (int c = m; c >= x; c--) {
                f[c] += f[c - x];
            }
        }
        return f[m];
    }

    // >= target
    public int findTargetSumWaysLeasts(int[] nums, int target) {
        int s = 0;
        for (int x : nums) {
            s += x;
        }
        s -= Math.abs(target);
        if (s < 0 || s % 2 == 1) {
            return 0;
        }
        int m = s / 2; // 背包容量

        int[] f = new int[m + 1];
        f[0] = 1;
        for (int x : nums) {
            for (int c = m; c >= 0; c--) {
                f[c] += f[Math.max(c - x, 0)];
            }
        }
        return f[m];
    }


    //选数正好是 (s+t)/2 的+1
    // dfs[i,c] = dfs[i-1,c] + dfs[i-1,c-w[i]] 0-1背包问题是否选择
    public int findTargetSumWaysEq(int[] nums, int target) {
        int s = 0;
        for (int x : nums) {
            s += x;
        }
        s -= Math.abs(target);
        if (s < 0 || s % 2 == 1) {
            return 0;
        }
        int m = s / 2; // 背包容量

        int n = nums.length;
        int[][] f = new int[2][m + 1];
        //也就是用 0 个数去凑出和为 0，有且只有一种「空集」方案——什么都不选。
        f[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int c = 0; c <= m; c++) {
                if (c < nums[i]) {
                    f[(i + 1) % 2][c] = f[i % 2][c]; // 只能不选
                } else {
                    f[(i + 1) % 2][c] = f[i % 2][c] + f[i % 2][c - nums[i]]; // 不选 + 选
                }
            }
        }
        return f[n % 2][m];
    }

    public int findTargetSumWaysMost(int[] nums, int target) {
        int s = 0;
        for (int x : nums) {
            s += x;
        }
        s -= Math.abs(target);
        if (s < 0 || s % 2 == 1) {
            return 0;
        }
        int m = s / 2; // 背包容量

        int n = nums.length;
        int[][] f = new int[2][m + 1];
        //也就是用 0 个数去凑出和为 0，有且只有一种「空集」方案——什么都不选。
        Arrays.fill(f[0], 1);

        for (int i = 0; i < n; i++) {
            for (int c = 0; c <= m; c++) {
                if (c < nums[i]) {
                    f[(i + 1) % 2][c] = f[i % 2][c]; // 只能不选
                } else {
                    f[(i + 1) % 2][c] = f[i % 2][c] + f[i % 2][c - nums[i]]; // 不选 + 选
                }
            }
        }
        return f[n % 2][m];
    }

    public int findTargetSumWaysLeast(int[] nums, int target) {
        int s = 0;
        for (int x : nums) {
            s += x;
        }
        s -= Math.abs(target);
        if (s < 0 || s % 2 == 1) {
            return 0;
        }
        int m = s / 2; // 背包容量

        int n = nums.length;
        int[][] f = new int[2][m + 1];
        //也就是用 0 个数去凑出和为 0，有且只有一种「空集」方案——什么都不选。
        f[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int c = 0; c <= m; c++) {
                if (c < nums[i]) {
                    f[(i + 1) % 2][c] = f[i % 2][c]; // 只能不选
                } else {
                    f[(i + 1) % 2][c] = f[i % 2][c] + f[i % 2][Math.max(c - nums[i], 0)]; // 不选 + 选
                }
            }
        }
        return f[n % 2][m];
    }

//    public int findTargetSumWaysByOne(int[] nums, int target) {
//        int s = 0;
//        for (int x : nums) {
//            s += x;
//        }
//        s -= Math.abs(target);
//        if (s < 0 || s % 2 == 1) {
//            return 0;
//        }
//        int m = s / 2; // 背包容量
//
//        int[] f = new int[m + 1];
//        f[0] = 1;
//        for (int x : nums) {
//            for (int c = m; c >= x; c--) {
//                f[c] += f[c - x];
//            }
//        }
//        return f[m];
//    }
}
