package dev.gabriel.springboot2.client;

import java.io.Serializable;

public class A implements Serializable {
    private static String name = "test1";
    private static A a = new A();

    static {
        name = "test2";
    }

    public A(){
        name = "test3";
    }

    public static String print(){
        return a.name;
    };

    @Override
    public String toString() {
        return print();
    }

    public static void main(String[] args) {
        System.out.println(A.print());
    }
}