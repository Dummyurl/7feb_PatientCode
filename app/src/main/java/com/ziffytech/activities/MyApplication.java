package com.ziffytech.activities;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.ziffytech.remainder.Reminder;


/**
 * Created by LENOVO on 8/18/2016.
 */
public class MyApplication extends MultiDexApplication {


    public static Reminder remainder;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();


    }
}
