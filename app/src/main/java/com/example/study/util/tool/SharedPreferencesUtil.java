package com.example.study.util.tool;

public class SharedPreferencesUtil {
    private static final SharedPreferencesUtil INSTANCE = new SharedPreferencesUtil();

    private SharedPreferencesUtil() {

    }

    public SharedPreferencesUtil getInstance() {
        return INSTANCE;
    }

}
