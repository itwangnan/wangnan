package str;

import java.util.Arrays;

public class KMP_Str {

    public static void main(String[] args) {
        //KMP算法

        String t = "bacbabababacabababacaab";
        //匹配模式
        String p = "ababaca";


        //首先创建p前缀表

        int[] arr = computePrefixFunction(p.toCharArray());

        int n = t.length();
        int m = p.length();
        int k = 0;

        char[] chars = t.toCharArray();
        char[] charp = p.toCharArray();
        for (int q = 0; q < n; q++) {


            while (k > 0 && charp[k] != chars[q]){
                k = arr[k-1];
            }


            if (charp[k] == chars[q]){
                k++;
            }

            if (k == m){
                System.out.println(t.substring(q-m+1,q+1));
                k = arr[k-1];
            }


        }

        System.out.println(Arrays.toString(arr));



    }

    private static int[] computePrefixFunction(char[] p) {
        int m = p.length;
        int[] res = new int[m];
        int k = 0;

        //ababaca
        //开始 k=0,q=1 ,
        for (int q = 1; q < m; q++) {
            //如果q和k不相同，将k向前移动, k前一个res下的存值
            while (k > 0 && p[k] != p[q]){
                k = res[k-1];
            }
            if (p[k] == p[q]){
                k++;

            }
            res[q] = k;
        }


        return res;
    }


}
