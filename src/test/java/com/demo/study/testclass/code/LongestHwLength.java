package com.demo.study.testclass.code;

import java.util.HashSet;

/**
 * 输入: "abccccdd" 输出: 7
 * <p>
 * 解释:
 * 我们可以构造的最长的回文串是"dccaccd", 它的长度是 7。
 * <p>
 * 我们上面已经知道了什么是回文串？现在我们考虑一下可以构成回文串的两种情况：
 * <p>
 * 1.字符出现次数为双数的组合
 * 2.字符出现次数为偶数的组合+单个字符中出现次数最多且为奇数次的字符
 */
class LongestHwLength {
    public int longestPalindrome(String s) {
        if (s.length() == 0)
            return 0;
        // 用于存放字符
        HashSet<Character> hashset = new HashSet<Character>();
        char[] chars = s.toCharArray();
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (!hashset.contains(chars[i])) {// 如果hashset没有该字符就保存进去
                hashset.add(chars[i]);
            } else {// 如果有,就让count++（说明找到了一个成对的字符），然后把该字符移除
                hashset.remove(chars[i]);
                count++;
            }
        }
        return hashset.isEmpty() ? count * 2 : count * 2 + 1;
    }

    public static void main(String[] args) {
        LongestHwLength solution = new LongestHwLength();
        System.out.println(solution.longestPalindrome("abba"));
    }
}