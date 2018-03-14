package com.nuhkoca.udacitybakingapp.presenter.about;

import com.nuhkoca.udacitybakingapp.view.about.AboutActivityView;

/**
 * Created by nuhkoca on 3/14/18.
 */

public class AboutActivityPresenterImpl implements AboutActivityPresenter {

    private AboutActivityView mAboutActivityView;

    public AboutActivityPresenterImpl(AboutActivityView mAboutActivityView) {
        this.mAboutActivityView = mAboutActivityView;
    }

    @Override
    public void invokeFirstRun() {
        mAboutActivityView.onFirstRun();
    }

    @Override
    public void prepareInfo() {
        mAboutActivityView.onInfoReady();
    }

    @Override
    public void destroyView() {
        if (mAboutActivityView != null) {
            mAboutActivityView = null;
        }
    }
}