package com.sty.ne.pmssample.pms;

import android.content.pm.PackageInfo;
import android.os.RemoteException;

import com.sty.ne.pmssample.MyIPackageManager;

/**
 * @Author: tian
 * @UpdateDate: 2020-08-20 21:31
 */
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
