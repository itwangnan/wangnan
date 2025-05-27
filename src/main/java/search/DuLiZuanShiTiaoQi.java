package search;

import java.util.Scanner;

public class DuLiZuanShiTiaoQi {

    //Move: 棋子走步
    //(sx,xy):起步坐标 (xv,yv):走步方向
    private static class Move{
        int sx;
        int sy;
        int xv;
        int yv;
    }

    private static int MAX = 100000;
    private static int SIZE = 7;
    private static int num;
    private static int endx,endy;
    private static int[][] board = new int[SIZE][SIZE];//board[7][7]表示棋盘;board[x][y]:0->空方格 1->棋子占用方格 2->棋盘外方格

    private static Move[] moves = new Move[MAX];
    private static Move[] ans = new Move[MAX];
    private static int count;

    private static long[][] gmoves;
    private static long gboard;


    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        while (true){
            count = 0;

            int x,y;
            init();

//            num = input.nextInt();
            num = 32;
//            for(int i=0; i<num; i++){
//                x = input.nextInt();
//                y = input.nextInt();
//                board[x][y] = 1;
//            }
            endx = input.nextInt();
            endy = input.nextInt();
            caches();
            genmv();
            initreg();
            if(backtrack(0))
                for(int i=0; i<num-1; i++)
                    out(ans[i]);
            else
                System.out.println("No Solution!");
        }
    }

//    private static void init(){
//        for(int i=0; i<SIZE; i++)
//            for(int j=0; j<SIZE; j++)
//                if((i<2||i>4) && (j<2||j>4))
//                    board[i][j] = 2;
//                else
//                    board[i][j] = 0;
//    }

    private static void setY(int y) {
        for (int i = 0; i < SIZE; i++) {
            board[i][y] = 1;
        }
    }

    private static void setX(int x) {
        for (int i = 0; i < SIZE; i++) {
            board[x][i] = 1;
        }
    }


    private static void setPoint(int x,int y) {
        board[x][y] = 0;
    }

    private static void init() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 2;
            }
        }
        setY(2);
        setY(3);
        setY(4);

        setX(2);
        setX(3);
        setX(4);
        setPoint(3,3);
    }

    private static void caches(){
        for(int i=0; i<SIZE; i++)
            for(int j=0; j<SIZE; j++){
                if(i<6 && i>0){
                    cache(i,j,1,0);
                    cache(i,j,-1,0);
                }
                if(j<6 && j>0){
                    cache(i,j,0,1);
                    cache(i,j,0,-1);
                }
            }
    }

    //所有76个合法走步存储在数组moves中
    private static void cache(int x, int y, int xv, int yv){
        if((board[x][y]!=2) && (board[x+xv][y+yv])!=2 && (board[x-xv][y-yv]!=2)){
            Move m = new Move();
            m.sx = x-xv;
            m.sy = y-yv;
            m.xv = xv;
            m.yv = yv;
            moves[count++] = m;
        }
    }

    //gmoves存储相应的走步状态，用于判定走步的合法性，由数组moves产生gmoves的值
    private static void genmv(){
        gmoves = new long[count][2];
        for(int j=0; j<count; j++){
            Move m;
            m = moves[j];
            gmoves[j][0] = coord2bit(m.sx,m.sy);
            gmoves[j][0] |= coord2bit(m.sx+m.xv,m.sy+m.yv);
            gmoves[j][0] |= coord2bit(m.sx+2*m.xv,m.sy+2*m.yv);
            gmoves[j][1] = coord2bit(m.sx,m.sy)|coord2bit(m.sx+m.xv,m.sy+m.yv);
        }
    }

    //棋盘上棋子的一种状态可以看成全部方格(33个)的一种二进制排列
    //有棋子为1，无棋子为0
    //将棋盘坐标转换为相应的位
    //注意64位long型
    private static long coord2bit(int x, int y){
        int pos;
        switch (y){
            case 0: pos = x-2; break;
            case 1: pos = x+1; break;
            case 5: pos = x+25; break;
            case 6: pos = x+28; break;
            default: pos = x-8+y*7;
        }

        return (long)1<<pos;//左移
    }

    //gboard: 表示当前棋盘整体状态，由initreg进行初始化
    private static void initreg(){
        gboard = 0;
        for(int i=0; i<SIZE; i++)
            for(int j=0; j<SIZE; j++)
                if(board[i][j] == 1)
                    gboard |= coord2bit(i,j);
    }

    private static boolean backtrack(int t){
        long[] m;
        if(finish(t)) return true;
        for(int i=0; i<count; i++){
            m = gmoves[i];
            if(ok(m)){
                next(m);
                if(backtrack(t+1)){
                    ans[t] = find(m);
                    return true;
                }
                restore(m);
            }
        }

        return false;
    }

    //检测终止条件
    //(endx,endy): 终止方格坐标
    //num: 初始棋盘上棋子个数
    private static boolean finish(int t){
        if(endx>0 || endy>0)
            return gboard==coord2bit(endx,endy);
        else
            return t>num-2;
    }

    //判定走步move的合法性
    private static boolean ok(long[] move){
        return ((gboard & move[0]) == move[1]);
    }

    //将棋盘状态变换为走步move后的状态
    private static void next(long[] move){
        gboard ^= move[0];
    }

    //将棋盘状态恢复为走步move前的状态
    private static void restore(long[] move){
        gboard ^= move[0];
    }

    //找出与走步状态相应的走步描述
    private static Move find(long[] m){
        for(int i=0; i<count; i++)
            if(gmoves[i] == m)
                return moves[i];

        return new Move();
    }

    private static void out(Move m){
        System.out.println("("+m.sx+","+m.sy+")("+(m.sx+2*m.xv)+","+(m.sy+2*m.yv)+")");
    }
}