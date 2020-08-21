package com.sty.ne.pmssample.pms;

import android.content.pm.PackageInfo;
import android.os.RemoteException;

import com.sty.ne.pmssample.MyIPackageManager;

/**
 * @Author: tian
 * @UpdateDate: 2020-08-20 21:48
 */
//Binder机制的服务端
public class MyPackageManagerService extends MyIPackageManager.Stub {
    @Override
    public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
        return null;
    }
}
