package com.example.study.pattern.singleton;

import com.example.study.util.tool.ObjectUtil;

public class Person {
    public static void run() {
        Object object = new Object();
//        ObjectUtil.inspectObjectHeader(object);
        synchronized (object) {
            //
        }
    }
}