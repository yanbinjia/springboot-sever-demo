package com.demo.study.testclass.code;

public class CompareVersion {
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");//超级重要的细节
        String[] v2 = version2.split("\\.");
        for (int i = 0, j = 0; i < v1.length || j < v2.length; i++, j++) {
            int n1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int n2 = j < v2.length ? Integer.parseInt(v2[j]) : 0;
            if (n1 != n2) {
                return n1 > n2 ? 1 : -1;
            }
        }
        return 0;//版本是一样的，返回0
    }


    public int compareVersion2(String v1, String v2) {
        int result = 0;

        if (v1 == null || "".equals(v1 = v1.trim())) v1 = "0";
        if (v2 == null || "".equals(v2 = v2.trim())) v2 = "0";

        String[] v1Array = v1.split("\\.");
        String[] v2Array = v2.split("\\.");

        for (int i = 0, j = 0; i < v1Array.length || j < v2Array.length; i++, j++) {
            int a = i < v1Array.length ? Integer.parseInt(v1Array[i]) : 0;
            int b = j < v2Array.length ? Integer.parseInt(v2Array[j]) : 0;
            if (a != b) {
                return a > b ? 1 : -1;
            }
        }

        return 0;
    }


    public static void main(String[] args) {
        CompareVersion compareVersion = new CompareVersion();
        System.out.println(compareVersion.compareVersion("1.1", "1.01"));
        System.out.println(compareVersion.compareVersion("1.1", "1.10"));
        System.out.println(compareVersion.compareVersion("1.1.1", "1.1"));
        System.out.println(compareVersion.compareVersion("1.001", "1.1"));


    }
}
