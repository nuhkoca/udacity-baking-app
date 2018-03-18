package com.nuhkoca.udacitybakingapp.view.ingredients.fragment;

/**
 * Created by nuhkoca on 3/14/18.
 */

public interface IngredientsFragmentView {
    void onIngredientsLoaded();

    void onPlayerInitialized();

    void onPlayerReleased();

    void onItemsAddedInDatabase();

    void onItemsRemovedFromDatabase();
}
