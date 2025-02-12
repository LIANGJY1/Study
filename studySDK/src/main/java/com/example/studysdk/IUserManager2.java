/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.example.studysdk;
// Declare any non-default types here with import statements

/**
 * aidl 生成的 IUserManager，此处复制过来作为学习使用
 */
// Binder 中传输的接口都要实现 IInterface
public interface IUserManager2 extends android.os.IInterface
{
  // DESCRIPTOR 为唯一标识 Binder 的字符串。包名 + 接口名。标识 IUserManager
  public static final String DESCRIPTOR = "com.example.studysdk.IUserManager";

  /** Default implementation for IUserManager. */
  // IUserManager 默认的、空的实现。可作为占位符
  public static class Default implements IUserManager2
  {
    @Override public java.util.List<User> getUser() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void addUser(User user) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }

  // IUserManager.aidl 中接口方法声明
  public java.util.List<User> getUser() throws android.os.RemoteException;
  public void addUser(User user) throws android.os.RemoteException;

  /** Local-side IPC implementation stub class. */
  // Stub 是一个 Binder 类。
    // 当客户端和服务端都位于同一个进程时，方法调用不会走跨进程的 transact 过程
    // 而当两者位于不同进程时，方法调用需要走 transact 过程，这个逻辑由 Stub 的内部代理类 Proxy 来完成。
  public static abstract class Stub extends android.os.Binder implements IUserManager2
  {
    /**
     * Cast an IBinder object into an com.example.studysdk.IUserManager interface,
     * generating a proxy if needed.
     */
    // 用于将服务端的 Binder 对象转换成客户端所需的 AIDL 接口类型的对象
    // 位于同一进程，返回服务端 Stub 对象本身
    // 位于不同进程，返回系统封装后的 Stub.proxy 对象
    public static IUserManager2 asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      // queryLocalInterface  用于检索与当前 IBinder 对象关联的本地接口实现。
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      // 返回的 IInterface 对象，代表了与当前 IBinder 对象关联的本地接口实现。如果本地没有实现该接口，则返回 null。
      if (((iin!=null)&&(iin instanceof IUserManager2))) {
        return ((IUserManager2)iin);
      }
      return new Proxy(obj);
    }

    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      // 将 Stub 对象与 DESCRIPTOR 关联起来。Stub 为实现 IUserManger 接口的对象。
      // 所以当这个 Stub 对象被传递到另一个进程中时，接收方可以通过这个描述符来识别它实现了哪个接口，并据此来调用接口中的方法。
      this.attachInterface(this, DESCRIPTOR);
    }


    // 用于获取一个接口的 IBinder 对象
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }

    // 该方法运行在服务端中的 Binder 线程池中，当客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此方法来处理。
    // 果此方法返回 false,那么客户端的请求会失败，因此我们可以利用这个特性来做权限验证，毕竟我们也不希望随便一个进程都能远程调用我们的服务。
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      String descriptor = DESCRIPTOR;
      // 检查 code 是否在有效的跨进程调用范围内
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      // 服务端通过 code 可以确定客户端所请求的目标方法是什么
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
      }
      switch (code)
      {
        case TRANSACTION_getUser:
        {
          java.util.List<User> _result = this.getUser();
          reply.writeNoException();
          // 当目标方法执行完毕后，就向 reply 中写入返回值(如果目标方法有返回值的话)。
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_addUser:
        {
          // 从 data 中取出目标方法所需的参数(如果目标方法有参数的话)，然后执行目标方法。
          User _arg0;
          _arg0 = _Parcel.readTypedObject(data, User.CREATOR);
          this.addUser(_arg0);
          reply.writeNoException();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }

    private static class Proxy implements IUserManager2
    {
      private android.os.IBinder mRemote;
      // Proxy 就是 ServiceManagerProxy，而 remote 就是 BinderProxy
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public java.util.List<User> getUser() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<User> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUser, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(User.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void addUser(User user) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, user, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }

    // id 交易码，标识方法。两个 id 用于标识在 transact 过程中客户端所请求的到底是哪个方法。
    // android.os.IBinder.FIRST_CALL_TRANSACTION   表示用户自定义交易码的起始值。任何通过AIDL定义并需要跨进程调用的方法都会被分配一个从FIRST_CALL_TRANSACTION开始的唯一交易码。
    static final int TRANSACTION_getUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }

  // _Parcel类中的这些方法为AIDL通信提供了基础的数据序列化和反序列化支持。
  // 通过这些方法，可以方便地将Parcelable对象和对象列表在进程间传输，是Android IPC机制中的一个重要部分。
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedList(
        android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
      if (value == null) {
        parcel.writeInt(-1);
      } else {
        int N = value.size();
        int i = 0;
        parcel.writeInt(N);
        while (i < N) {
    writeTypedObject(parcel, value.get(i), parcelableFlags);
          i++;
        }
      }
    }
  }
}
