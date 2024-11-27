package leetcode.refine.binary_tree;

public class Tcc {

    public static int exgcdWhile(int a, int b, int[] xy) {
        int x2 = 1, y2 = 0; // 初始值 x_0 和 y_0
        int x1 = 0, y1 = 1; // 初始值 x_1 和 y_1
        int x = 0, y = 1;
        int r = a % b;
        int q = a / b;

        while (r != 0) {
            x = x2 - q * x1; // 递推公式 x_i = x_{i-2} - q_i * x_{i-1}
            y = y2 - q * y1; // 递推公式 y_i = y_{i-2} - q_i * y_{i-1}

            x2 = x1;
            y2 = y1;

            x1 = x;
            y1 = y;

            a = b;
            b = r;

            r = a % b;
            q = a / b;
        }

        xy[0] = x1;
        xy[1] = y1;
        return b;
    }

    public static void main(String[] args) {
        int a = 252;
        int b = 198;
        int[] xy = new int[2];
        int gcd = exgcd(a, b, xy);
        System.out.println("GCD: " + gcd);
        System.out.println("x: " + xy[0]);
        System.out.println("y: " + xy[1]);

        int i = xy[0] * a + xy[1] * b;
        System.out.println(i);

        int x = xy[0] + b;
        int y = xy[1] - a;
        System.out.println(x * a + y * b);
    }

    public static int exgcd(int a, int b, int[] xy) {


//        x1;
//        x2;
        return 1;
    }

    public static int gcd(int a, int b) {

        if (a % b == 0){
            return b;
        }

        return gcd(b,a % b);
    }

    public static int gcdWhile(int a, int b) {

        while (b > 0){

            int x = b;
            b = a % b;
            a = x;
        }

        return a;
    }
}


