package com.demo.study.testclass.code;

public class LongestHwStr {
    private int index, len;

    public String longestPalindrome(String s) {
        if (s.length() < 2)
            return s;
        for (int i = 0; i < s.length() - 1; i++) {
                PalindromeHelper(s, i, i);
                PalindromeHelper(s, i, i + 1);
        }
        return s.substring(index, index + len);
    }

    public void PalindromeHelper(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        if (len < r - l - 1) {
            index = l + 1;
            len = r - l - 1;
        }
    }

    public static void main(String[] args) {
        // "cbbd"
        LongestHwStr longestHwStrSolution = new LongestHwStr();
        String s = longestHwStrSolution.longestPalindrome("babad");
        System.out.println(s);

        s = longestHwStrSolution.longestPalindrome("cbbd");
        System.out.println(s);
    }
}
