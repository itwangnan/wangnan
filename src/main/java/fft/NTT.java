package fft;

import java.util.Arrays;

public class NTT {

    //素数 998244353 = 2^23*119+1
    static long M = 998244353;

    //P的原根
    static long g = 3;

    //3 mod 998244353的逆元
    static long GI = 332748118;


    public static void main(String[] args) {
        int n = 8;
        long[] c1 = new long[n];
        c1[0] = 1;
        c1[1] = 2;
        c1[2] = 1;
        c1[3] = 0;
        c1[4] = 0;
        c1[5] = 0;
        c1[6] = 0;
        c1[7] = 0;

        long[] c2 = new long[n];
        c2[0] = 3;
        c2[1] = 4;
        c2[2] = 0;
        c2[3] = 0;
        c2[4] = 0;
        c2[5] = 0;
        c2[6] = 0;
        c2[7] = 0;

        long[] a1 = NTT.ntt(c1, -1);
        long[] a2 = NTT.ntt(c2, -1);

        long[] a3 = new long[n];


        for (int i = 0; i < a1.length; i++) {
            if (a2.length <= i){
                a3[i] = a1[i];
            }else {
                a3[i] = a1[i] * a2[i];
            }

        }
        System.out.println(Arrays.toString(a3));
        long[] res = ntt(a3, 1);

        System.out.println(Arrays.toString(res));
    }

    private static long[] bitReverseCopy(long[] x) {
        long[] A = new long[x.length];

        for (int k = 0; k <= x.length - 1; k++) {
            int rev = rev(k, x.length - 1);
            A[rev] = x[k];
        }

        return A;
    }

    private static int rev(long k, int flag) {
        int length = Integer.toBinaryString(flag).length() - 1;
        int a = 0;
        for (int i = 0; i <= length; i++) {
            a += (k >> (length - i) & 1) << i;
        }

        return a;
    }

    public static long[] ntt(long[] arr, int opt) {
        int n = arr.length;

        long[] A = bitReverseCopy(arr);

        // base case
        if (n == 1) {
            return new long[]{arr[0]};
        }

        // radix 2 Cooley-Tukey FFT
        if ((n & (n-1)) != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }


        //层数
        for (int s = 1; s <= FFT.log(2, n); s++) {

            int m = 1 << s;

            //如果是NTT就是用原根，否则使用原根的逆元 原根*逆元 % M = 1
            long a = qpow(opt == -1 ? g : GI, (M - 1) / m);

            //下个
            for (int k = 0; k < n; k += m) {

                long w = 1;


                for (int j = 0; j < m / 2; j++) {

                    //A[k + j + m / 2]=多加个步长
                    long t = (A[k + j + m / 2] * (w)) % M;
                    //A[k + j]
                    long u = A[k + j];
                    //偶
                    A[k + j] = (u + t) % M;
                    //奇 防止负数+M
                    A[k + j + m / 2] = (u - t + M) % M;

                    w = (w * a) % M;

                }


            }

        }

//        if (opt == 1) {
//            for (int i = 0; i < A.length; i++) {
//                A[i] = A[i] / A.length;
//            }
//        }
        if (opt == 1) {
            //n mod M-2 的逆元
            long inv = qpow(n, M - 2);

            for (int i = 0; i < n; i++) {
                A[i] =( A[i] * inv) % M;
            }
        }


        return A;
    }

    public static int myEuclid(int e, int modValue) {  //扩展的欧几里得除法
        int x = e;
        int num = e;
        int d = 1;

        while ((num % modValue) != 1) {
            d++;
            num += e;
        }
        //System.out.println(d);
        return d;
    }

    public static long[] ntt2(long[] a, int type) {
        int n = a.length;

        a = bitReverseCopy(a);
        for (int i = 1; i < n; i <<= 1) {//在这之前NTT与FFT无异
            long gn = qpow(type == -1 ? g : GI, (M - 1) / (i << 1));//单位原根g_n
            for (int j = 0; j < n; j += (i << 1)) {//枚举具体区间，j也就是区间右端点
                long g0 = 1;

                for (int k = 0; k < i; ++k, g0 = (g0 * gn) % M) {//合并，记得要取模
                    long x = a[j + k], y = (g0 * a[i + j + k]) % M;
                    a[j + k] = (x + y) % M;
                    a[i + j + k] = (x - y + M) % M;
                }
            }
        }

        if (type == 1) {
            long inv = qpow(n, M - 2);

            for (int i = 0; i < n; i++) {
                a[i] =( a[i] * inv) % M;
            }
        }

        return a;
    }

    //这是获取a mod b的逆元 ax = 1(mod b)
    public static long qpow(long a, long b) {
        long res = 1;

        while (b != 0) {
            if ((b & 1) == 1) {
                res = (res * a) % M;

            }
            a = (a * a) % M;
            b >>= 1;
        }
        //System.out.println(d);
        return res;
    }


    public static int factor(int a, int b) {
        int e = a;

        for (int i = 1; i <= e; i++) {    //辗转相除法

            int c = a > b ? a : b;      //两个数字的大值
            int d = a < b ? a : b;      //两个数字的小值

            a = c % d;
            b = d;

            if (a == 0) {
                //  System.out.println("最大公因数为："+d);
                return d;                //结束
            }

        }
        return 0;

    }


}
