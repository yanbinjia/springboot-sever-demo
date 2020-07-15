package com.demo.study.testclass.code;

import java.util.HashMap;
import java.util.Map;

public class RomanToInt {

    public int romanToInt(String s) {
        Map<Character, Integer> markMap = new HashMap<>();
        markMap.put('M', 1000);
        markMap.put('D', 500);
        markMap.put('C', 100);
        markMap.put('L', 50);
        markMap.put('X', 10);
        markMap.put('V', 5);
        markMap.put('I', 1);

        int result = 0;
        int current = 0;
        int next = 0;

        for (int i = 0; i < s.length() - 1; i++) {
            current = markMap.get(s.charAt(i));
            next = markMap.get(s.charAt(i + 1));

            if (next > current) {
                result -= current;
            } else {
                result += current;
            }
        }
        result += markMap.get(s.charAt(s.length() - 1));

        return result;
    }
}
