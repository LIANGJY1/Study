package com.example.study.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Student {
    int studentNo;//学号
    int age; //年龄
    String name;
    @Generated(hash = 742641795)
    public Student(int studentNo, int age, String name) {
        this.studentNo = studentNo;
        this.age = age;
        this.name = name;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }

    public int getStudentNo() {
        return this.studentNo;
    }
    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
