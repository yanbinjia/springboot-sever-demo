package com.demo.study.testclass.code;

import javax.xml.bind.annotation.XmlInlineBinaryData;
import java.lang.annotation.Target;

public class BinSearch {
    public int search(int[] nums, int target) {
        int midPoint = 0;
        int left = 0, right = nums.length - 1;
        while (left <= right) {// 条件要有=
            midPoint = (right - left) / 2 + left;// mid计算!!!!!
            if (target == nums[midPoint]) return midPoint;
            if (target > nums[midPoint]) {
                left = midPoint + 1;
            } else {
                right = midPoint - 1;
            }
        }
        return -1;

    }
    public int searchTest(int[] nums, int target) {
        if (nums == null) return -1;
        int len = nums.length;
        int left = 0;
        int right = len - 1;
        while (left <= right) {
            int mid = (right - left) / 2 + left;// mid计算!!!!!
            if (target == nums[mid]) return mid;
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

        }
        return -1;
    }

    public int searchTestL(int[] nums, int target) {
        if (nums == null) return -1;
        int len = nums.length;
        int left = 0;
        int right = len - 1;
        while (left < right) {
            int mid = (right - left) / 2 + left;// mid计算!!!!!
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }

        }
        return nums[left] == target ? left : -1;
    }

    public int searchTestR(int[] nums, int target) {
        if (nums == null) return -1;
        int len = nums.length;
        int left = 0;
        int right = len - 1;
        while (left < right) {
            int mid = (right - left) / 2 + left + 1;// mid计算!!!!!
            if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid;
            }

        }
        return nums[left] == target ? left : -1;
    }

    public static void main(String[] args) {
        BinSearch binSearch = new BinSearch();
        int location = binSearch.search(new int[]{1, 2, 4, 6, 7, 8, 99, 10000}, 4);
        System.out.printf("location=%d%n", location);

        location = binSearch.searchTestL(new int[]{1, 6, 6, 6, 7, 8, 99, 10000}, 6);
        System.out.printf("location=%d%n", location);

        location = binSearch.searchTestR(new int[]{1, 6, 6, 6, 7, 8, 99, 10000}, 6);
        System.out.printf("location=%d%n", location);
    }
}
