package str;

public class RK_Str {

    public static void main(String[] args) {
        String t = "bacbabababacabababacaab";

        String p = "ababaca";

        long d = 10;
        long q = Long.MAX_VALUE;

        Integer tCount = t.length();
        Integer pCount = p.length();

        Double aDouble = Math.pow(tCount.doubleValue(), Integer.valueOf(pCount - 1).doubleValue()) % q;
        long h = aDouble.intValue();


        char[] tChars = t.toCharArray();
        char[] pChars = p.toCharArray();



        long pSum = 0;
        long tSum = 0;


        for (int i = 0; i < pCount; i++) {
            pSum = (pSum*d + pChars[i]) % q;
            tSum = (tSum*d + tChars[i]) % q;
        }
//        long v = (long)((d * (tSum - tChars[0])* Math.pow(d, pCount)) + tChars[0]) % q;
//        long v = (long) ((tSum * d + 98 - Math.pow(d, pCount) * 98) % Long.MAX_VALUE);
//        System.out.println(v);

        for (int i = 0; i <= (tCount - pCount); i++) {

            if (pSum == tSum && p.equals(t.substring(i,i+pCount))){
                System.out.println(t.substring(i,i+pCount));
            }
            if(i < (tCount - pCount)){
//                tSum = (((d * (tSum-tChars[i]) * h) + tChars[i+pCount]) % q);
                tSum = (long) ((tSum * d + tChars[i+pCount] - Math.pow(d, pCount) * tChars[i]) % q);
            }



        }

        long hash_p = hash(p,d);//p的hash值
        long[] hashOfS = hash(t, p.length(),d);
        for (int i = 0; i < hashOfS.length; i++) {
            if (hashOfS[i] == hash_p) {
                System.out.println("match:" + t.substring(i,i+p.length()));
            }
        }





    }

    private static long[] hash(String s, int n,long seed) {
        long[] res = new long[s.length() - n + 1];
        //前n个字符的hash
        res[0] = hash(s.substring(0, n),seed);
        for (int i = n; i < s.length(); i++) {
            char newChar = s.charAt(i);  // 新增的字符
            char oldchar = s.charAt(i - n);  // 前n字符的第一字符
            //前n个字符的hash*seed-前n字符的第一字符*seed的n次方
            long v = (long) ((res[i - n] * seed + newChar - Math.pow(seed, n) * oldchar) % Long.MAX_VALUE);
            res[i - n + 1] = v;
        }
        return res;
    }

    static long hash(String str,long seed) {
        long h = 0;
        for (int i = 0; i != str.length(); ++i) {
            h = seed * h + str.charAt(i);
        }
        return h % Long.MAX_VALUE;
    }


}
