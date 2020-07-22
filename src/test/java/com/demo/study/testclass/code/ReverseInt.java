package com.demo.study.testclass.code;

public class ReverseInt {
    public int reverse(int x) {
        if (x > -10 && x < 10) {
            return x;
        }
        int flag = 1;
        String str = Integer.toString(x);
        char[] chars = str.toCharArray();
        if (x < 0) {
            flag = -1;
            chars = str.substring(1, str.length()).toCharArray();
        }

        int right = chars.length - 1;

        int left = 0;
        char[] output = new char[chars.length];

        while (right >= 0) {
            if (chars[right] == 0 && right == (chars.length - 1)) {
                right--;
                continue;
            }
            output[left] = chars[right];
            right--;
            left++;
        }

        int resInt = 0;
        long res = flag * Long.valueOf(new String(output));
        if (flag > 0)
            resInt = res > Integer.MAX_VALUE ? 0 : (int) res;
        if (flag < 0)
            resInt = res < Integer.MIN_VALUE ? 0 : (int) res;
        return resInt;
    }

    public int reverse2(int x) {
        long temp = 0;
        int rs = 0;
        boolean positive = true;
        String xstr = String.valueOf(x);
        if (x < 0) {
            positive = false;
            xstr = "" + (-x);
        }
        for (int i = xstr.length() - 1; i >= 0; i--) {
            temp = temp * 10 + (xstr.charAt(i) - '0');
        }
        if (positive) {
            rs = temp > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) temp;
        } else {
            rs = (-temp) < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) (-temp);
        }
        return rs;

    }
}
