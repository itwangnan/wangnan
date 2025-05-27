package josephus;

import position.PositionOp;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 约瑟夫问题
 */
public class Josephus {

    public static void main(String[] args) {
        int start = 0;
        int n = 6;
        int step = 2;
        int index3 = getIndex3(n, step, start);
        System.out.println(index3);
        int index4 = getIndex4(n, step, start);
        System.out.println(index4);
    }

    //固定解
    private static int getIndex(int len) {
        if (len == 0){
            return 0;
        }

        //先看是否是2的幂
        if (power2(len)){
            //2的幂直接返回0
            return 0;
        }

        return 2 *  (len - PositionOp.flp(len));
    }

    /**
     * f(n,k) = (f(n-1,k) + k) % n
     * 自顶向下递归形式求最后索引
     */
    private static int getIndex2(int n,int k,int r) {
        if (n == 0 || n == 1){
            return r;
        }
        return (getIndex2(n - 1, k, r) + k) % n;
    }

    /**
     * 自底向上循环求最后索引
     */
    private static int getIndex3(int n,int k,int r) {

        int res = 0;
        //该方法多个% 1直接就是0，所以无论r是多少都是从0开始
        for (int i = 1; i <= n; ++i) res = (res + k) % i;

        //只能在最后处理
        return (res + r) % n;
    }

    /**
     * 该方法是getIndex3优化后版本
     */
    private static int getIndex4(int n,int k,int r) {
        if (n <= 1){
            return 0;
        }

        for (int i = 2; i <= n; i++) r = (r + k) % i;

        return r;
    }

    /**
     * 求逆约瑟夫问题，给出起始索引，结束索引，总长度，求k步长
     * 该方法原本是为了求解析解，但是发现求 f(n,k) = (f(n-1,k) + k) % n 后得k = ？
     * 因为取模的存在导致会有类似周期循环的情况，无法求解
     */
    private static BigDecimal getK1(int start,int end,int n) {

//        BigDecimal b2 = BigDecimal.valueOf(end - start).divide(BigDecimal.valueOf(n - 1), 4, RoundingMode.HALF_UP);
//
//        BigDecimal b1 = BigDecimal.valueOf(2).add(BigDecimal.valueOf(n - 2).divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP));


        return  BigDecimal.valueOf(end - start).add(equalDif(n,1,1).subtract(BigDecimal.ONE)).divide(BigDecimal.valueOf(n-1),4,RoundingMode.HALF_UP);

    }

    /**
     * 该方法是如果每次都是从固定的x下标开始数，则可以求n! or 最小公倍数
     * https://blog.csdn.net/Mr_Fmnwon/article/details/129309005
     */
    private static int getK2(int start,int end,int n) {
        int lcm = getLcm(1, 2);
        for (int i = 3; i <= n; i++) {
            lcm = getLcm(lcm,i);
        }
        return lcm;
    }


    /**
     * 该方法是通过循环n次去匹配结果是否是end索引
     */
    private static int getK3(int start,int end,int n) {
        int res = 0;
        for(int i=1; i<=n; i++) {

            int y = getIndex3(n, i, start);

            if(y == end) {
                res = i;
                break ;
            }
        }
        return res;
    }

    /**
     * 等差求和
     */
    private static BigDecimal equalDif(int n,int d,int a1) {
        BigDecimal b1 = new BigDecimal(n * a1);
        BigDecimal b2 = new BigDecimal(n * (n-1) * d).divide(BigDecimal.valueOf(2),4, RoundingMode.HALF_UP);
        return b1.add(b2);
    }


    public static boolean power2(int num){
        //2的幂只有一个1
        return (num & (num - 1)) == 0;
    }


    //使用欧几里得算法求解数m和数n最大公约数
    public static int getGcd(int m, int n){
        while(n > 0){
            int temp = m % n;
            m = n;
            n = temp;
        }
        return m;
    }

    //求解数m和n和最小公倍数
    public static int getLcm(int m, int n){
        int gcd = getGcd(m,n);
        int result = m*n / gcd;
        return result;
    }

}
