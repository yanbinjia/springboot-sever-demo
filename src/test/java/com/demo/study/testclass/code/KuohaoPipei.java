package com.demo.study.testclass.code;

import java.util.Stack;

public class KuohaoPipei {
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();   //创建一个栈
        for (int i = 0; i < s.length(); i++) {
            char tmp = s.charAt(i);
            // 如果是左括号，则将其放入栈内
            if (tmp == '(' || tmp == '[' || tmp == '{') {
                stack.push(s.charAt(i));
            }
            // 如果是右括号
            if (tmp == ')' || tmp == ']' || tmp == '}') {
                // 如果栈为空，则证明括号不匹配，返回false
                if (stack.empty()) {
                    return false;
                }

                char peek = stack.peek();
                // 如果栈顶元素和下一个右括号相匹配，则将其栈顶元素出栈
                if ((peek == '(' && tmp == ')') || (peek == '[' && tmp == ']') || (peek == '{' && tmp == '}')) {
                    stack.pop();
                }
            }
        }
        if (stack.empty()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(KuohaoPipei.isValid("(()[])"));
        System.out.println(KuohaoPipei.isValid("(()[)"));
    }

}
