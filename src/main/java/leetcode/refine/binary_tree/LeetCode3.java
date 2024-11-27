package leetcode.refine.binary_tree;


import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/
 * 无重复字符的最长子串
 */
public class LeetCode3 {

    @Test
    public void test(){
        //给定一个字符串s,请你找出其中不含有重复字符的最长子串的长度。
        //示例 1:
        //
        //输入: s = "abcabcbb"
        //输出: 3
        //解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
        //示例 2:
        //
        //输入: s = "bbbbb"
        //输出: 1
        //解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
        //示例 3:
        //
        //输入: s = "pwwkew"
        //输出: 3
        //解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
        // 请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。

        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(100,"asd");
        map.put(99,"cc");

        map.forEach((x,y) -> {
            System.out.println(y);
        });
//        System.out.println(lengthOfLongestSubstring3("abcabcbb"));
//        System.out.println(lengthOfLongestSubstring3("bbbbb"));
//        System.out.println(lengthOfLongestSubstring3("pwwkew"));


    }


    public int lengthOfLongestSubstring(String s) {

        int res = 0;
        String flag = "";

        char[] chars = s.toCharArray();
        for (int right = 0; right < chars.length; right++) {

            flag += chars[right];

            for (int i = 0; i < flag.toCharArray().length - 1; i++) {
                if (flag.toCharArray()[i] == chars[right]){
                    flag = flag.substring(i + 1);
                    break;
                }
            }

            res = Math.max(flag.length(),res);
        }

        return res;
    }


    public int lengthOfLongestSubstring2(String s) {

        int res = 0;
        int left = 0;

        Map<Character,Integer> charMap = new HashMap<>();

        char[] chars = s.toCharArray();

        for (int right = 0; right < chars.length; right++) {

            char curChar = chars[right];
            Integer index = charMap.get(curChar);

            if (index != null && left <= index){
                //重复
                left = index + 1;
            }

            charMap.put(curChar,right);

            res = Math.max(right - left + 1,res);
        }

        return res;
    }


    public int lengthOfLongestSubstring3(String s) {

        int res = 0;
        int left = 0;

        Map<Character,Integer> charMap = new HashMap<>();

        char[] chars = s.toCharArray();

        for (int right = 0; right < chars.length; right++) {

            char curChar = chars[right];
            charMap.merge(curChar, 1, Integer::sum);

            while (charMap.get(curChar) > 1){
                charMap.merge(chars[left], -1, Integer::sum);
                //重复
                left++;

            }

            res = Math.max(right - left + 1,res);
        }

        return res;
    }


    /**
     * 思路:左指针代表子串开始，右指针是当前子串结尾
     * 那么如果找到相同的字符，如何将该字符前存在的字符去掉（需要左指针来做）
     * has数组存储当前字符是否存在
     * 1。如果has[c] == true，则让left前进，has[s[left]]=false,直到has[c] == false,说明该重复字符前的字符都清理干净了 (如果重复次数可以存次数减次数)
     * 2。接着加入 c，比较最大值
     * @param S
     * @return
     */
    public int lengthOfLongestSubstring4(String S) {
        char[] s = S.toCharArray(); // 转换成 char[] 加快效率（忽略带来的空间消耗）
        int n = s.length, ans = 0, left = 0;
        boolean[] has = new boolean[128]; // 也可以用 HashSet<Character>，这里为了效率用的数组
        for (int right = 0; right < n; right++) {
            char c = s[right];
            // 如果窗口内已经包含 c，那么再加入一个 c 会导致窗口内有重复元素
            // 所以要在加入 c 之前，先移出窗口内的 c
            while (has[c]) { // 窗口内有 c
                has[s[left++]] = false; // 缩小窗口
            }
            has[c] = true; // 加入 c
            ans = Math.max(ans, right - left + 1); // 更新窗口长度最大值
        }
        return ans;
    }

}
