package com.demo.study.testclass.code;

import org.junit.platform.commons.util.StringUtils;

public class IsPalindromeStr {

    public boolean isPalindrome(String s) {
        if (s.length() == 0)
            return true;
        int l = 0, r = s.length() - 1;
        while (l < r) {
            // 从头和尾开始向中间遍历
            if (!Character.isLetterOrDigit(s.charAt(l))) {// 字符不是字母和数字的情况
                l++;
            } else if (!Character.isLetterOrDigit(s.charAt(r))) {// 字符不是字母和数字的情况
                r--;
            } else {
                // 判断二者是否相等
                if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r)))
                    return false;
                l++;
                r--;
            }
        }
        return true;
    }

    public boolean isPalindrome2(String s) {

        if (StringUtils.isBlank(s)) {
            return false;
        }

        int i = 0;
        int j = s.length() - 1;
        char left = 0;
        char right = 0;
        int count = 0;

        while (i < j) {
            left = s.charAt(i);
            right = s.charAt(j);
            if (!Character.isLetterOrDigit(left)) {
                i++;
            } else if (!Character.isLetterOrDigit(right)) {
                j--;
            } else {
                if (Character.toLowerCase(left) == Character.toLowerCase(right)) {
                    count++;
                    i++;
                    j--;
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        IsPalindromeStr solution = new IsPalindromeStr();
        System.out.println(solution.isPalindrome2("A man, a plan, a canal: Panama"));
        System.out.println(solution.isPalindrome2("race a car"));
    }
}
