package com.geoverifi.geoverifi.app;

import android.app.*;

import com.squareup.otto.*;

/**
 * Created by chriz on 7/23/2017.
 */

public class MyApp extends Application {
    private static Bus mEventBus;

    public static Bus getBus(){
        return mEventBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus = new Bus(ThreadEnforcer.ANY);
    }
}
