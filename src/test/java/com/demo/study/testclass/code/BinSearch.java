package com.demo.study.testclass.code;

import javax.xml.bind.annotation.XmlInlineBinaryData;

public class BinSearch {
    public int search(int[] nums, int target) {
        int midPoint = 0;
        int left = 0, right = nums.length - 1;
        while (left <= right) {// 条件要有=
            midPoint = (right - left) / 2 + left;// mid计算
            if (target == nums[midPoint]) return midPoint;
            if (target > nums[midPoint]) {
                left = midPoint + 1;
            } else {
                right = midPoint - 1;
            }
        }
        return -1;

    }

    public static void main(String[] args) {
        BinSearch binSearch = new BinSearch();
        int location = binSearch.search(new int[]{1, 2, 4, 6, 7, 8, 99, 10000}, 4);
        System.out.printf("location=%d%n", location);
    }
}
