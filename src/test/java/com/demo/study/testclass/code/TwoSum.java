package com.demo.study.testclass.code;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public int[] twoSum(int[] input, int target) {
        int[] result = new int[2];
        if (input == null || input.length == 0) {
            return result;
        }
        Map<Integer, Integer> tmpMap = new HashMap<>();
        for (int i = 0; i < input.length; i++) {
            tmpMap.put(input[i], i);
        }
        for (int i = 0; i < input.length; i++) {
            int dest = target - input[i];
            if (tmpMap.get(dest) != null && tmpMap.get(dest) > i) {
                result[0] = i;
                result[1] = tmpMap.get(dest);
            }
        }
        return result;
    }
}
