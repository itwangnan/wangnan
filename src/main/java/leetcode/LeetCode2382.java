package leetcode;

import org.junit.Test;

import java.util.Arrays;

/**
 * 2382. 删除操作后的最大子段和
 * https://leetcode.cn/problems/maximum-segment-sum-after-removals/description/
 */
public class LeetCode2382 {


    @Test
    public void test() {
        int[] nums = {244,19,445,671,801,103,291,335,781,33,51,789,746,510,38,7,529,905};
//        int[][] edges = {{0, 1}, {0, 2}, {2, 3}, {2, 4}};
        //[14, 7, 2, 2, 0]
        int[] removeQueries = {4,8,11,12,1,5,0,9,6,17,3,15,14,7,2,13,16,10};
        System.out.println(Arrays.toString(this.maximumSegmentSum(nums, removeQueries)));
    }

    int[] fa;

    public long[] maximumSegmentSum(int[] nums, int[] removeQueries) {
        var n = nums.length;
        //初始化并查集：fa[i] = i（注意这里把下标扩展到 n+1 大小）
        fa = new int[n + 1];
        for (var i = 0; i <= n; i++) fa[i] = i;
        //sum[i] 用来维护“以 i 为并查集根（root）对应的那段连续子数组的总和” （该代表元对应区间的值）
        var sum = new long[n + 1];
        //ans[i] 最终要返回的长度也是 n，代表“在第 i 次 remove 操作之后”的最大连续和
        var ans = new long[n];

        /**
         * 因为有合并操作，x 合并 x+1 ，如果不让fa和sum 长度是n+1就要对最后一个下标特殊处理，因为没法合并了
         * 如果长度是n+1就可以保证都可以合并，sum[n] = 0也是没关系的
         */
        for (var i = n - 1; i > 0; --i) {
            var x = removeQueries[i];
            // find(x+1)：实际上是去找“x + 1”这个下标当前所属的并查集根
            //  为什么用 x+1？因为我们把每个连通块的代表(根)设在“区间最右端 + 1”上，或借助 n 作为哨兵
            var to = find(x + 1);
            fa[x] = to; // 合并 x 和 x+1

            //本身区间sum[to] + 其他区间 sum[x] + 当前节点nums[x] sum(x+1,...,b) + sum(a,...,x-1) + nums[x] 正是三部分
            sum[to] = sum[to] + sum[x] + nums[x];

            //因为是增加的方式，之前最大的区间ans[i] 和 目前合并区间sum[to]比较就行
            ans[i - 1] = Math.max(ans[i], sum[to]);
        }
        return ans;
    }

    public int find(int a) {
        if (a == fa[a]) {
            return a;
        }

        //这步将链路上的都一纬化
        fa[a] = find(fa[a]);
        return fa[a];
    }

}
