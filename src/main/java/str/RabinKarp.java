package str;

/**
 * 滚动哈希法
 * 对目标字符串按d进制求值，mod h 取余作为其hash
 * 对源串，一次求出m个字符的hash，保存在数组中(滚动计算)
 * 匹配时，只需对比目标串的hash值和预存的源串的hash值表
 */
public class RabinKarp {

    public static void main(String[] args) {
        String t = "bacbabababacabababacaab";

        String p = "ababaca";
        match(p, t);

    }

    static void match(String p,String s){
        long hash_p = hash(p);//p的hash值
        long[] hashOfS = hash(s, p.length());
        for (int i = 0; i < hashOfS.length; i++) {
            if (hashOfS[i] == hash_p) {
                System.out.println("match:" + s.substring(i,i+p.length()));
            }
        }
    }

    final static long seed = 31;

    /**
     * 滚动哈希
     * @param s 源串
     * @param n 子串的长度
     * @return
     */
    private static long[] hash(String s, int n) {
        long[] res = new long[s.length() - n + 1];
        //前n个字符的hash
        res[0] = hash(s.substring(0, n));
        for (int i = n; i < s.length(); i++) {
            char newChar = s.charAt(i);  // 新增的字符
            char oldchar = s.charAt(i - n);  // 前n字符的第一字符
            //前n个字符的hash*seed-前n字符的第一字符*seed的n次方
            long v = (long) ((res[i - n] * seed + newChar - Math.pow(seed, n) * oldchar) % Long.MAX_VALUE);
            res[i - n + 1] = v;
        }
        return res;
    }

    static long hash(String str) {
        long h = 0;
        for (int i = 0; i != str.length(); ++i) {
            h = seed * h + str.charAt(i);
        }
        return h % Long.MAX_VALUE;
    }
}