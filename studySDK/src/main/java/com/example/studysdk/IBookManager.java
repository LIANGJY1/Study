package com.example.studysdk;

import android.os.IBinder;
import android.os.IInterface;

import java.util.List;

public interface IBookManager extends IInterface {
    String DESCRIPTOR = "com.example.studysdk.IBookManager";
    int TRANSACTION_GET_BOOK_LIST = (IBinder.FIRST_CALL_TRANSACTION);
    int TRANSACTION_ADD_BOOK = (IBinder.FIRST_CALL_TRANSACTION + 1);
    List<Book> getBookList() throws android.os.RemoteException;
    void addBook(Book book) throws android.os.RemoteException;
}
