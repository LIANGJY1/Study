package com.example.javatest.test;

// 1. 基础静态初始化顺序
public class StaticTestDemo {
    static int a = initA(); // 静态变量显式赋值

    static { // 静态代码块
        System.out.println("static1");
    }

    static int b = initB();

    static {
        System.out.println("static2");
    }

    private static int initA() {
        System.out.println("initA");
        return 1;
    }

    private static int initB() {
        System.out.println("initB");
        return 2;
    }

    public static void main(String[] args) {
        System.out.println("main");
    }
}

// 2. 父子类静态初始化顺序
//class Parent {
//    static {
//        System.out.println("Parent");
//    }
//}
//
//class Child extends Parent {
//    static {
//        System.out.println("Child");
//    }
//}
//
//public class StaticTestDemo {
//    public static void main(String[] args) {
//        new Child();
//    }
//}

// 3. 数组定义不触发初始化
//class ArrayClass {
//    static {
//        System.out.println("ArrayClass");
//    }
//}
//
//public class StaticTestDemo {
//    public static void main(String[] args) {
//        ArrayClass[] arr = new ArrayClass[10]; // 仅创建数组，不初始化组件类
//    }
//}

// 4. 编译时常量不触发初始化
//class ConstantClass {
//    public static final int CONST = 123; // 编译时常量
//
//    static {
//        System.out.println("ConstantClass");
//    }
//}
//
//public class StaticTestDemo {
//    public static void main(String[] args) {
//        System.out.println(ConstantClass.CONST); // 直接内联值
//    }
//}

// 5. 接口初始化特性
//interface InterfaceDemo {
//    // 接口中的静态字段默认 public static final
//    Thread INIT_THREAD = new Thread() {
//        {
//            System.out.println("InterfaceDemo");
//        }
//    };
//}
//
//public class StaticTestDemo {
//    public static void main(String[] args) {
//        System.out.println(InterfaceDemo.INIT_THREAD); // 触发接口初始化
//    }
//}

// 6. 多线程安全验证
//class ThreadSafeClass {
//    static {
//        System.out.println(Thread.currentThread().getName() + " init");
//    }
//}
//
//public class StaticTestDemo {
//    public static void main(String[] args) {
//        Runnable task = () -> {
//            System.out.println(Thread.currentThread().getName() + " start");
//            new ThreadSafeClass();
//        };
//
//        new Thread(task).start();
//        new Thread(task).start();
//    }
//}
