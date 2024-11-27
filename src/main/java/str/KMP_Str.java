package str;

import java.util.ArrayList;
import java.util.List;

import static str.ac.AcTest.readFile;

/**
 * O(m+n)
 */
public class KMP_Str {

    public static void main(String[] args) throws Exception {
        List<String> patternList = new ArrayList<>();
        List<String> textList = new ArrayList<>();

        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/图号2.csv", patternList::add);
        readFile("/Users/wangnan/IdeaProjects/wangnan/src/main/resources/t3.csv",x -> textList.add(x.split(",")[1]));
        long start = System.currentTimeMillis();

        int i = 0;

        for (String p : patternList) {
            int[] next = computePrefixFunction(p.toCharArray());
            i++;
            for (String t : textList) {
                kmp(t, p,next);
            }
            System.out.println(i);
        }


        long end = System.currentTimeMillis();
        System.err.println("KMP结束时间"+(end-start));
//        System.out.println(computePrefixFunction("ababc".toCharArray()));
    }


    public static boolean kmp(String t, String p,int[] next) {
        if (t.length() < p.length() || p.length() < 1){
            return false;
        }

        //首先创建p前缀表
        int m = t.length();
        int n = p.length();

        for(int i=0, j=0; i < m; i++){

            while(j>0 && t.charAt(i) != p.charAt(j)){
                j = next[j-1];
            }

            if(t.charAt(i) == p.charAt(j)){
                j++;
            }

            if(j == n){
                return true;
//                System.out.println(i);
//                j = next[j-1];
            }
        }


        return false;
    }


    public static boolean kmp(String t, String p) {
        if (t.length() < p.length() || p.length() < 1){
            return false;
        }

        //首先创建p前缀表
        int[] next = computePrefixFunction(p.toCharArray());

        kmp(t,p,next);

        return false;
    }


    public static int[] computePrefixFunction(char[] p) {
        int n = p.length;
        //next数组
        int[] next = new int[n];
        //默认
        next[0] = 0;


        for (int i = 1; i < n; i++) {
            //赋予next 1..i
            int j = next[i-1];

            //找最大border在比较
            while (j > 0 && p[i] != p[j]){

                j = next[j-1];
            }

            //相同则在之前border上+1
            if(p[i] == p[j]){
                j++;
            }

            next[i] = j;

        }

        return next;
    }


}
