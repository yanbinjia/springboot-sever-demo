package com.demo.study.testclass.code;

public class mergeTwoList {

    public void merge(int[] A, int m, int[] B, int n) {

        int length = m + n;

        for (int i = length - 1; i >= 0; i--) {
            if (m > 0 && n > 0) {
                if (A[m - 1] < B[n - 1])           //比较AB最大值的大小，B更大则.....
                {
                    A[i] = B[--n];
                } else                      //A更大
                {
                    A[i] = A[--m];
                }

            } else if (n > 0 && m == 0) {
                A[i] = B[--n];
            }
        }

    }

    public static Node mergeTwoList(Node L1, Node L2) {
        Node node = new Node(0);
        Node p = node;

        while (L1 != null && L2 != null) {
            if (L1.val < L2.val) {
                p.next = L1;
                L1 = L1.next;
            } else {
                p.next = L2;
                L2 = L2.next;
            }
            p = p.next;
        }
        if (L1 != null) p.next = L1;
        if (L2 != null) p.next = L2;

        return node.next;
    }

    public static void main(String[] args) {
        Node l1 = new Node(0);
        l1.next = new Node(10);
        l1.next = new Node(15);

        Node l2 = new Node(1);
        l2.next = new Node(2);
        l2.next = new Node(20);

        mergeTwoList(l1, l2);
    }
}
