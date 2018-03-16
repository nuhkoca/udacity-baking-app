package com.nuhkoca.udacitybakingapp.callback;

import com.nuhkoca.udacitybakingapp.model.RecipeResponse;

/**
 * Created by nuhkoca on 3/16/18.
 */

public interface IStepTabletCallbackListener {
    void onIngredientsScreenOpened(RecipeResponse recipeResponse, int whichItem, boolean isTablet);

    void onTutorialScreensActivated(boolean isTablet, int order);
}
