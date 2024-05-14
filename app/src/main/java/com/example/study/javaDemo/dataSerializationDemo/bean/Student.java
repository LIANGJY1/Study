package com.example.study.javaDemo.dataSerializationDemo.bean;

import java.io.Serializable;

public class Student implements Serializable {

    public static final long serialVersionUID = 1L;

    private int age;
    private String name;
    private transient int id;

    public Student(String name, int age, int id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
