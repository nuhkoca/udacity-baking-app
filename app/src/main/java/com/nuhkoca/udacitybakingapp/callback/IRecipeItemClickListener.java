package com.nuhkoca.udacitybakingapp.callback;

import com.nuhkoca.udacitybakingapp.model.RecipeResponse;

/**
 * Created by nuhkoca on 3/13/18.
 */

public interface IRecipeItemClickListener {
    void onRecipeItemClick(RecipeResponse recipeResponse);
}