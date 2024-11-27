package str;

/**
 * KMP算法扩展 Z算法
 */
public class ZPattern {


    /**
     * 获取z数组
     * @param p 模式串
     */
    public int[] getZArr(String p){

        char[] arr = p.toCharArray();

        /**
         * Z算法   s[0 ... x] = s[i ... i+x]    可见是真前后缀border
         * KMP则是 s[0 ... x] = s[i-x ... i]    可见是LCP最长前缀border
         */
        int[] z = new int[arr.length];

        z[0] = 0;

        for (int i = 1; i < arr.length; i++) {
            //获取前者的z信息
            //如果 s[0 ... x] = s[i ... i+x] 最大长度是2，则比较s[0 ... x+1] s[i+1 ... i+x+1]
            int j = z[i - 1];



        }

        return null;

    }



}
