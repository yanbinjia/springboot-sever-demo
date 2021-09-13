package com.demo.study.testclass.code;

public class QuickSort {
    public void quickSort(int a[], int start, int end) {
        if (a == null || a.length == 0)
            return;
        if (start >= end) {
            return;
        }
        int i = start;
        int j = end;
        while (i < j) {
            while (i < j && a[i] <= a[j]) {
                j--;
            }
            if (i < j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
            while (i < j && a[i] <= a[j]) {
                i++;
            }
            if (i < j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        }
        quickSort(a, start, i - 1);
        quickSort(a, i + 1, end);
    }

    public static void bubbleSort(int[] arr) {
        //控制共比较多少论
        for (int i = 0; i < arr.length - 1; i++) {
            //控制比较次数
            for (int j = 0; j < arr.length - 1 - i; j++) {

                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int a[] = new int[]{1, 23, 3, 4, 6, 2, 3, 55, 33, 99};
        QuickSort qs = new QuickSort();
        qs.quickSort(a, 0, a.length - 1);
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
    }
}
