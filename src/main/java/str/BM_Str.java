package str;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BM_Str {



    //注意此处right[]的构造
    public static void getRight(String pat, int[] right) {
        for (int j = 0; j < pat.length(); j++) {
            right[pat.charAt(j)] = j;
        }
    }

    public static int Search(String txt, String pat, int[] right) {
        int M = txt.length();
        int N = pat.length();
        int skip;
        for (int i = 0; i < M - N; i += skip) {
            skip = 0;
            for (int j = N - 1; j >= 0; j--) {
                if (pat.charAt(j) != txt.charAt(i + j)) {
                    skip = j - right[txt.charAt(i + j)];
                    if (skip < 1)
                        skip = 1;
                    break;
                }
            }
            if (skip == 0)
                return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        String txt = "bacbbcababacabababacaab";
        String pat = "ababcab";
        int[] right = new int[256];
        getRight(pat, right);
        System.out.println(Search(txt, pat, right));

//        test();

        System.out.println(bm(txt.toCharArray(),pat.toCharArray()));
    }


    public static void test(){
        String t = "bacbbcababacabababacaab";
        String p = "ababaca";

        int[] arr = computePrefixFunction(p.toCharArray());

        int n = t.length();
        int m = p.length();

        char[] charst = t.toCharArray();
        char[] charsp = p.toCharArray();

        Map<Character,Integer> map = new HashMap<>();

        for (int i = 0; i < m; i++) {
            map.put(charsp[i],i);
        }

        int seed;
        for (int j = 0; j < (n - m); j+=seed) {
            seed = 0;

            for (int i = m - 1; i >= 0; i--) {

                if (charst[i + j] != charsp[i]){
                    Integer num = map.get(charst[i + j]);
                    num = num == null ? 0 : num;
                    seed = i - num;

                    seed = m - arr[i];
                    //m - num - 1
                    if (seed < 1){
                        seed = 1;
                    }
                    break;
                }

            }
            if (seed == 0) {
                System.out.println(j);
                break;
            }



        }


    }

    private static int[] computePrefixFunction(char[] arr) {
        int[] res = new int[arr.length];

        int k = 0;
        for (int i = 1; i < arr.length; i++) {

            while (k > 0 && arr[k] != arr[i]){
                k = res[k];
            }

            if(arr[k] == arr[i]){
                k++;
            }

            res[i] = k;
        }



        return res;
    }


    public static int[] suffix(char[] p){

        int m = p.length;
        int[] suffix = new int[m];

        suffix[m-1] = m;

        for (int i = m-2; i >= 0; --i) {

            int q = i;
            while (q >= 0 && p[q] == p[m -1 -i + q]){
                q--;
            }
            suffix[i] = i - q;

        }
        return suffix;
    }

    public static int[] bmgs(char[] p){

        int m = p.length;
        int[] bmgs = new int[m];

        int[] suffix = suffix(p);

        Arrays.fill(bmgs,m);

        for (int i = m-1,j = 0; i >= 0; i--) {
            if (suffix[i] == i + 1){
                for (;j < m -1 -i; j++){
                    if (bmgs[j] == m){
                        bmgs[j] = m -1 - i;
                    }
                }
            }

        }

        for (int i = 0; i < m - 1; i++) {
            bmgs[m -1 -suffix[i]] = m -1 - i;
        }
        return bmgs;
    }

    public static int bm(char[] t,char[] p){

        int[] bmgs = bmgs(p);

        int j = 0;
        int n = t.length;
        int m = p.length;

        while (j <= n-m){

            int i = m -1;
            while (i >= 0 && p[i] == t[i + j]){
                i--;
            }

            if (i < 0){
                return j;
            }

            j += bmgs[i];

        }


        return -1;
    }
}
