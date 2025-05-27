package search;

import org.junit.Test;

import java.util.Arrays;

/**
 * Floyd-Warshall 算法（弗洛伊德最短路径算法）
 * 多源最短路径问题的算法
 * n是顶点个数，时间复杂度 O(n^3) ，空间:O(n^2)
 * 判断是否存在负环，可以通过结束后对角线是否d[x][x] < 0判断
 */
public class FloydFinder {

    @Test
    public void test() {

        int[][] st = new int[5][5];
        Arrays.stream(st).forEach(x -> Arrays.fill(x,Integer.MAX_VALUE));
        st[0][1] = 10;
        st[0][3] = 30;
        st[0][4] = 100;
        st[1][2] = 50;
        st[2][4] = 10;
        st[3][2] = 20;
        st[3][4] = 60;
        for (int i = 0; i < st.length; i++) {
            st[i][i] = 0;
        }

        this.finder(st,5);
    }

    private void finder(int[][] d, int v) {

        int[][] p = new int[v][v];
        Arrays.stream(p).forEach(x -> Arrays.fill(x,-1));

        //
        for (int c = 0; c < v; c++) {

            for (int i = 0; i < v; i++) {

                if (d[i][c] == Integer.MAX_VALUE) {
                    continue;
                }

                for (int j = 0; j < v; j++) {

                    if (d[c][j] != Integer.MAX_VALUE && d[i][c] + d[c][j] < d[i][j]){
                       d[i][j] = d[i][c] + d[c][j];
                       p[i][j] = c;
                    }

                }


            }
        }

        for (int i = 0; i < v; i++) {
            if (d[i][i] < 0){
                System.out.println("存在负权环");
                return;
            }
        }

        System.out.println("正常等到结果");
    }


}
