package my_t;

public class Solution {

    public static void main(String[] args) {
        System.out.println(test("006005004"));
//        System.out.println(splitString("99999998999799969995"));
    }

    public static boolean test(String s){

        int n = s.length();
        int start = 0;

        char[] arr = s.toCharArray();
        //枚举第一个子字符串对应的初始值
        //第一个子字符串不能包含整个字符串

        for (int i = 0; i < n - 1; i++) {
            start = 10 * start + Integer.valueOf(arr[i]);

            int pval = start;
            int cval = 0;
            int cidx = i + 1;

            f :for (int j = i + 1; j < n; j++) {

                if (pval == 1){
                    // 如果上一个值为 1，那么剩余字符串对应的数值只能为 0

                    for (int k = cidx; k < n; k++) {
                        if (k != '0'){
                             break f;
                        }
                    }

                    return true;

                }
                cval = 10 * cval + Integer.valueOf(arr[j]);
                if (cval > pval - 1){
                    // 不符合要求，提前结束
                    break;
                }else if (cval == pval - 1){
                    if (j + 1 == n){
                        // 已经遍历到末尾
                        return true;
                    }
                    pval = cval;
                    cval = 0;
                    cidx = j + 1;
                }

            }


        }
        return false;

    }




    public static boolean splitString(String s) {
        // 去除前缀 0
        while (s.charAt(0) == '0' && s.length() > 1) s = s.substring(1);
        if(s.length() <= 1) return false;

        // 确定第一个数字， 执行dfs
        char[] chars = s.toCharArray();
        String num = "";
        // 小于 18 防止 long 越界
        for (int i = 0; i < chars.length && i < 18; i++) {
            num += chars[i];
            long first = Long.parseLong(num);
            if (dfs(s.substring(i + 1), first - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在 s 中找到 前缀为 preNum
     */
    public static boolean dfs(String s, long preNum) {
        // 终止条件
        if(s.length() == 0) return false;
        // 补丁 * 1
        if (preNum == 0) {
            return Long.parseLong(s) == 0;
        }
        // 寻找比 pre 小 1 的数字
        char[] chars = s.toCharArray();
        String num = "";
            for (int i = 0; i < chars.length; i++) {
            num += chars[i];
            long first = Long.parseLong(num);
            // 提前结束
            if (first > preNum) return false;
            if (first == preNum) {
                if (s.length() > num.length()) {
                    return dfs(s.substring(i + 1), first - 1);
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
