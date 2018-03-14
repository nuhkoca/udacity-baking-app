package com.nuhkoca.udacitybakingapp.network;

import com.nuhkoca.udacitybakingapp.model.RecipeResponse;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nuhkoca on 3/13/18.
 */

public interface IBakingAPI {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Observable<List<RecipeResponse>> getRecipes();
}