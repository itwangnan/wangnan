package search;

import org.junit.Test;

import java.util.Arrays;

/**
 * 单源最短路径问题的算法
 * 时间复杂度O(V E),V顶点数，E边数
 */
public class BellmanFordFinder {




    @Test
    public void test(){
        int[][] st = new int[5][5];
        st[0][1] = 90;
        st[0][3] = 80;
        st[0][4] = 75;
        st[1][2] = -30;
        st[2][4] = 10;
        st[3][2] = -30;
        st[3][4] = 10;
//        st[4][3] = 10; //负环检查
        this.finder(st,5);




    }

    private void finder(int[][] edges, int v) {
        //代价
        int[] d = new int[v];
        Arrays.fill(d,Integer.MAX_VALUE);
        d[0] = 0;

        //来源
        int[] p = new int[v];

        int flag = v;

        boolean isUpdate = false;
        //隐藏点：当每个点都过一遍后v，就不会在有边权重变化了，除非存在负环
        while (flag > 0){

            isUpdate = false;
            //d[j] = min(d[j],d[i] + edges[i][j)
            for (int i = 0; i < v; i++) {
                for (int j = 0; j < v; j++) {
                    //无法到达
                    if (edges[i][j] == 0){
                        continue;
                    }
                    //这里就是正常边数E
                    int newD = d[i] + edges[i][j];
                    if (newD < d[j]){
                        d[j] = newD;
                        p[j] = i;
                        isUpdate = true;
                    }
                }
            }

            if (!isUpdate){
                break;
            }

            flag--;
        }

        if (isUpdate){
            System.out.println("存在负数环");
        }


    }
}
