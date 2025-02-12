// IUserManager.aidl
package com.example.studysdk;

import com.example.studysdk.User;

// Declare any non-default types here with import statements

// server 端提供的功能
interface IUserManager {
    List<User> getUser();
    void addUser(in User user);
}