package com.example.studysdk.util;

import com.liang.log.MLog;

public class DebugUtil {

    public static void printStackTrace(String method, int stackLength) {
        if (stackLength == Integer.MAX_VALUE) {
            stackLength -= 2;
        }
        Throwable throwable = new Throwable();
//        try {
//            throw throwable;
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        MLog.i("DebugUtil printStackTrace, " + method);
        for (int i = 2; i < (Math.min(stackLength + 2, stackTrace.length)); i++) {
            StackTraceElement element = stackTrace[i];
            MLog.i( "Called by: " + element.getClassName() + "." + element.getMethodName());
        }
    }
}
