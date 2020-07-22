package com.demo.study.testclass.code;

public class isPalindromeNum {
    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        if (x >= 0 && x < 10) {
            return true;
        }

        char[] chars = Integer.toString(x).toCharArray();
        int left = 0;
        int right = chars.length - 1;

        while (left < right) {
            if (chars[left] == chars[right]) {
                left++;
                right--;
            } else {
                return false;
            }
        }

        return true;
    }

    public static boolean isPalindromeByString(int num) {
        /**
         * 特殊情况：
         *  如上所述，当 x < 0 时，x 不是回文数。
         *  同样地，如果数字的最后一位是 0，为了使该数字为回文，
         *  则其第一位数字也应该是 0
         *  只有 0 满足这一属性
         */
        if (num < 0 || (num % 10 == 0 && num != 0)) {
            return false;
        }

        /**
         * 对于数字 1221，如果执行 1221 % 10，我们将得到最后一位数字 1，
         * 要得到倒数第二位数字，我们可以先通过除以10 把最后一位数字从 1221 中移除，1221 / 10 = 122，
         * 再求出上一步结果除以10的余数，122 % 10 = 2，就可以得到倒数第二位数字。
         * 如果我们把最后一位数字乘以10，再加上倒数第二位数字，1 * 10 + 2 = 12，就得到了我们想要的反转后的数字。
         * 如果继续这个过程，我们将得到更多位数的反转数字。
         */
        int revertedNumber = 0;
        while (num > revertedNumber) {
            revertedNumber = revertedNumber * 10 + num % 10;
            num = num / 10;
        }

        /**
         * 当数字长度为奇数时，我们可以通过
         * revertedNumber/10 去除处于中位的数字。
         * 例如，当输入为 12321 时，在 while 循环的末尾我们可以得到
         * x = 12，revertedNumber = 123，
         * 由于处于中位的数字不影响回文（它总是与自己相等），
         * f所以我们可以简单地将其去除。
         */
        return num == revertedNumber || num == revertedNumber / 10;
    }
}
