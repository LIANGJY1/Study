package com.example.javatest.test;

public class Outer {
    private int outerField;
    public class Inner {
        public Inner() {
        }
        public void innerMethod() {
            int value = outerField;
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }
    }

}
