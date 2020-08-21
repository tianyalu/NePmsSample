// MyIPackageManager.aidl
package com.sty.ne.pmssample;

import android.content.pm.PackageInfo;

interface MyIPackageManager {
    PackageInfo getPackageInfo(String packageName, int flags, int userId);
}
