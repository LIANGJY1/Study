package com.example.study.util;

import java.util.Objects;

public class PackageUtil {
    public PackageUtil() {
    }

    public static String getSimpleClassName(Object object) {
        Class<?> aClass = object.getClass();
        String str1 = aClass.getName().replace("$", ".");
        String str2 = str1.replace(Objects.requireNonNull(aClass.getPackage()).getName(), "");
        return str2.substring(1);
    }
}
