package com.nuhkoca.udacitybakingapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by nuhkoca on 3/12/18.
 */

public class App extends Application {

    private static Application app;

    public static Application getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }

        Timber.plant(new Timber.DebugTree());
        LeakCanary.install(this);

        app = this;
    }
}