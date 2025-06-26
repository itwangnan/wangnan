package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 51. N 皇后
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 * https://leetcode.cn/problems/n-queens/description/
 */
public class LeetCode51 {


    @Test
    public void test() {
        int[] nums = {1,2,3};
        for (List<String> list : solveNQueens(4)) {
            System.out.println(list);
        }
    }

    List<List<String>> res;
    int n;
    String temp;
    //皇后位置 (r,queens[r])
    int[] queens;
    boolean[] col;
    boolean[] lSlash;
    boolean[] rSlash;

    public List<List<String>> solveNQueens(int n) {
        res = new ArrayList<>();
        this.n = n;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            sb.append(".");
        }
        temp = sb.toString();
        queens = new int[n];
        col = new boolean[n];
        lSlash = new boolean[n * 2 - 1];
        rSlash = new boolean[n * 2 - 1];
        this.dfs(0);
        return res;
    }

    private void dfs(int r) {
        if (r == n) {
            List<String> list = new ArrayList<>();
            StringBuilder sb = new StringBuilder(temp);
            for (int j = 0; j < queens.length; j++) {
                int q = queens[j];
                sb.replace(q,q+1,"Q");
                list.add(sb.toString());
                sb.replace(q,q+1,".");
            }
            res.add(list);
            return;
        }


        /**
         * 因为n皇后的特殊性，每行每列必须有一个皇后，所以行为标准记录 queens[r] = i  (r,i)有Q
         * col[i]记录被选中列
         * 注意斜线有特殊性质就是 如果 (0,1)为皇后则 (1,2),(2,3)  和 (1,0) 都不能选择的，
         * 可以发现左斜线 0 + 1 = 1 + 0 ,右斜线 0 - 1 = 1 - 2 = 2 - 3 ,但是可能有负数问题所以减法 加个n-1
         * 所以需要lSlash和rSlash 长度都是2n-1
         *
         * 另外从上到下 和 从下到上 左右斜线的性质是相反的
         */
        for (int i = 0; i < n; i++) {
            int rc = r - i + n - 1; //保证正数
            if (!col[i] && !lSlash[r + i] && !rSlash[rc]) { // 判断能否放皇后
                queens[r] = i; // 直接覆盖，无需恢复现场
                col[i] = lSlash[r + i] = rSlash[rc] = true; // 皇后占用了 c 列和两条斜
                dfs(r + 1);
                col[i] = lSlash[r + i] = rSlash[rc] = false; // 恢复现场
            }
        }
    }
}
