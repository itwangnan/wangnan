package leetcode.refine.binary_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NTest {

    private List<List<String>> res = new ArrayList<>();
    private int count;
//    private boolean[][] flag;
    private boolean[] use,diag1,diag2;

    private int[] col;

    @Test
    public void leetCode51() {
        //https://leetcode.cn/problems/n-queens/description/
        // n皇后

        //因为不同行，不同列的情况，导致每一行每一列只能有1个皇后
        //另外斜线保证的是(x,y) x+y不能有一样的皇后

        leetCode51();
        List<List<String>> res = solveNQueens(4);

        for (List<String> re : res) {
            System.out.println(re);
        }
        System.out.println(count);

    }

    public List<List<String>> solveNQueens(int n) {
//        flag = new boolean[n][n];
        col = new int[n];
        use = new boolean[n];
        diag1 = new boolean[n * 2 - 1];
        diag2 = new boolean[n * 2 - 1];
        this.dfs(0, n);
        return res;
    }

    /**
     * 时间复杂度O（n^2 * n!）,构造答案需要n^2,一共n!个叶子节点，非叶子节点则是n次循环
     * 空间复杂度O(n)
     * @param level
     * @param n
     */
    private void dfs(int level, int n) {

        if (level == n) {
            List<String> path = new ArrayList<>();
            for (int i : col) {
                char[] chars = new char[n];
                Arrays.fill(chars,'.');
                chars[i] = 'Q';
                path.add(new String(chars));
            }
            res.add(path);
            count++;
            return;
        }


        for (int i = 0; i < n; i++) {

            //这里因为要判断 level + i右上有没出现过，level - i左上有没出现过，就要记录下因为level - i最大为n-1则在加上n-1
            int rc = level - i + n - 1;

            if (!use[i] && !diag1[level + i] && !diag2[rc]) {
                col[level] = i;
                use[i] = diag1[level + i] = diag2[rc] = true;
                dfs(level + 1,n);
                use[i] = diag1[level + i] = diag2[rc] = false; // 恢复现场
            }

//            int upperRight = level + i;
//            int upperLeft = level - i;
//            boolean isCon = false;
//
//            for (int j = level - 1; j >= 0; j--) {
//
//                if (upperRight - j >= 0 && upperRight - j < n && (flag[j][upperRight - j])) {
//                    isCon = true;
//                    break;
//                }
//                if (j - upperLeft  >= 0 && j - upperLeft < n && (flag[j][j - upperLeft])) {
//                    isCon = true;
//                    break;
//                }
//            }
//
//            if (!isCon && !use[i]) {
//
//                col[level] = i;
//                flag[level][i] = true;
//
//                use[i] = true;
//                dfs(level + 1, n);
//                use[i] = false;
//
//                flag[level][i] = false;
//
//            }
        }
    }
}
