package search;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 在BellmanFordFinder基础上优化，因为只有变化的节点周围的点才需要重新计算所以增加队列
 * d[j] = min(d[j],d[i] + edges[i][j)
 */
public class SPFAFinder {

    @Test
    public void test() {
        int[][] st = new int[5][5];
        st[0][1] = 90;
        st[0][3] = 80;
        st[0][4] = 75;
        st[1][2] = -30;
        st[2][4] = 10;
        st[3][2] = -30;
        st[3][4] = 10;
        st[4][3] = 10; //负环检查
        this.finder(st,5);

    }

    private void finder(int[][] edges, int v) {
        //代价
        int[] d = new int[v];
        int[] inQueueCount = new int[v];
        boolean[] isExists = new boolean[v];
        Arrays.fill(d,Integer.MAX_VALUE);
        d[0] = 0;

        //来源
        int[] p = new int[v];

        //queue存的是第几个点变动的下标
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        isExists[0] = true;
        while (!queue.isEmpty()){
            Integer i = queue.poll();
            isExists[i] = false;

            for (int j = 0; j < v; j++) {
                if (edges[i][j] == 0){
                    continue;
                }
                //这里就是正常边数E
                int newD = d[i] + edges[i][j];

                if (newD < d[j]){
                    inQueueCount[j]++;

                    // 如果一个节点被加入队列超过 V-1 次，说明图中有负权环
                    if (inQueueCount[j] >= v) {
                        System.out.println("存在负权环");
                        return;
                    }
                    d[j] = newD;
                    p[j] = i;
                    queue.offer(j);
                    isExists[j] = true;
                }
            }
        }


        System.out.println("正常结束");
    }

}
