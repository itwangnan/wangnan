package cf;

import org.junit.Test;

import java.util.Random;
import java.util.Scanner;

/**
 * https://www.luogu.com.cn/problem/CF1999G1
 */
public class CF1999G1 {

    public static void main(String[] args) {
        Random rd = new Random();
        int x = rd.nextInt(999);


        int flag = 10;
        Scanner input= new Scanner(System.in);
        while (flag > 0){
            String next = input.nextLine();
            String[] arr = next.split(" ");
            if (arr.length == 2){
                System.out.println(x +"_" + arr[1]);
                System.out.println();
                break;
            }
            Integer y1 = Integer.valueOf(arr[1]);
            Integer y2 = Integer.valueOf(arr[2]);
            if (y1 >= x){
                y1++;
            }
            if (y2 >= x){
                y2++;
            }
            System.out.println(y1 * y2);
            System.out.println();
            flag--;
        }
    }

    @Test
    public void test(){

        Random rd = new Random();
        int x = rd.nextInt(999);

        int l = 1;
        int r = 1001;

        int count = 0;
        while (l <= r){

            int m = (r - l) / 3;
            int lm = l + m;
            int rm = l + 2*m;

//            int input = this.input(x, m, m);
            if(lm >= x && rm >= x){
                //证明在左边
                r = lm - 1;

            } else if (lm < x && rm < x){
                l = rm + 1;

            }else if(lm < x){

                r = rm - 1;

                l = lm + 1;

            }else {
                System.out.println();
            }


            count++;
        }
        System.out.println(count);
        System.out.println(l);
        System.out.println(x);
    }

    private int input(int x,int l, int r) {
        int res = 0;
        if (l >= x){
            res += 1;
        }
        if (r >= x){
            res += 2;
        }
        return res;
    }

}
