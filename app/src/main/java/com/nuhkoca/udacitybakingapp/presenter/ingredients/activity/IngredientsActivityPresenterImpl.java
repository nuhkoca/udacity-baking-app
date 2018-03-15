package com.nuhkoca.udacitybakingapp.presenter.ingredients.activity;

import com.nuhkoca.udacitybakingapp.view.ingredients.activity.IngredientsActivityView;

/**
 * Created by nuhkoca on 3/14/18.
 */

public class IngredientsActivityPresenterImpl implements IngredientsActivityPresenter {

    private IngredientsActivityView mIngredientsActivityView;

    public IngredientsActivityPresenterImpl(IngredientsActivityView mIngredientsActivityView) {
        this.mIngredientsActivityView = mIngredientsActivityView;
    }

    @Override
    public void invokeFirstRun() {
        mIngredientsActivityView.onFirstRun();
    }

    @Override
    public void attachFragment() {
        mIngredientsActivityView.onFragmentAttached();
    }

    @Override
    public void destroyView() {
        if (mIngredientsActivityView != null) {
            mIngredientsActivityView = null;
        }
    }
}