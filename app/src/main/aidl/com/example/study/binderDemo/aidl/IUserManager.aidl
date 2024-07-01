// IUserManager.aidl
package com.example.study.binderDemo.aidl;
import com.example.study.binderDemo.aidl.User;

// Declare any non-default types here with import statements

interface IUserManager {
    List<User> getUser();
    void addUser(in User user);
}