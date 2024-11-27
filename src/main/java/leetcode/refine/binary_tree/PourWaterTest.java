package leetcode.refine.binary_tree;

import org.junit.Test;

public class PourWaterTest {


    @Test
    public void test(){

        //扩展欧几里德算法 非递归特解
        int a = 7;
        int b = 4;
        int r = 1;

        int gcd = this.gcd(a, b);
        if (r % gcd != 0){
            System.out.println("无效");
            return;
        }
        int[] xy = this.exgcd(a, b,r);
        //装

        int x1 = xy[0];
        int x2 = xy[1];

        int num1 = 0;
        int num2 = 0;
        int newA = 0;
        int newB = 0;

        if (x1 > 0){
            num1 = x1;
            newA = a;
            num2 = x2;
            newB = b;
        }else {
            num2 = x1;
            newB = a;
            num1 = x2;
            newA = b;
        }


    }


    public int gcd(int a, int b) {

        if (a % b == 0){
            return b;
        }

        return gcd(b,a % b);
    }


    private int[] exgcd(int a, int b, int c) {
        int[] res = new int[2];

        int x1 = 0,y1 = 1;
        int x2 = 1,y2 = 0;

        int x = 0, y = 1;

        int q = a / b;
        int r = a % b;

        while (b > c){

            x = x2 - q * x1;
            y = y2 - q * y1;

            x2 = x1;
            y2 = y1;

            x1 = x;
            y1 = y;

            a = b;
            b = r;

            q = a / b;
            r = a % b;
        }
        res[0] = x1;
        res[1] = y1;

        return res;
    }
}
