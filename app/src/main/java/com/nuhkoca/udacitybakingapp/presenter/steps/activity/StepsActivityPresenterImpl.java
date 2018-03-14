package com.nuhkoca.udacitybakingapp.presenter.steps.activity;

import com.nuhkoca.udacitybakingapp.view.steps.activity.StepsActivityView;

/**
 * Created by nuhkoca on 3/14/18.
 */

public class StepsActivityPresenterImpl implements StepsActivityPresenter {

    private StepsActivityView mStepsActivityView;

    public StepsActivityPresenterImpl(StepsActivityView mStepsActivityView) {
        this.mStepsActivityView = mStepsActivityView;
    }

    @Override
    public void invokeFirstRun() {
        mStepsActivityView.onFirstRun();
    }

    @Override
    public void attachFragment() {
        mStepsActivityView.onFragmentAttached();
    }

    @Override
    public void destroyView() {
        if (mStepsActivityView != null) {
            mStepsActivityView = null;
        }
    }
}