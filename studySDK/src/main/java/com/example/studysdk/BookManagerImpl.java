package com.example.studysdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class BookManagerImpl extends Binder implements IBookManager {

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (((iin != null) && (iin instanceof IBookManager))) {
            return ((IBookManager) iin);
        }
        return new Proxy(obj);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case TRANSACTION_GET_BOOK_LIST:
                data.enforceInterface(DESCRIPTOR);
                List<Book> _result = getBookList();
                reply.writeNoException();
                // 将返回结果写入reply Parcel，使用writeTypedList方法，
                // 将List<Book>转换为Parcelable数组，并写入reply Parcel中。
                reply.writeTypedList(_result);
                return true;
            case TRANSACTION_ADD_BOOK:
                data.enforceInterface(DESCRIPTOR);
                Book _arg0 = _Parcel.readTypedObj(data, Book.CREATOR);
                addBook(_arg0);
                reply.writeNoException();
                return true;
            default:
                return super.onTransact(code, data, reply, flags);

        }

    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return Collections.emptyList();
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }

    public static class Proxy implements IBookManager {

        private final IBinder mRemote;

        public Proxy(IBinder obj) {
            mRemote = obj;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            // 获取两个Parcel对象，用于数据的序列化和反序列化
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();
            // 定义一个变量来存储方法调用的结果
            List<Book> _result;
            try {
                // 向_data Parcel中写入接口的描述符（DESCRIPTOR），这是为了标识这个调用是针对哪个接口的
                _data.writeInterfaceToken(DESCRIPTOR);
                // 调用mRemote（这是一个Binder对象，代表远程服务）的transact方法，
                // 发送一个请求到远程服务，请求执行getBook方法。
                // TRANSACTION_GET_BOOK_LIST 是一个整型常量，代表getUBook 方法的唯一标识符。
                // _data是包含请求数据的Parcel，_reply是用于接收响应数据的Parcel。
                // 最后一个参数是flags，通常设置为0。
                mRemote.transact(TRANSACTION_GET_BOOK_LIST, _data, _reply, 0);
                // 检查_reply Parcel中是否有异常发生
                _reply.readException();
                // 从_reply Parcel中读取getBook方法的返回结果，
                // createTypedArrayList方法使用Book类的CREATOR来反序列化数据。
                _result = _reply.createTypedArrayList(Book.CREATOR);
            } finally {
                _reply.recycle();
                _data.recycle();
            }
            return _result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                _Parcel.writeTypedObject(_data, book, 0);
                mRemote.transact(TRANSACTION_ADD_BOOK, _data, _reply, 0);
                _reply.readException();
            }
            finally {
                _reply.recycle();
                _data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }
    }

    static class _Parcel {
        // 泛型方法，用于从Parcel中读取一个实现了Parcelable接口的对象
        /**
         * @param parcel  Parcel对象，从中读取数据
         *                Creator对象，用于创建T类型的实例
         */
        private static <T> T readTypedObj(Parcel parcel, Parcelable.Creator <T> creator ) {
            // 从Parcel中读取一个整数，通常用作标志位，表示是否有数据可读
            if (parcel.readInt() == 0) {
                // 如果没有数据可读，返回null
                return null;
            } else {
                // 否则，使用Creator对象的createFromParcel方法从Parcel中读取一个对象，并返回
                return creator.createFromParcel(parcel);
            }

        }

        public static <T> void writeTypedObject(Parcel parcel, T object, int parcelableFlags) {
            // 如果对象为null，则写入一个标志位，表示没有对象可读
            if (object == null) {
                parcel.writeInt(0);
            } else {
                // 否则，写入一个标志位，表示有对象可读，并使用对象的writeToParcel方法将对象写入Parcel中
                parcel.writeInt(1);
                ((Parcelable) object).writeToParcel(parcel, parcelableFlags);
            }
        }
    }

}
