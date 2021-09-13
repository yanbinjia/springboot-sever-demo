package com.demo.study.testclass.code;

import com.alibaba.druid.sql.visitor.functions.Char;

import java.util.HashMap;
import java.util.Map;

public class LongestSameStr {

    public String LongestSameStr(String str1, String str2) {
        int maxLen = 0;
        int index = 0;
        for (int i = 0; i < str1.length(); i++) {
            for (int j = i + 1; j < str2.length(); j++) {
                if (str1.contains(str2.substring(i, j))) {
                    if (maxLen < j - i) {
                        maxLen = j - i;
                        index = i;
                    }
                } else {
                    break;
                }
            }
        }

        if (maxLen == 0) return "-1";

        return str2.substring(index, index + maxLen);
    }

    public int lengthOfLongestSubstring(String s) {
        HashMap<Character, Integer> map = new HashMap();
        int ans = 0;//最大值
        int left = 0;//左指针
        for (int i = 0; i < s.length(); i++) {
            if (map.containsKey(s.charAt(i))) {
                left = Math.max(left, map.get(s.charAt(i)) + 1);
            }
            //无论是否相等我们都要录入下一个数的值和位置
            map.put(s.charAt(i), i);
            ans = Math.max(ans, i - left + 1);
        }
        return ans;
    }

    public int longestNoRepeatStr(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int result = 0;
        int left = 0;
        for (int i = 0; i < s.length(); i++) {
            if (map.containsKey(s.charAt(i))) {
                left = Math.max(left, map.get(s.charAt(i)) + 1);
            }
            map.put(s.charAt(i), i);
            result = Math.max(result, i - left + 1);
        }
        return result;
    }

    public static void main(String[] args) {
        LongestSameStr longestSameStr = new LongestSameStr();
        System.out.println(longestSameStr.longestNoRepeatStr("abbaa"));
    }


}
