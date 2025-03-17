package com.example.study.util.tool;

import android.util.Log;

import java.lang.reflect.Field;

public class ObjectUtil {
//    private static Unsafe unsafe;
//
//    static {
//        try {
//            Field field = Unsafe.class.getDeclaredField("theUnsafe");
//            field.setAccessible(true);
//            unsafe = (Unsafe) field.get(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static long getAddress(Object obj) {
//        Object[] array = new Object[]{obj};
//        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
//        return unsafe.getLong(array, baseOffset);
//    }

    static {
        System.loadLibrary("native-lib");
    }

    public static native long getObjectAddress(Object obj);
    public static native byte[] readMemory(long address, int size);

    public static void inspectObjectHeader(Object obj) {
        long address = getObjectAddress(obj);
        byte[] memory = readMemory(address, 8); // 读取前8字节（对象头通常位于前8字节）
        Log.d("ObjectHeader", "Memory: " + bytesToHex(memory));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

//    public static void main(String[] args) throws InterruptedException {
//        Object obj = new Object();
//        System.out.println("初始状态（无锁）:");
//        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
//
//        Thread.sleep(5000); // 等待偏向锁生效
//        synchronized (obj) {
//            System.out.println("休眠后首次加锁（偏向锁）:");
//            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
//        }
//    }
}
