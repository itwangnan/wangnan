package leetcode;

import org.junit.Test;

import java.util.*;

//方法一：跳跃游戏 + 字符串哈希 + 二分
public class LeetCode3292_02 {

    @Test
    public void test(){

        System.out.println(minValidStrings(new String[]{"abc","aaaaa","bcdef"},"aabcdabc"));
        System.out.println(minValidStrings(new String[]{"abababab","ab"},"ababaababa"));
        System.out.println(minValidStrings(new String[]{"abcdef"},"xyz"));
        System.out.println(minValidStrings(new String[]{"a","babc"},"aacab"));
    }


    //质数
    private static final int MOD = 1_070_777_777;


    public int minValidStrings(String[] words, String target) {
        char[] t = target.toCharArray();
        int n = target.length();

        // 多项式字符串哈希（方便计算子串哈希值）
        // 哈希函数 hash(s) = s[0] * base^(n-1) + s[1] * base^(n-2) + ... + s[n-2] * base + s[n-1]
        final int BASE = (int) 8e8 + new Random().nextInt((int) 1e8); // 随机 base，防止 hack
        int[] powBase = new int[n + 1]; // powBase[i] = base^i
        int[] preHash = new int[n + 1]; // 前缀哈希值 preHash[i] = hash(target[0] 到 target[i-1])
        powBase[0] = 1;
        for (int i = 0; i < n; i++) {
            powBase[i + 1] = (int) (((long) powBase[i] * BASE) % MOD);
            preHash[i + 1] = (int) (((long) preHash[i] * BASE + t[i]) % MOD); // 秦九韶算法计算多项式哈希
        }

        int maxLen = 0;
        for (String w : words) {
            maxLen = Math.max(maxLen, w.length());
        }
        Set<Integer>[] sets = new HashSet[maxLen];
        Arrays.setAll(sets, i -> new HashSet<>());
        for (String w : words) {
            long h = 0;
            for (int j = 0; j < w.length(); j++) {
                h = (h * BASE + w.charAt(j)) % MOD;
                sets[j].add((int) h); // 注意 j 从 0 开始
            }
        }

        int ans = 0;
        int curR = 0; // 已建造的桥的右端点
        int nxtR = 0; // 下一座桥的右端点的最大值
        for (int i = 0; i < n; i++) {
            int sz = calcSz(i, preHash, powBase, sets);
            nxtR = Math.max(nxtR, i + sz);
            if (i == curR) { // 到达已建造的桥的右端点
                if (i == nxtR) { // 无论怎么造桥，都无法从 i 到 i+1
                    return -1;
                }
                curR = nxtR; // 造一座桥
                ans++;
            }
        }
        return ans;
    }

    private int calcSz(int i, int[] preHash, int[] powBase, Set<Integer>[] sets) {
        // 开区间二分，left 一定满足要求，right 一定不满足要求
        int left = 0;
        //注意left和right 其实算的是 i + ？ 的距离
        int right = Math.min(preHash.length - 1 - i, sets.length) + 1;
        //以mid - 1为标准，(0,n+1)对应mid - 1为(-1，n)
        while (left + 1 < right) {
            int mid = (left + right) >>> 1;
            //i + mid = r,i = left, mid = r - l + 1
            long subHash = (((long) preHash[i + mid] - (long) preHash[i] * powBase[mid]) % MOD + MOD) % MOD;
            // 长度 - 1
            if (sets[mid - 1].contains((int) subHash)) {
                left = mid;
            } else {
                right = mid;
            }
        }
        //返回的自然是i能走多远
        return left;
    }





}
