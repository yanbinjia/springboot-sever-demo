package com.demo.study.testclass.code;

public class BigAdd {
    public String bigAdd(String a, String b) {
        // input check
        if (a == null || b == null || "".equals(a.trim()) || "".equals(b.trim())) {
            return "";
        }
        // 去符号
        boolean aIsPositive = true;
        boolean bIsPositive = true;
        a = a.trim();
        b = b.trim();
        int aLen = a.length();
        int bLen = b.length();

        if (a.charAt(0) == '+' || a.charAt(0) == '-') {
            if (a.charAt(0) == '-') {
                aIsPositive = false;
            }
            a = a.substring(1, aLen);
        }
        if (b.charAt(0) == '+' || b.charAt(0) == '-') {
            if (b.charAt(0) == '-') {
                bIsPositive = false;
            }
            b = b.substring(1, bLen);
        }
        // !注意提取符号后的长度
        aLen = a.length();
        bLen = b.length();
        // 补0补齐
        if (aLen > bLen) {
            int f = aLen - bLen;
            while (f > 0) {
                b = "0" + b;
                f--;
            }
        } else {
            int f = bLen - aLen;
            while (f > 0) {
                a = "0" + a;
                f--;
            }
        }
        // 计算
        String rs = "";
        if ((aIsPositive && bIsPositive) || (!aIsPositive && !bIsPositive)) {
            // 同号
            if (aIsPositive) {
                rs = absAdd(a, b);
            } else {
                rs = "-" + absAdd(a, b);
            }
        } else {
            // 异号
            int compare = a.compareTo(b);
            if (compare > 0) {
                // a > b
                if (aIsPositive) {
                    rs = absSub(a, b);
                } else {
                    rs = "-" + absSub(a, b);
                }
            } else if (compare < 0) {
                // a < b
                if (aIsPositive) {
                    rs = "-" + absSub(b, a);
                } else {
                    rs = absSub(b, a);
                }
            } else {
                rs = "0";
            }
        }
        return rs;
    }

    private String absAdd(String a, String b) {
        String rs = "";
        int carry = 0;
        for (int i = a.length() - 1; i >= 0; i--) {
            int ai = a.charAt(i) - '0';
            int bi = b.charAt(i) - '0';
            int sum = ai + bi + carry;
            carry = sum / 10;
            sum = sum % 10;
            rs = sum + rs;
        }
        if (carry > 0) {
            rs = carry + rs;
        }
        return rs;
    }

    private String absSub(String a, String b) {
        String rs = "";
        int carry = 0;
        for (int i = a.length() - 1; i >= 0; i--) {
            int ai = a.charAt(i) - '0';
            int bi = b.charAt(i) - '0';
            boolean needCarry = (ai - carry) < bi;
            int sum = 0;
            if (needCarry) {
                sum = ai - carry + 10 - bi;
                carry = 1;
            } else {
                sum = ai - carry - bi;
                carry = 0;
            }
            rs = sum + rs;
        }
        return rs;
    }

    public static void main(String args[]) {
        BigAdd BigIntAdd = new BigAdd();
        System.out.println(BigIntAdd.bigAdd("1111", "9999"));
        System.out.println(BigIntAdd.bigAdd("1111", "-9999"));
        System.out.println(BigIntAdd.bigAdd("-1111", "9999"));
        System.out.println(BigIntAdd.bigAdd("-9999", "9999"));
        System.out.println(BigIntAdd.bigAdd("0", "9999"));
        System.out.println(BigIntAdd.bigAdd("0", "-9999"));
        System.out.println(BigIntAdd.bigAdd("198", "-169"));
        System.out.println(BigIntAdd.bigAdd("198", "198"));
        System.out.println(BigIntAdd.bigAdd("-198", "196"));
        System.out.println(BigIntAdd.bigAdd("-1111111111", "1"));
    }
}
