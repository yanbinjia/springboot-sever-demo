package com.demo.study.testclass.code;

import java.util.HashMap;

public class LRUCache<K, V> {

    private int currentSize;
    private int capacity;
    private HashMap<K, Node> cacheHashMap;
    private Node first;
    private Node last;

    public LRUCache(int size) {
        this.currentSize = 0;
        this.capacity = size;
        cacheHashMap = new HashMap<K, Node>(size);
    }

    public void put(K k, V v) {
        Node node = cacheHashMap.get(k);
        if (node == null) {
            if (cacheHashMap.size() >= capacity) {
                cacheHashMap.remove(last.key);
                removeLast();
            }
            node = new Node();
            node.key = k;
        }
        node.value = v;
        moveToFirst(node);
        cacheHashMap.put(k, node);
    }

    public Object get(K k) {
        Node node = cacheHashMap.get(k);
        if (node == null) {
            return null;
        }
        moveToFirst(node);
        return node.value;
    }

    public Object remove(K k) {
        Node node = cacheHashMap.get(k);
        if (node != null) {
            if (node.pre != null) {
                node.pre.next = node.next;
            }
            if (node.next != null) {
                node.next.pre = node.pre;
            }
            if (node == first) {
                first = node.next;
            }
            if (node == last) {
                last = node.pre;
            }
        }

        return cacheHashMap.remove(k);
    }

    private void moveToFirst(Node node) {
        if (first == node) {
            return;
        }
        if (node.next != null) {
            node.next.pre = node.pre;
        }
        if (node.pre != null) {
            node.pre.next = node.next;
        }
        if (node == last) {
            last = last.pre;
        }
        if (first == null || last == null) {
            first = last = node;
            return;
        }

        node.next = first;
        first.pre = node;
        first = node;
        first.pre = null;

    }

    private void removeLast() {
        if (last != null) {
            last = last.pre;
            if (last == null) {
                first = null;
            } else {
                last.next = null;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node node = first;
        while (node != null) {
            sb.append(String.format("%s:%s ", node.key, node.value));
            node = node.next;
        }

        return sb.toString();
    }

    class Node {
        Node pre;
        Node next;
        Object key;
        Object value;

        public Node() {

        }
    }

    public static void main(String[] args) {

        LRUCache<Integer, String> lru = new LRUCache<Integer, String>(3);

        lru.put(1, "a");    // 1:a
        System.out.println(lru.toString());
        lru.put(2, "b");    // 2:b 1:a
        System.out.println(lru.toString());
        lru.put(3, "c");    // 3:c 2:b 1:a
        System.out.println(lru.toString());
        lru.put(4, "d");    // 4:d 3:c 2:b
        System.out.println(lru.toString());
        lru.put(1, "aa");   // 1:aa 4:d 3:c
        System.out.println(lru.toString());
        lru.put(2, "bb");   // 2:bb 1:aa 4:d
        System.out.println(lru.toString());
        lru.put(5, "e");    // 5:e 2:bb 1:aa
        System.out.println(lru.toString());
        lru.get(1);         // 1:aa 5:e 2:bb
        System.out.println(lru.toString());
        lru.remove(11);     // 1:aa 5:e 2:bb
        System.out.println(lru.toString());
        lru.remove(1);      //5:e 2:bb
        System.out.println(lru.toString());
        lru.put(1, "aaa");  //1:aaa 5:e 2:bb
        System.out.println(lru.toString());

        String sb = new StringBuffer("abba").reverse().toString();

    }

}