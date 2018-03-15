package com.nuhkoca.udacitybakingapp.presenter.ingredients.fragment;

import com.nuhkoca.udacitybakingapp.view.ingredients.fragment.IngredientsFragmentView;

/**
 * Created by nuhkoca on 3/14/18.
 */

public class IngredientsFragmentPresenterImpl implements IngredientsFragmentPresenter {

    private IngredientsFragmentView mIngredientsFragmentView;

    public IngredientsFragmentPresenterImpl(IngredientsFragmentView mIngredientsFragmentView) {
        this.mIngredientsFragmentView = mIngredientsFragmentView;
    }

    @Override
    public void loadIngredients() {
        mIngredientsFragmentView.onIngredientsLoaded();
    }

    @Override
    public void initializePlayer() {
        mIngredientsFragmentView.onPlayerInitialized();
    }

    @Override
    public void releasePlayer() {
        mIngredientsFragmentView.onPlayerReleased();
    }

    @Override
    public void destroyView() {
        if (mIngredientsFragmentView != null) {
            mIngredientsFragmentView = null;
        }
    }
}