package search;

import org.junit.Test;

import java.util.*;

public class DuLiZuanShi {

    //棋盘大小 默认7*7
    static int SIZE = 7;

    //棋盘，不允许移动0，不存在-1，存在1
    static int[][] BOARDS = new int[SIZE][SIZE];

    static int[] direction = {-1,0,1,0,-1};

    static Stack<Blank> BLANK_STACK = new Stack<>();

    static Map<Long,Boolean> CACHE = new HashMap<>();

    static Map<Integer,Map<Integer,Integer>> NUM_CACHE = new HashMap<>();

    private static void setY(int y) {
        for (int i = 0; i < SIZE; i++) {
            BOARDS[i][y] = 1;
        }
    }

    private static void setX(int x) {
        for (int i = 0; i < SIZE; i++) {
            BOARDS[x][i] = 1;
        }
    }

    private static void valid(int x,int y) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

            }
        }
    }

    private static void setPoint(int x,int y) {
        BOARDS[x][y] = -1;
        BLANK_STACK.push(new Blank(x,y));
    }

    private static void delPoint(int x,int y) {
        BOARDS[x][y] = 1;
        BLANK_STACK.pop();
    }


    @Test
    public void test() {

        //初始化
        this.init();

        int exitsCount = 0;

        int count = 0;
        long sum = 0;
        //并且计算总
        for (int x = 0; x < SIZE; x++) {
            NUM_CACHE.computeIfAbsent(x, k -> new HashMap<>());
            for (int y = 0; y < SIZE; y++) {
                if (BOARDS[x][y] == 1){
                    sum |= (1L << count);
                    NUM_CACHE.get(x).put(y,count);
                    count++;
                    exitsCount++;
                }else if (BOARDS[x][y] == -1){
//                    sum -= (1L << count);
                    NUM_CACHE.get(x).put(y,count);
                    count++;
                }
            }
        }

        //每一个空白点查看4个方向看是否存在有效跳跃点


        //使用dfs深度遍历
        if (this.exec(exitsCount, sum)){
            System.out.println("有解");
        }else {
            System.out.println("无解");
        }

    }

    private boolean exec(int exitsCount,long sum) {
        if (exitsCount == 1){
            //证明是走到只剩1
            System.out.println(true);
            return true;
        }
        exitsCount--;
        boolean execFlag = false;
        for (int j = 0; j < BLANK_STACK.size(); j++) {
            Blank blank = BLANK_STACK.get(j);

            for (int i = 1; i < direction.length; i++) {

                int x = direction[i-1];
                int y = direction[i];

                int firstX = x + blank.x;
                int firstY = y + blank.y;
                if (this.exists(firstX,firstY)){
                    continue;
                }

                int secondX = x + firstX;
                int secondY = y + firstY;
                if (this.exists(secondX,secondY)){
                    continue;
                }

                //存在可以跳跃点，进行跳跃
                setPoint(firstX,firstY);
                setPoint(secondX,secondY);
                //并且继续
                Integer num1 = NUM_CACHE.get(firstX).get(firstY);
                Integer num2 = NUM_CACHE.get(secondX).get(secondY);

                boolean l1 = ((sum >> num1) & 1) == 1;
                long n_sum = sum;
                if (l1){
                    n_sum = sum - (1L << num1);
                }
                boolean l2 = ((sum >> num2) & 1)  == 1;
                if (l2){
                    n_sum = sum - (1L << num2);
                }
                if (CACHE.getOrDefault(n_sum,false)){
                    continue;
                }
                execFlag = exec(exitsCount, n_sum);
                CACHE.put(n_sum,execFlag);
                //去掉之前跳跃点
                delPoint(secondX,secondY);
                delPoint(firstX,firstY);
            }
        }

        return execFlag;
    }

    private boolean exists(int x, int y) {

        return x < 0 || y < 0 || x >= SIZE || y >= SIZE || BOARDS[x][y] != 1;
    }

    private void init() {
        setY(2);
        setY(3);
        setY(4);

        setX(2);
        setX(3);
        setX(4);
        setPoint(3,3);
    }


    static class Blank{
        int x;
        int y;

        public Blank(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
