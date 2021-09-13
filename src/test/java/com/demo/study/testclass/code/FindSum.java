package com.demo.study.testclass.code;

import java.util.Arrays;

public class FindSum {
    /**
     * 如果数组排好序，我们可以利用这种顺序，以O（n）的时间遍历数组。
     * 思路是这样的：
     * 设置两个指针i和j，i指向最左边最小的元素，j指向最右边最大的元素，
     * 然后判断a[i]+a[j]和x的关系。小于的时候i++，大于的时候j– 。
     * 遇见相等就表示找到了。如果i和j的大小关系颠倒了表示没有这样的两个数。
     */
    public int find(int[] a, int target) {
        Arrays.sort(a);
        int i = 0;
        int j = a.length - 1;
        while (i <= j) {
            if (a[i] + a[j] == target)
                return 1;
            else if (a[i] + a[j] < target)
                ++i;
            else
                --j;
        }
        return -1;
    }
}
