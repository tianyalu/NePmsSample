package com.sty.ne.pmssample.pms;

/**
 * @Author: tian
 * @UpdateDate: 2020-08-20 21:36
 */
public class MyContext {

    public MyPackageManager getPackageManager() {
        return new MyApplicationPackageManager();
    }
}
