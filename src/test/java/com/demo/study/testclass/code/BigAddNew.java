package com.demo.study.testclass.code;

public class BigAddNew {
    public String bigAdd(String a, String b) {
        String rs = "";
        if (a == null || "".equals(a = a.trim())) a = "0";
        if (b == null || "".equals(b = b.trim())) b = "0";
        int aLen = a.length();
        int bLen = b.length();
        int carry = 0;
        for (int i = aLen - 1, j = bLen - 1; i >= 0 || j >= 0; i--, j--) {
            int aTmp = i >= 0 ? (a.charAt(i) - '0') : 0;
            int bTmp = j >= 0 ? (b.charAt(j) - '0') : 0;
            int sum = aTmp + bTmp + carry;
            sum = sum % 10;
            carry = sum / 10;
            rs = sum + rs;
        }
        if (carry > 0) {
            rs = carry + rs;
        }
        return rs;
    }

    public String bigAdd2(String a, String b){
        String rs = "";
        if (a == null || "".equals(a = a.trim())) a = "";
        if (b == null || "".equals(b = b.trim())) b = "";
        int aLen = a.length();
        int bLen =  b.length();
        int carry = 0;
        for (int i = aLen - 1, j= bLen - 1; i >= 0 || j >= 0 ;i--,j--){
            int aTmp = i >=0 ? a.charAt(i) - '0' : 0;
            int bTmp = j >=0 ? b.charAt(i) - '0' : 0;
            int sum = aTmp + bTmp + carry;
            sum = sum % 10;
            carry = sum / 10;
            rs = sum + rs;
        }
        if (carry > 0){
            rs = "1" + rs;
        }
        return rs;
    }

    public static void main(String[] args) {
        BigAddNew bigAdder = new BigAddNew();
        System.out.println(bigAdder.bigAdd("-1", "2222"));
    }
}
