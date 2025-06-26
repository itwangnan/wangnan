package leetcode;

import org.junit.Test;

public class LeetCode1143 {

    @Test
    public void test() {
        String text1 = "abcde",text2 = "ace";
        System.out.println(longestCommonSubsequence(text1, text2));
        text1 = "abc";text2 = "abc";
        System.out.println(longestCommonSubsequence(text1, text2));
        text1 = "abc";text2 = "def";
        System.out.println(longestCommonSubsequence(text1, text2));
        text1 = "ezupkr";text2 = "ubmrapg";
        System.out.println(longestCommonSubsequence(text1, text2));
    }

    public int longestCommonSubsequence(String text1, String text2) {

        //选或不选
        char[] c1 = text1.toCharArray();
        char[] c2 = text2.toCharArray();

//        int[][] dp = new int[text1.length() + 1][text2.length() + 1];
//        // dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-1]+1)
//        for (int i = 1; i <= c1.length; i++) {
//            for (int j = 1; j <= c2.length; j++) {
//                if (c1[i-1] == c2[j-1]) {
//                    //才能都选
//                    dp[i][j] = dp[i-1][j-1] + 1;
//                }else {
//                    //可以选择一个
//                    dp[i][j] = Math.max(dp[i-1][j],dp[i][j-1]);
//                }
//            }
//        }

        //空间优化
        int[] dp = new int[text2.length() + 1];
        // dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-1]+1)
        for (int i = 1; i <= c1.length; i++) {
            //dp[j-1]
            int flag = dp[0];
            for (int j = 1; j <= c2.length; j++) {
                int temp = dp[j];
                if (c1[i-1] == c2[j-1]) {
                    //才能都选
                    dp[j] = flag + 1;
                }else {
                    //可以选择一个
                    dp[j] = Math.max(dp[j],dp[j-1]);
                }
                flag = temp;
            }

        }



        return dp[c2.length];
    }
}
