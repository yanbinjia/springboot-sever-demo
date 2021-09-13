package com.demo.study.testclass.code;

import java.util.List;

public class addTwoNumbers {
    class ListNode {
        int val;
        ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode addTwoNumbers(ListNode first, ListNode second) {
        ListNode node = new ListNode(0);
        ListNode p = node;

        if (first == null) return second;
        if (second == null) return first;

        int carry = 0;
        while (first != null || second != null) {
            int sum = 0;
            int a = 0;
            int b = 0;
            if (first != null) {
                a = first.val;
                first = first.next;
            }
            if (second != null) {
                b = second.val;
                second = second.next;
            }

            sum = a + b + carry;
            sum = sum % 10;
            carry = sum / 10;

            ListNode tmp = new ListNode(sum);
            p.next = tmp;
            p = p.next;
        }

        return node.next;
    }
}
