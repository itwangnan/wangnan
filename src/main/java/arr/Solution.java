package arr;

import java.util.Scanner;
import java.util.regex.Matcher;

public class Solution{

    public static String check(String str){
        if (str == null || str.length() == 0){
            return "Wrong";
        }
        char c = str.charAt(0);
        short flag = 0;
        if(c >= 65 && c <= 90){
            for(int i = 1;i < str.length();i++){
                c = str.charAt(i);
                if (c >= 48 && c <= 57) {
                    flag |= 1;
                }else if((c >= 65 && c <= 90) || (c >= 97 && c <= 122)){
                    flag |= 2;
                }else{
                    return "Wrong";
                }
            }
            if (flag != 3) {
                return "Wrong";
            }

            return "Accept";
        }

        return "Wrong";

    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int cnt = sc.nextInt();
        sc.nextLine();
        while(cnt > 0){
            System.out.println(check(sc.nextLine()));
            cnt--;
        }
    }

    static String REG = "^([a-zA-Z])([a-zA-z0-9]*)([0-9]+)([a-zA-z0-9]*)$";


    public static void check2(String str){
        Scanner sc = new Scanner(System.in);
        // 循环次数
        int t = sc.nextInt();
        sc.nextLine();
        while (t-- > 0) {
            String s = sc.nextLine();
            // 检查并输出
            System.out.println(s.matches(REG) ? "Accept" : "Wrong");
        }
    }

}
