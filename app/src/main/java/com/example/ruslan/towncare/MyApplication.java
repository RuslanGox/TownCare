package com.example.ruslan.towncare;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ruslan on 10/06/2017.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getMyContext() {
        return context;
    }
}

