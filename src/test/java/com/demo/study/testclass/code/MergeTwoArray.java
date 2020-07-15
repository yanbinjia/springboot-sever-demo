package com.demo.study.testclass.code;

import java.util.Map;

public class MergeTwoArray {
    public void merge(int[] A, int m, int[] B, int n) {
        int len = m + n;
        for (int i = len - 1; i >= 0; i--) {
            if (m > 0 && n > 0) {
                if (A[m - 1] > B[n - 1]) {
                    A[i] = A[--m];
                } else {
                    A[i] = B[--n];
                }
            } else if (n > 0 && m == 0) {
                A[i] = B[--n];
            }
        }
    }

    public static void main(String[] args) {
        int[] A = {1,2,3,4,5};
        int[] B = {0,2,2,2,15};


    }
}
