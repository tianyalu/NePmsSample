# `PMS`源码分析

[TOC]

## 一、基础理论

### 1.1 `PMS`概念

#### 1.1.1 什么是`PMS`

`PMS`全称为`PackageManagerService`，是`Android`系统中的一个核心服务，负责应用程序的**安装**、**卸载**和**应用程序信息查询、管理**等。

#### 1.1.2 `PMS`类关系图

![image](https://github.com/tianyalu/NePmsSample/raw/master/show/pms_invoke_process.png)

### 1.2 `PMS`模拟实现

由源码可知 [`IPackageManager`](https://www.androidos.net.cn/android/9.0.0_r8/xref//frameworks/base/core/java/android/content/pm/IPackageManager.aidl) 是个`AIDL`文件，涉及到跨进程通信，因为其实现类是系统生成的，无法看到具体内容，所以我们根据上图各类的关系模拟实现系统的相关类和方法。

#### 1.2.1 `MyIPackageManager`的`AIDL`文件

```java
import android.content.pm.PackageInfo;
interface MyIPackageManager {
    PackageInfo getPackageInfo(String packageName, int flags, int userId);
}
```

#### 1.2.1 `MyPackageManager`类

```java
public abstract class MyPackageManager {
    public abstract PackageInfo getPackageInfo(String packageName, int flags);
}
```

#### 1.2.3 `MyApplicationPackageManager`类

`MyApplicationPackageManager`是`MyPackageManager`的实现类：

```java
public class MyApplicationPackageManager extends MyPackageManager{
    private MyIPackageManager mPM;
    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) {
        try {
            return mPM.getPackageInfo(packageName, flags, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

#### 1.2.4 `MyContext`类

```java
public class MyContext {
    public MyPackageManager getPackageManager() {
        return new MyApplicationPackageManager();
    }
}
```

#### 1.2.5 `MyPackageManagerService`类

```java
//Binder机制的服务端，继承MyIPackageManager.Stub
public class MyPackageManagerService extends MyIPackageManager.Stub {
    @Override
    public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
        return null;
    }
}
```

#### 1.2.6 系统生成的`MyIPackageManager`的实现类

```java
public interface MyIPackageManager extends android.os.IInterface {
    /**
     * Default implementation for MyIPackageManager.
     */
    public static class Default implements com.sty.ne.pmssample.MyIPackageManager {
        @Override
        public android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException {
            return null;
        }

        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements com.sty.ne.pmssample.MyIPackageManager {
        private static final java.lang.String DESCRIPTOR = "com.sty.ne.pmssample.MyIPackageManager";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.sty.ne.pmssample.MyIPackageManager interface,
         * generating a proxy if needed.
         */
        public static com.sty.ne.pmssample.MyIPackageManager asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.sty.ne.pmssample.MyIPackageManager))) {
                return ((com.sty.ne.pmssample.MyIPackageManager) iin);
            }
            return new com.sty.ne.pmssample.MyIPackageManager.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

      	//4.服务端进程
        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_getPackageInfo: {
                    data.enforceInterface(descriptor);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    int _arg1;
                    _arg1 = data.readInt();
                    int _arg2;
                    _arg2 = data.readInt();
                  	//5.调用服务端的getPackageInfo方法
                    android.content.pm.PackageInfo _result = this.getPackageInfo(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if ((_result != null)) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

      	//2.该类为 MyIPackageManager 的实现类，该类重写了 getPackageInfo 方法
        //这里属于 Binder 机制的客户端
        private static class Proxy implements com.sty.ne.pmssample.MyIPackageManager {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException {
              	//Parcel 打包
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                android.content.pm.PackageInfo _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                  	//3.Binder 通讯，发送请求到服务端，跨进程调用
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageInfo, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().getPackageInfo(packageName, flags, userId);
                    }
                    _reply.readException();
                    if ((0 != _reply.readInt())) {
                        _result = android.content.pm.PackageInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public static com.sty.ne.pmssample.MyIPackageManager sDefaultImpl;
        }

        static final int TRANSACTION_getPackageInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);

        public static boolean setDefaultImpl(com.sty.ne.pmssample.MyIPackageManager impl) {
            if (Stub.Proxy.sDefaultImpl == null && impl != null) {
                Stub.Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static com.sty.ne.pmssample.MyIPackageManager getDefaultImpl() {
            return Stub.Proxy.sDefaultImpl;
        }
    }

  	//1.代码最终会调用到这里
    public android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException;
}
```

### 1.3 总结

`PMS`服务调用流程总结如下图所示：

![image](https://github.com/tianyalu/NePmsSample/raw/master/show/my_pms_invoke_process.png)