package com.sty.ne.pmssample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.sty.ne.pmssample.pms.MyContext;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MyIPackageManager.Stub stub;

        // 系统的
        Context context = this;
        try {
            context.getPackageManager().getPackageInfo("", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 自己的
        MyContext myContext = new MyContext();
        myContext.getPackageManager().getPackageInfo("", 0);
    }
}
