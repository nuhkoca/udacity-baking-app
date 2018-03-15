package com.nuhkoca.udacitybakingapp.presenter.ingredients.fragment;

/**
 * Created by nuhkoca on 3/14/18.
 */

public interface IngredientsFragmentPresenter {
    void loadIngredients();

    void initializePlayer();

    void releasePlayer();

    void destroyView();
}
