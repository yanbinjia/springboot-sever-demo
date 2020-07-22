package com.demo.study.testclass.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ListTest {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<String>();
        list1.add("A");
        list1.add("B");

        List<String> list2 = new ArrayList<String>();
        list2.add("C");
        list2.add("B");

        // 2个集合的并集
        list1.addAll(list2);
        System.out.println("并集:" + list1);

        // 2个集合的并集
        list1.retainAll(list2);
        System.out.println("交集:" + list1);

        Map<String, String> map = new HashMap<>(100);
        map.put("key-user-99383", "sssssssss");

        Map<String, String> concurrentMap = new ConcurrentHashMap<>(500);
        concurrentMap.put("key-user-10029225", "10029225");

        System.out.println("MAXIMUM_CAPACITY " + (1 << 30));
    }
}
