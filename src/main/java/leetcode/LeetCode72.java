package leetcode;

import org.junit.Test;

import java.util.Arrays;

public class LeetCode72 {

    @Test
    public void test() {
        String word1 = "horse", word2 = "ros";
        System.out.println(minDistance(word1, word2));
        word1 = "intention"; word2 = "execution";
        System.out.println(minDistance(word1, word2));
        word1 = "sea"; word2 = "eat";
        System.out.println(minDistance(word1, word2));
    }

    public int minDistance(String word1, String word2) {
        char[] c1 = word1.toCharArray();
        char[] c2 = word2.toCharArray();

//        int[][] dp = new int[c1.length + 1][c2.length + 1];
//        //以下初始化 说明当 dp[i][0]和dp[0][j]都只要对应i和j次操作就完全一致
//        for (int i = 0; i <= c1.length; i++) {
//            dp[i][0] = i;
//        }
//        for (int j = 0; j <= c2.length; j++) {
//            dp[0][j] = j;
//        }
//
//        for (int i = 1; i <= c1.length; i++) {
//            for (int j = 1; j <= c2.length; j++) {
//                if (c1[i-1] == c2[j-1] ) {
//                    //相同不用直接跳过
//                    dp[i][j] = dp[i-1][j-1];
//                }else {
////                    //插入相当于是
////                    dp[i][j] = dp[i][j-1] + 1;
////                    //删除一个字符
////                    dp[i][j] = dp[i-1][j] + 1;
////                    //替换一个字符
////                    dp[i][j] = dp[i-1][j-1] +1;
//                    dp[i][j] = Math.min(Math.min(dp[i][j-1],dp[i-1][j]),dp[i-1][j-1]) + 1;
//                }
//            }
//        }


        int[] dp = new int[c2.length + 1];
        //以下初始化 说明当 dp[i][0]和dp[0][j]都只要对应i和j次操作就完全一致
        for (int j = 0; j <= c2.length; j++) {
            dp[j] = j;
        }

        for (int i = 1; i <= c1.length; i++) {
            int flag = dp[0];
            //为什么需要dp[0] = i，因为每一轮初始化dp[i][0] = i,需要维护
            dp[0] = i;
            for (int j = 1; j <= c2.length; j++) {
                int temp = dp[j];
                if (c1[i-1] == c2[j-1] ) {
                    //相同不用直接跳过
                    dp[j] = flag;
                }else {
//                    //插入相当于是
//                    dp[i][j] = dp[i][j-1] + 1;
//                    //删除一个字符
//                    dp[i][j] = dp[i-1][j] + 1;
//                    //替换一个字符
//                    dp[i][j] = dp[i-1][j-1] +1;
                    dp[j] = Math.min(Math.min(dp[j-1],dp[j]),flag) + 1;
                }
                flag = temp;
            }
        }

        return dp[c2.length];
    }
}
