package com.sty.ne.pmssample.pms;

import android.content.pm.PackageInfo;

/**
 * @Author: tian
 * @UpdateDate: 2020-08-20 21:30
 */
public abstract class MyPackageManager {
    public abstract PackageInfo getPackageInfo(String packageName, int flags);
}
