package com.demo.study.testclass.lang;

public class BitCompute {
    public static void main(String[] args) {
        int a = 100;

        // 数 a 向右移一位，相当于将 a 除以 2；
        // 数 a 向左移一位，相当于将 a 乘以 2
        System.out.println(a >> 1);
        System.out.println(a << 1);

        // 判断奇数偶数
        if (0 == (a & 1)) {
            //偶数
            System.out.println("a=" + a + ",a为偶数");
        }

        // 变换符号，取反加1
        System.out.println(~a + 1);

        int b = 200;

        // 交换两数
        a = a ^ b;
        b = b ^ a;
        a = a ^ b;
        System.out.printf("a=%d %n", a);
        System.out.printf("b=%d %n", b);

        // 交换两数=>
        int c = a;
        a = b;
        b = c;
        System.out.printf("a=%d %n", a);
        System.out.printf("b=%d %n", b);

        // 计算绝对值
        a = -100;
        int i = a >> 31;
        int absI = i == 0 ? a : (~a + 1);
        System.out.println("absI=" + absI);

        // 对于任何数，与0异或都会保持不变，与-1(即0xFFFFFFFF)异或就相当于取反
        a = -100;
        i = a >> 31; // 0 or 1
        absI = ((a ^ i) - i);
        // 11111111111111111111111111111111
        System.out.println(Integer.toBinaryString(-1));
        System.out.println("absI=" + absI);

        // 统计二进制中1的个数
        a = 123;
        System.out.println(Integer.toBinaryString(a));
        int count = 0;
        while (a != 0) {
            a = a & (a - 1);
            count++;
        }
        System.out.println("count=" + count);

    }
}
