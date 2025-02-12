package com.example.study.pattern.singleton;

public class SingletonDemo {

//    // 饿汉式
//    private SingletonDemo() {
//    }
//
//    private static final SingletonDemo INSTANCE = new SingletonDemo();
//
//    public static SingletonDemo getInstance() {
//        return INSTANCE;
//    }






//    // 懒汉式1: 线程不安全
//    private SingletonDemo() {
//
//    }
//
//    private static SingletonDemo INSTANCE;
//
//    public static SingletonDemo getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new SingletonDemo();
//        }
//
//        return INSTANCE;
//    }





//    // 懒汉式2: 同步方法
//    private SingletonDemo() {
//
//    }
//
//    private static SingletonDemo INSTANCE;
//
//    public static synchronized SingletonDemo getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new SingletonDemo();
//        }
//
//        return INSTANCE;
//    }




//    // 懒汉式3：双重检查
//    private SingletonDemo() {
//
//    }
//
//    private static SingletonDemo INSTANCE;
//
//    public static SingletonDemo getInstance() {
//        if (INSTANCE == null) {
//            synchronized (SingletonDemo.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new SingletonDemo();
//                }
//            }
//        }
//        return INSTANCE;
//    }




    // 懒汉式4：静态内部类
    private SingletonDemo(){}

    private static class Inner {
        private static final SingletonDemo INSTANCE = new SingletonDemo();
    }

    public static SingletonDemo getInstance() {
        return Inner.INSTANCE;
    }


}
