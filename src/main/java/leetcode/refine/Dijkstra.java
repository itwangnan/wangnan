package leetcode.refine;

import arr.Arr;
import com.google.common.collect.Lists;
import org.apache.commons.math3.analysis.function.Max;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Dijkstra {


    public static void main(String[] args) {

//        int[][] arr = new int[4][4];
//
//        arr[1][0] = 1;
//        arr[1][2] = 1;
//        arr[2][3] = 1;
//        arr[0][3] = 1;
//
//        exec1(arr,1,3);
//        exec2(arr,1,3);

//        int[][] arr = new int[7][7];
//
//        arr[0][1] = 2;
//        arr[1][0] = 2;
//        arr[0][2] = 6;
//        arr[2][0] = 6;
//        arr[1][3] = 5;
//        arr[3][1] = 5;
//        arr[2][3] = 8;
//        arr[3][2] = 8;
//        arr[5][3] = 15;
//        arr[3][5] = 15;
//        arr[3][4] = 10;
//        arr[4][3] = 10;
//        arr[5][4] = 6;
//        arr[4][5] = 6;
//        arr[5][6] = 6;
//        arr[6][5] = 6;
//        arr[4][6] = 2;
//        arr[6][4] = 2;

//        exec1(arr,0,6);
//        exec2(arr,0,6);
//        exec1();
//        exec2();

//        int[][] arr = new int[][]{
//                new int[]{1,4,3,1,3,2},
//                new int[]{3,2,1,3,2,4},
//                new int[]{2,3,3,2,3,1}
//        };
//
//        leetCode407(arr,3,6);

//        int[][] arr2 = new int[][]{
//                new int[]{1,2,2},
//                new int[]{3,8,2},
//                new int[]{5,3,5}
//        };
        int[][] arr2 = new int[][]{
                new int[]{1,2,1,1,1},
                new int[]{1,2,1,2,1},
                new int[]{1,2,1,2,1},
                new int[]{1,2,1,2,1},
                new int[]{1,1,1,2,1}
        };

        leetCode1631(arr2,0,0,4,4);

    }

    private static void leetCode1631(int[][] arr,int startX,int startY,int endX,int endY) {

        /**
         *
         * 最小体力消耗路径
         * https://leetcode.cn/problems/path-with-minimum-effort/description/
         *
         * 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights ，
         * 其中 heights[row][col] 表示格子 (row, col) 的高度。一开始你在最左上角的格子 (0, 0) ，
         * 且你希望去最右下角的格子 (rows-1, columns-1) （注意下标从 0 开始编号）。
         * 你每次可以往 上，下，左，右 四个方向之一移动，你想要找到耗费 体力 最小的一条路径。
         *
         * 一条路径耗费的 体力值 是路径上相邻格子之间 高度差绝对值 的 最大值 决定的。
         *
         * 请你返回从左上角走到右下角的最小 体力消耗值 。
         *
         */
        //int[0] 节点下标 ， int[1]最短路径长度
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (a[2] - b[2]));

        /**
         * m * n
         */
        int m = arr.length;
        int n = arr[0].length;

        //最短路径   i代表节点
        int[][] dis = new int[m][n];
        //上一步节点 i节点 上一步
//        int[] parent = new int[n];

//        Arrays.fill(parent,-1);
//        Arrays.fill(dis,Integer.MAX_VALUE);
        for (int[] ints : dis) {
            Arrays.fill(ints,Integer.MAX_VALUE);
        }

        dis[startX][startY] = arr[startX][startY];

        pq.offer(new int[]{startX,startY,dis[startX][startY]});
        //标志结束

        int[] direction = {-1,0,1,0,-1};
//        int[] direction = {-1,-1,1,1,-1};
//        int[] direction = {0,-1,-1,0,1,1,-1,1,0};
        while (!pq.isEmpty()) {

            //注意这里处理数据本质是O(n)的，如果用小根堆则O(1)
            int[] poll = pq.poll();
            int x = poll[0];
            int y = poll[1];
            int dx = poll[2];
//            int pre = poll[2];

//            parent[x] = pre;

            //处理数据
            for (int i = 0; i < direction.length - 1; i++) {

                int newX = x + direction[i];
                int newY = y + direction[i + 1];

                if (newX >= 0 && newY >=0 && newX < m && newY < n){
                    int abs = Math.abs(dx - arr[newX][newY]);
                    if (abs < dis[newX][newY]){
                        dis[newX][newY] = abs;

                        pq.offer(new int[]{newX,newY,arr[newX][newY]});
                    }
                }

            }

        }

        System.out.println(dis[endX][endY]);
//        //打印路线
//        StringBuilder sb = new StringBuilder();
//        while (true){
//            sb.insert(0,end);
//            sb.insert(0,"->");
//            end = parent[end];
//            if (end == -2){
//                break;
//            }
//            if (end == -1){
//                sb.delete(0,sb.length());
//                break;
//            }
//        }
//        System.out.println("开始"+sb.toString());

    }

    private static void leetCode407(int[][] arr,int row,int col) {

        /**
         *
         * 3d 接雨水
         * https://leetcode.cn/problems/trapping-rain-water-ii/
         *
         * 给你一个 m x n 的矩阵，其中的值均为非负整数，代表二维高度图每个单元的高度，请计算图中形状最多能接多少体积的雨水。
         *
         * 思路一:采用Dijkstra 图最短路径实现
         *
         * 这道题的本质是 dijkstra 最短路算法，每个格子是一个结点，相邻两格子之间有一条边，每条边的权重为两端点高度的最大值，
         * 最短路里的加法替换为了 max 运算，
         * 建图方式类似于 1631. 最小体力消耗路径 这道题，1631 这道题的最短路解法和这道题很相似。
         *
         */

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (a[2] - b[2]));

        //首先把一圈都进堆
        boolean[][] exist = new boolean[row][col];
        for (int i = 0; i < arr.length; i++) {

            for (int j = 0; j < arr[i].length; j++) {

                if (i == 0 || j == 0 || i == arr.length - 1 || j == arr[i].length - 1){

                    pq.offer(new int[]{i, j, arr[i][j]});
                    exist[i][j] = true;
                }
            }
        }

        int[] direction = {-1,0,1,0,-1};

        int sum = 0;
        while (!pq.isEmpty()){

            int[] flag = pq.poll();

            int i = flag[0];
            int j = flag[1];
            int h = flag[2];

            //
            for (int k = 0; k < direction.length-1; k++) {

                int newI = i + direction[k];

                int newJ = j + direction[k + 1];


                if (newI > 0 && newJ > 0
                        && newI < row && newJ < col && !exist[newI][newJ]){
                    //证明没有处理过则入堆

                    int newH = Math.max(arr[newI][newJ], h);

                    sum += (newH - arr[newI][newJ]);

                    exist[newI][newJ] = true;

                    pq.offer(new int[]{newI, newJ, newH});

                }

            }

        }

        System.out.println(sum);
        
    }

    /**
     * 朴素Dijkstra 查找最短路径 （适用于稠密图 边多）
     * @return
     */
    private static void exec1(int[][] arr,int start,int end) {

        /**
         * n * n
         */
        int n = arr.length;
        int k = start;

        //最短路径   i代表节点
        int[] dis = new int[n];
        //上一步节点 i节点 上一步
        int[] parent = new int[n];
        //是否使用   i节点是否使用
        boolean[] done = new boolean[n];

        Arrays.fill(parent,-1);
        Arrays.fill(dis,Integer.MAX_VALUE);

        dis[k] = 0;
        parent[k] = -2;

        //标志结束

//        int pre = -2;
        while (true) {


            int x = Integer.MAX_VALUE;

            //处理数据
            for (int i = 0; i < dis.length; i++) {
                if (dis[i] == Integer.MAX_VALUE){
                    continue;
                }
                if (!done[i] && dis[i] < x){
                    x = i;
                }
            }

            //没有找到有效的下一个节点结束 || 或者到达不了
            if(x == Integer.MAX_VALUE){
                break;
            }

            done[x] = true;

//            pre = x;

            //处理数据
            for (int i = 0; i < arr.length; i++) {
                if (arr[x][i] == 0){
                    continue;
                }
                int newDis = dis[x] + arr[x][i];
                if (dis[i] > newDis){
                    dis[i] = newDis;

                    parent[i] = x;
                }
//                dis[i] = Math.min(dis[x] + arr[x][i],dis[i]);
            }

        }

        System.out.println(dis[end]);
        //打印路线
        StringBuilder sb = new StringBuilder();
        while (true){
            sb.insert(0,end);
            sb.insert(0,"->");
            end = parent[end];
            if (end == -2){
                break;
            }
            if (end == -1){
                sb.delete(0,sb.length());
                break;
            }
        }
        System.out.println("开始"+sb.toString());

    }

    /**
     * 堆优化 Dijkstra（适用于稀疏图）
     * @return
     */
    private static void exec2(int[][] arr,int start,int end) {

        //int[0] 节点下标 ， int[1]最短路径长度
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (a[0] - b[0]));

        /**
         * n * n
         */
        int n = arr.length;
        int k = start;

        //最短路径   i代表节点
        int[] dis = new int[n];
        //上一步节点 i节点 上一步
        int[] parent = new int[n];
        //是否使用   i节点是否使用
