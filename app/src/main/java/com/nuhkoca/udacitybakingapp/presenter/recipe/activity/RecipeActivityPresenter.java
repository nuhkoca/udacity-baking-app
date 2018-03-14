package com.nuhkoca.udacitybakingapp.presenter.recipe.activity;

/**
 * Created by nuhkoca on 3/13/18.
 */

public interface RecipeActivityPresenter {
    void attachFragment();

    void invokeFirstRun();

    void destroyView();
}
