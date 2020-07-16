package com.demo.study.testclass.lang;

public class StringStudy {
    public static void main(String[] args) {

        String str1 = "123";
        String str2 = "123";
        String str3 = new String("123");

        System.out.println(str1 == str2);// true
        System.out.println(str1 == str3);// false

        String str4 = str3;
        System.out.println(str3 == str4);// true

        str3 = str3.intern();
        System.out.println(str1 == str3);// true
        System.out.println(str1 == str4);// false

        /**
         *    L20
         *     LINENUMBER 20 L20
         *     LDC "123456abc"
         *     ASTORE 5
         *    L21
         *     LINENUMBER 21 L21
         *     LDC "123456abc"
         *     ASTORE 6
         */
        String stringAdd = "123" + "456" + "abc";
        String stringAdd2 = "123456abc";
        System.out.println(stringAdd == stringAdd2);// true

        /**
         *     L26
         *     LINENUMBER 36 L26
         *     NEW java/lang/StringBuilder
         *     DUP
         *     INVOKESPECIAL java/lang/StringBuilder.<init> ()V
         *     LDC "123456"
         *     INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
         *     ALOAD 7
         *     INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
         *     INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;
         *     ASTORE 8
         */
        String stringAdd3 = "abc";
        String stringAdd4 = "123456" + stringAdd3;
        System.out.println(stringAdd == stringAdd4); // false

        /**
         *    L30
         *     LINENUMBER 51 L30
         *     LDC "test"
         *     ASTORE 9
         *    L31
         *     LINENUMBER 52 L31
         *     ICONST_0
         *     ISTORE 10
         *    L32
         *    FRAME APPEND [java/lang/String I]
         *     ILOAD 10
         *     BIPUSH 100
         *     IF_ICMPGE L33
         *    L34
         *     LINENUMBER 53 L34
         *     NEW java/lang/StringBuilder
         *     DUP
         *     INVOKESPECIAL java/lang/StringBuilder.<init> ()V
         *     ALOAD 9
         *     INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
         *     ILOAD 10
         *     INVOKEVIRTUAL java/lang/StringBuilder.append (I)Ljava/lang/StringBuilder;
         *     INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;
         *     ASTORE 9
         *    L35
         *     LINENUMBER 52 L35
         *     IINC 10 1
         *     GOTO L32
         */
        String teststr = "test";
        for (int i = 0; i < 100; i++) {
            teststr = teststr + i;
        }
        System.out.println(teststr);


    }
}