//        boolean[] done = new boolean[n];

        Arrays.fill(parent,-1);
        Arrays.fill(dis,Integer.MAX_VALUE);

        dis[k] = 0;

        pq.offer(new int[]{dis[k],k,-2});
        //标志结束

        while (!pq.isEmpty()) {

            //注意这里处理数据本质是O(n)的，如果用小根堆则O(1)
            int[] poll = pq.poll();
            int dx = poll[0];
            int x = poll[1];
            int pre = poll[2];
//            for (int i = 0; i < dis.length; i++) {
//                if (dis[i] == Integer.MAX_VALUE){
//                    continue;
//                }
//                if (!done[i] && dis[i] < x){
//                    x = i;
//                }
//            }

//            done[x] = true;

            parent[x] = pre;

            //处理数据
            for (int i = 0; i < arr.length; i++) {
                if (arr[x][i] == 0){
                    continue;
                }
                if (dx + arr[x][i] < dis[i]){
                    dis[i] = dx + arr[x][i];
                    pq.offer(new int[]{dis[i],i,x});
                }

            }

        }

        System.out.println(dis[end]);
        //打印路线
        StringBuilder sb = new StringBuilder();
        while (true){
            sb.insert(0,end);
            sb.insert(0,"->");
            end = parent[end];
            if (end == -2){
                break;
            }
            if (end == -1){
                sb.delete(0,sb.length());
                break;
            }
        }
        System.out.println("开始"+sb.toString());

    }





}
