package com.demo.study.testclass;

public class DisappearedException {
    public void show() throws BaseException {
        try {
            Integer.parseInt("Hello");
        } catch (NumberFormatException e1) {
            throw new BaseException(e1);// 异常被finally抛出的异常向上传递丢失了
        } finally {
            try {
                int result = 2 / 0;
            } catch (ArithmeticException e2) {
                throw new BaseException(e2);
            }
        }
    }

    public void show2() throws BaseException {
        Throwable numberFormatException = null;
        try {
            Integer.parseInt("Hello");
        } catch (NumberFormatException e1) {
            numberFormatException = e1;
            throw new BaseException(e1);// 异常被finally抛出的异常向上传递丢失了
        } finally {
            try {
                int result = 2 / 0;
            } catch (ArithmeticException e2) {
                BaseException baseException = new BaseException(e2);
                baseException.addSuppressed(numberFormatException);
                throw baseException;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DisappearedException d = new DisappearedException();
        System.out.println("show");
        d.show2();

    }
}

class BaseException extends Exception {
    public BaseException(Exception ex) {
        super(ex);
    }
}
