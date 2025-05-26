// IUserManager.aidl
package com.example.study.service;
import com.example.study.service.User;

// Declare any non-default types here with import statements

interface IUserManager {
    List<User> getUser();
    void addUser(in User user);
}