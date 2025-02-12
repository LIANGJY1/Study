package com.example.javatest;

public class MyClass {
    private static Status mCurrentStatus;

    enum Status{
        YELLOW, RED, GREEN
    }

    public static void setCurrentStatus(Status currentStatus){
        mCurrentStatus = currentStatus;
    }
    public static void main(String[] args) {
        setCurrentStatus(Status.GREEN);
    }
}