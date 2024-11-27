package cf;

import org.junit.Test;

import java.util.Random;
import java.util.Scanner;

/**
 * https://www.luogu.com/discuss/402723
 */
public class CF1624F {

    @Test
    public void test(){
        int count = 1000;

        while (count > 0){
            Random rd = new Random();
            int n = rd.nextInt(999) + 2;

            int x = rd.nextInt(n - 1) + 1;
            int flag_x = x;

            int l = 1;
            int r = n - 1;

            int preM = 0;
            while (l <= r){

                int m = l + ((r - l) >>> 1);

                //input = d
                int input = (m - preM + n) % n;

                int pre_f = x / n;

                x += input;
                preM = m;

                int f = x / n;

                if (f == pre_f){

                    l = m + 1;

                }else {

                    r = m - 1;
                }

            }

            if (flag_x != (n - l)){
                System.out.println(flag_x);
                System.out.println(n - l);
                break;
            }
            count--;
        }

    }

    @Test
    public void test2(){
        Random rd = new Random();

        int n = 10;

        int x = 4;

        int flag_x = x;

        int l = 1;
//        x -= l;
        int r = n - 1;

        while (l <= r){

            int m = l + ((r - l) >>> 1);
//            int input = m;

            int pre_f = x / n;

            x += m;

            int f = x / n;

            if (f == pre_f){


                r = m - 1;
            }else {


                l = m + 1;

            }

        }
        System.out.println(flag_x);
        System.out.println(l);
    }

    // 定义 add 方法
    public static int add(int x) {
        System.out.println("+ " + x);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int l = 1, r = n - 1, tot = 0, last = 0,ans = 1;

        while (l <= r) {
            int x = (l + r + 1) >>> 1;
            int c = n - (tot + x) % n;

            System.out.println("+ " + c);
//            System.out.flush();

            int res = scanner.nextInt();

            if (res > last) {
                l = x + 1;
                ans = x;
            } else {
                r = x - 1;
            }

            tot += c;
            last = res;
        }

        System.out.println("! " + (ans + l));
//        System.out.flush();

        scanner.close();
    }
}
