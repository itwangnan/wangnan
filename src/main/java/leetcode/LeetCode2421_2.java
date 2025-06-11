package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 第二种写法，相当于是将结果保存到0下标处
 */
public class LeetCode2421_2 {

    @Test
    public void test() {
        int[] vals = {2,5,5,1,5,2,3,5,1,5};
//        int[] vals = {1,1,2,2,3};
//        int[] vals = {2,5,5,1,5,2,3,5,1,5};
//        int[] vals = {1,1,1};
//        int[] vals = {5,1,4,2,1,5,4,3};
//        int[] vals = {2,2,5,5};
        int[][] edges = {{0,1},{2,1},{3,2},{3,4},{3,5},{5,6},{1,7},{8,4},{9,7}};
//        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {2, 4}};
//        int[][] edges = {{0,1},{2,1},{3,2},{3,4},{3,5},{5,6},{1,7},{8,4},{9,7}};
//        int[][] edges = {{0,2},{2,1}};
//        int[][] edges = {{1,0},{2,0},{3,2},{4,2},{5,4},{6,4},{6,7}};
//        int[][] edges = {{1,0},{0,2},{3,2}};
        System.out.println(this.numberOfGoodPaths(vals, edges));
    }

    static int MAXN = 30001;
    static int[] arr = new int[MAXN];
    static int[] cnt = new int[MAXN];
    static int[] max = new int[MAXN];
    static int[] res = new int[MAXN];

    public int numberOfGoodPaths(int[] vals, int[][] edges) {

        build(vals);
        Arrays.sort(edges, (e1, e2) -> Math.max(vals[e1[0]], vals[e1[1]]) - Math.max(vals[e2[0]], vals[e2[1]]));

        for (int[] edge : edges) {
            union(edge[0],edge[1]);
        }

        return res[0];
    }

    private void build(int[] vals) {
        for (int i = 0; i < vals.length; i++) {
            arr[i] = i;
            max[i] = vals[i];
            cnt[i] = 1;
            res[i] = 1;
        }
    }

    private void union(int a, int b) {
        a = find(a);
        b = find(b);

        if (a == b) {
            return;
        }
        int flag = Math.min(a, b);
        b = Math.max(a,b);
        a = flag;

        //a是相对小的值如 a=0 b=1,现在把所有点结果放在0处则只能让 xxx[a] = ?
        arr[b] = a;
        if (max[a] > max[b]){
            res[a] += res[b];
        } else if (max[a] < max[b]) {
            max[a] = max[b];
            cnt[a] = cnt[b];
            res[a] += res[b];
        }else {
            res[a] = cnt[b] * cnt[a] + res[b] + res[a];
            cnt[a] += cnt[b];
        }

    }

    private int find(int a) {
        if (arr[a] == a){
            return a;
        }
        arr[a] = find(arr[a]);
        return arr[a];
    }


}
