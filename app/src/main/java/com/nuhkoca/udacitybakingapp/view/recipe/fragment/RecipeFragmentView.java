package com.nuhkoca.udacitybakingapp.view.recipe.fragment;

import com.nuhkoca.udacitybakingapp.model.RecipeResponse;

import java.util.List;

/**
 * Created by nuhkoca on 3/13/18.
 */

public interface RecipeFragmentView {
    void onRecipesLoaded(List<RecipeResponse> recipeResponses);

    void onRecipesLoadingFailed(String message);

    void onScreenRotated();

    void onProgressVisibility(boolean visible);
}
