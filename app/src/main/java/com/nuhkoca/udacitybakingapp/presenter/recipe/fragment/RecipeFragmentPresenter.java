package com.nuhkoca.udacitybakingapp.presenter.recipe.fragment;

/**
 * Created by nuhkoca on 3/13/18.
 */

public interface RecipeFragmentPresenter {
    void fetchRecipes();

    void behaveAfterRotation();

    void destroyView();
}
