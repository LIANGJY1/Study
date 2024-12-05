// IUserManager.aidl
package com.example.studysdk;

//3333
import com.example.studysdk.User;

// Declare any non-default types here with import statements

//2222

interface IUserManager {
//1111
    List<User> getUser();
    void addUser(in User user);
}