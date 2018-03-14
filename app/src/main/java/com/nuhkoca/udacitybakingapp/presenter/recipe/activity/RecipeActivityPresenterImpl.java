package com.nuhkoca.udacitybakingapp.presenter.recipe.activity;

import com.nuhkoca.udacitybakingapp.view.recipe.activity.RecipeActivityView;


/**
 * Created by nuhkoca on 3/13/18.
 */

public class RecipeActivityPresenterImpl implements RecipeActivityPresenter {

    private RecipeActivityView mRecipeActivityView;

    public RecipeActivityPresenterImpl(RecipeActivityView mRecipeActivityView) {
        this.mRecipeActivityView = mRecipeActivityView;
    }

    @Override
    public void attachFragment() {
        mRecipeActivityView.onFragmentAttached();
    }

    @Override
    public void invokeFirstRun() {
        mRecipeActivityView.onFirstRun();
    }

    @Override
    public void destroyView() {
        if (mRecipeActivityView != null) {
            mRecipeActivityView = null;
        }
    }
}