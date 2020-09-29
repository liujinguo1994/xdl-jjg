package com.xdl.jjg.test;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/1 13:36
 */
public class TwoTuple<A,B> {

    public final A first;
    public final B second;

    public TwoTuple(A a, B b) {
        this.first = a;
        this.second = b;
    }

    @Override
    public String toString() {
        return "TwoTuple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
