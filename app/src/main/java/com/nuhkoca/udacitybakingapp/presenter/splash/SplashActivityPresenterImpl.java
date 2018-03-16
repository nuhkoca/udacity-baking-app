package com.nuhkoca.udacitybakingapp.presenter.splash;

import android.os.Handler;

import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.nuhkoca.udacitybakingapp.view.splash.SplashActivityView;


/**
 * Created by nuhkoca on 3/13/18.
 */

public class SplashActivityPresenterImpl implements SplashActivityPresenter {

    private SplashActivityView mSplashActivityView;
    private int mDuration;

    public SplashActivityPresenterImpl(SplashActivityView mSplashActivityView) {
        this.mSplashActivityView = mSplashActivityView;
    }

    @Override
    public void openActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSplashActivityView.onActivityOpened();
            }
        }, Constants.ACTIVITY_OPEN_DURATION);
    }

    @Override
    public void destroyView() {
        if (mSplashActivityView != null) {
            mSplashActivityView = null;
        }
    }
}