package com.example.study.util.tool;

import com.liang.log.MLog;

public class LogUtil {

    /**
     * 获取当前运行的方法的名称
     *
     * @return 当前方法名称
     */
    public static String getCurrentMethodName() {
        // 获取当前线程的堆栈跟踪元素数组
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        // 堆栈跟踪元素的索引：0是getStackTrace方法，1是getCurrentMethodName方法，2是当前调用的方法
        StackTraceElement targetElement = stackTraceElements[3];

        if (targetElement.getMethodName() == null) {
            MLog.i("获取当前方法名称失败");
            return "";
        }
        // 返回方法名称
        return targetElement.getMethodName();
    }


}
