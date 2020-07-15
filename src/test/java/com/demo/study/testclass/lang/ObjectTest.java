package com.demo.study.testclass.lang;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectTest {
    public static void main(String[] args) {
        String str = new String("xxx");
        str.equals("123");

        Object object = new Object();

        System.out.println(object.toString());
        System.out.println(object.equals(null));
        System.out.println(object.hashCode());

        System.out.println(object.getClass().getTypeName());
        System.out.println(object.getClass().getInterfaces());
        System.out.println(object.getClass().getClassLoader());
        System.out.println(object.getClass().getAnnotations());

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("11");

        Collections.sort(list);

        Map<String, String> map = new ConcurrentHashMap<>();
        map.get("key");

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("x");

        Map<String, String> hashMap = new HashMap<>(16);

        StringBuffer stringBuffer = new StringBuffer("1");
        stringBuffer.append("1111");

        StringBuilder stringBuilder = new StringBuilder("1");
        int count = 0;
        for (int i = 0; i < Integer.MAX_VALUE - 2000000000; i++) {
            stringBuilder.append("a");
            count++;
        }
        System.out.println(count);


        /**
         * The maximum size of array to allocate (unless necessary).
         * Some VMs reserve some header words in an array.
         * Attempts to allocate larger arrays may result in
         * OutOfMemoryError: Requested array size exceeds VM limit
         */
        // private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

        /**
         * The value is used for character storage.
         */
        // char[] value;

        /**
         * The count is the number of characters used.
         */
        // int count;

    }
}
