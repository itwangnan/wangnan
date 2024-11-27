package leetcode;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/find-the-number-of-good-pairs-ii/solutions/2790631/tong-ji-yin-zi-ge-shu-pythonjavacgo-by-e-bl3o/
 */
public class LeetCode3164 {


    @Test
    public void test(){


        System.out.println(numberOfPairs(new int[]{1,3,4},new int[]{1,3,4}, 1));
        System.out.println(numberOfPairs(new int[]{1,2,4,12},new int[]{2,4}, 3));


    }

    /**
     * 时间复杂度 n + m + (U/k)logm
     * @param nums1
     * @param nums2
     * @param k
     * @return
     */
    public long numberOfPairs(int[] nums1, int[] nums2, int k) {

        int max1 = 0;
        for (int num1 : nums1) {
            if (num1 % k == 0){
                max1 = Math.max(max1, num1 / k);
            }
        }
        if (max1 == 0) {
            return 0;
        }
        int[] cnt1s = new int[max1 + 1];
        for (int num1 : nums1) {
            if (num1 % k == 0){
                cnt1s[num1 / k]++;
            }
        }


        int max2 = 0;
        for (int num2 : nums2) {
            max2 = Math.max(max2, num2);
        }
        int[] cnt2s = new int[max2 + 1];
        for (int num2 : nums2) {
            cnt2s[num2]++;
        }


        long count = 0;
        for (int i = 1; i <= max2; i++) {
            int cnt2 = cnt2s[i];
            if (cnt2 == 0) {
                continue;
            }
            long sum = 0;
            //a 的倍数
//            for (int j = 1; j * i <= max1; j++) {
//                sum += cnt1s[j * i];
//            }

            // += i 比 上面的比较更快
            for (int j = i; j <= max1; j += i) {
                sum += cnt1s[j];
            }



            count += (cnt2 * sum);
        }

        return count;
    }

    /**
     * 找因子，
     * 时间复杂度 O (n/k * 根号U/k + m)
     * 空间复杂度 O(U/k) U/k是最大的数，就算[1...U/k]全是因子也就U/k个值
     * @param nums1
     * @param nums2
     * @param k
     * @return
     */
    public long numberOfPairs2(int[] nums1, int[] nums2, int k) {

        Map<Integer,Integer> cnt = new HashMap<>();

        for (int num1 : nums1) {

            if (num1 % k > 0){
                continue;
            }

            num1 /= k;

            //找因子
            for (int q = 1; q * q <= num1; q++) {

                cnt.merge(q,1,Integer::sum);
                if (q * q < num1){
                    cnt.merge(num1 / q,1,Integer::sum);
                }

            }


        }


        long count = 0;
        for (int num : nums2) {
            count += cnt.getOrDefault(num,0);
        }

        return count;
    }


    /**
     * 这种方式适合nums1、nums2中的元素过大，map会比上面的方式慢些
     * @param nums1
     * @param nums2
     * @param k
     * @return
     */
    public long numberOfPairs12(int[] nums1, int[] nums2, int k) {
        Map<Integer, Integer> cnt1 = new HashMap<>();
        for (int x : nums1) {
            if (x % k == 0) {
                cnt1.merge(x / k, 1, Integer::sum); // cnt1[x/k]++
            }
        }
        if (cnt1.isEmpty()) {
            return 0;
        }

        Map<Integer, Integer> cnt2 = new HashMap<>();
        for (int x : nums2) {
            cnt2.merge(x, 1, Integer::sum); // cnt2[x]++
        }

        long ans = 0;
        int u = Collections.max(cnt1.keySet());

        for (Map.Entry<Integer, Integer> e : cnt2.entrySet()) {
            int x = e.getKey();
            int cnt = e.getValue();
            int s = 0;
            for (int y = x; y <= u; y += x) { // 枚举 x 的倍数
                if (cnt1.containsKey(y)) {
                    s += cnt1.get(y);
                }
            }
            ans += (long) s * cnt;
        }
        return ans;
    }

}
