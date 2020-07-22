package com.demo.study.testclass.lang;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.BitSet;

public class BitSetTest {
    public static void main(String[] args) {

        /**
         *  public BitSet(int nbits) {}
         *  最大为Integer.MAX_VALUE  2147483647 (20亿)
         */

        BitSet bitset = new BitSet();
        bitset.set(0);
        bitset.set(99);
        bitset.set(999);
        bitset.set(10000 * 10000 * 10 * 2);
        bitset.clear(999);

        for (int i = 0; i < 1000; i++) {
            if (bitset.get(i))
                System.out.println(bitset.get(i) + ":" + i);
        }

        // bitCount
        System.out.println("bitCount=" + bitset.cardinality());

        System.out.println("Integer.MAX_VALUE:" + Integer.MAX_VALUE);
        System.out.println("bitset.size=" + bitset.size());

        /*
        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);

        // set some bits
        for (int i = 0; i < 16; i++) {
            if ((i % 2) == 0) bits1.set(i);
            if ((i % 5) != 0) bits2.set(i);
        }
        System.out.println("Initial pattern in bits1: ");
        System.out.println(bits1);
        System.out.println("\nInitial pattern in bits2: ");
        System.out.println(bits2);

        // AND bits
        bits2.and(bits1);
        System.out.println("\nbits2 AND bits1: ");
        System.out.println(bits2);

        // OR bits
        bits2.or(bits1);
        System.out.println("\nbits2 OR bits1: ");
        System.out.println(bits2);

        // XOR bits
        bits2.xor(bits1);
        System.out.println("\nbits2 XOR bits1: ");
        System.out.println(bits2);
        */

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseInstance(bitset).instanceSize());
        //System.out.println(ClassLayout.parseClass(BitSetTest.class).toPrintable());
    }
}
