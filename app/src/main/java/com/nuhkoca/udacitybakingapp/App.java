package com.nuhkoca.udacitybakingapp;

import android.app.Application;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by nuhkoca on 3/12/18.
 */

public class App extends Application {

    private static Application app;

    protected String mUserAgent;

    public static Application getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        Timber.plant(new Timber.DebugTree());
        LeakCanary.install(this);

        mUserAgent = Util.getUserAgent(this, Constants.EXO_PLAYER_USER_AGENT);

        deleteDatabase(Constants.DATABASE_NAME);

        app = this;
    }

    public DataSource.Factory buildDataSourceFactory(TransferListener<? super DataSource> listener) {
        return new DefaultDataSourceFactory(this, listener, buildHttpDataSourceFactory(listener));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(
            TransferListener<? super DataSource> listener) {
        return new DefaultHttpDataSourceFactory(mUserAgent, listener);
    }
}