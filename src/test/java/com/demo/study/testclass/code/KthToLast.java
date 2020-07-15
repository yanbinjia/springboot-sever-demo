package com.demo.study.testclass.code;

import org.apache.commons.configuration.plist.XMLPropertyListConfiguration;

public class KthToLast {
    /**
     * Definition for singly-linked list.
     */
    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public int kthToLast(ListNode head, int k) {
        if (head == null) {
            return Integer.MIN_VALUE;
        }
        ListNode former = head;
        ListNode later = head;
        for (int i = 0; i < k; i++) {
            former = former.next;
        }
        while (former != null) {
            former = former.next;
            later = later.next;
        }
        return later.val;
    }

    public static void main(String[] args) {
        System.err.println();
    }

}
