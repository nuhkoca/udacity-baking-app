package com.nuhkoca.udacitybakingapp.helper;

import com.nuhkoca.udacitybakingapp.model.RecipeResponse;
import com.nuhkoca.udacitybakingapp.network.IBakingAPI;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;


/**
 * Created by nuhkoca on 3/13/18.
 */

public class ObservableHelper {

    private static IBakingAPI getIBakingAPI() {
        return getRetrofit().create(IBakingAPI.class);
    }

    private static Retrofit getRetrofit() {
        return RetrofitInterceptor.build();
    }

    public static Observable<List<RecipeResponse>> getRecipes() {
        IBakingAPI mIBakingAPI = getIBakingAPI();

        return mIBakingAPI.getRecipes();
    }
}