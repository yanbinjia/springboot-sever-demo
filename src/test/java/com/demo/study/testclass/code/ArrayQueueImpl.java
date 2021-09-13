package com.demo.study.testclass.code;

public class ArrayQueueImpl {
    int[] queue = new int[5];
    int size = 0;// 表示队列中还有几个元素
    int start = 0;// 指向队列头
    int end = 0;// 指向队列尾

    // 入队列
    public void put(int value) {
        if (size >= queue.length) {
            throw new ArrayIndexOutOfBoundsException("the size more than bounds");
        }

        queue[end++] = value;
        size++;

        if (end == queue.length) {// 到达数组尾部，但是数组并没有满，再回到数组头部继续循环
            end = 0;
        }
    }

    // 出队列
    public int poll() {
        if (size <= 0) {
            throw new IllegalArgumentException("the size less than 0");
        }
        if (start == queue.length) {// 到达数组尾部，但是数组并没有空，再回到数组头部继续循环
            start = 0;
        }
        size--;
        return queue[start++];
    }

    public static void main(String[] args) {
        ArrayQueueImpl queue = new ArrayQueueImpl();
        queue.put(1);
        queue.put(2);
        queue.put(3);
        queue.put(4);
        queue.put(5);
        System.out.println(queue.poll());// 1
        System.out.println(queue.poll());// 2
        queue.put(6);
        queue.put(7);
        System.out.println(queue.poll());// 3
        System.out.println(queue.poll());// 4
        System.out.println(queue.poll());// 5
        System.out.println(queue.poll());// 6
        queue.put(8);
        System.out.println(queue.poll());// 7
        System.out.println(queue.poll());// 8
    }
}
