package leetcode;

import org.junit.Test;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/number-of-good-paths/submissions/636074246/
 * 第一种写法，分为大小合并，将小树合并到大树（秩合并），并保存大数个数
 */
public class LeetCode2421 {
    @Test
    public void test() {
//        int[] vals = {2,5,5,1,5,2,3,5,1,5};
        int[] vals = {1,1,2,2,3};
//        int[] vals = {2,5,5,1,5,2,3,5,1,5};
//        int[] vals = {1,1,1};
//        int[] vals = {5,1,4,2,1,5,4,3};
//        int[] vals = {2,2,5,5};
//        int[][] edges = {{0,1},{2,1},{3,2},{3,4},{3,5},{5,6},{1,7},{8,4},{9,7}};
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {2, 4}};
//        int[][] edges = {{0,1},{2,1},{3,2},{3,4},{3,5},{5,6},{1,7},{8,4},{9,7}};
//        int[][] edges = {{0,2},{2,1}};
//        int[][] edges = {{1,0},{2,0},{3,2},{4,2},{5,4},{6,4},{6,7}};
//        int[][] edges = {{1,0},{0,2},{3,2}};
        System.out.println(this.numberOfGoodPaths(vals, edges));
    }

    static int MAXN = 30001;
    static int[] arr = new int[MAXN];
    static int[] cnt = new int[MAXN];

    /**
     * 第一种方式，合并把小的合并到大的树，并将arr[小] = 大，这样可以利用vals[大]的值就不需要在一个max数组了
     * @param vals
     * @param edges
     * @return
     */
    public int numberOfGoodPaths(int[] vals, int[][] edges) {
        //使用并查集
        build(vals.length);
        Arrays.sort(edges, (e1, e2) -> Math.max(vals[e1[0]], vals[e1[1]]) - Math.max(vals[e2[0]], vals[e2[1]]));

        int res = vals.length;
        for (int[] edge : edges) {
            res += add(edge[0], edge[1],vals);
        }
        return res;
    }

    private void build(int n) {
        for (int i = 0; i < n; i++) {
            arr[i] = i;
            cnt[i] = 1;
        }
    }

    public int find(int a) {
        if (a == arr[a]) {
            return a;
        }

        //这步将链路上的都一纬化
        arr[a] = find(arr[a]);
        return arr[a];
    }

    public int add(int a, int b, int[] vals) {
        int fa = find(a);
        int fb = find(b);

        if (fa == fb) {
            return 0;
        }
//        int flag = Math.min(a, b);
//        b = Math.max(a,b);
//        a = flag;
        //a < b
        //这里可以有不同的处理逻辑进行合并，可以根据树大小或高度（秩合并）
        //根据用小的树合并大的树
        int res = 0;
        if (vals[fa] > vals[fb]){
            //小的指向大
            arr[fb] = fa;
        }else if (vals[fa] < vals[fb]){
            arr[fa] = fb;
        }else {
            arr[fa] = fb;
            res += cnt[fa] * cnt[fb];
            cnt[fb] += cnt[fa];
        }
        return res;
    }


}
