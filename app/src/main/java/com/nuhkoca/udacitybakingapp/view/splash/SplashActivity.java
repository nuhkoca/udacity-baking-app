package com.nuhkoca.udacitybakingapp.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nuhkoca.udacitybakingapp.presenter.splash.SplashActivityPresenter;
import com.nuhkoca.udacitybakingapp.presenter.splash.SplashActivityPresenterImpl;
import com.nuhkoca.udacitybakingapp.view.recipe.activity.RecipeActivity;

public class SplashActivity extends AppCompatActivity implements SplashActivityView {

    private SplashActivityPresenter mSplashActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSplashActivityPresenter = new SplashActivityPresenterImpl(this);
        mSplashActivityPresenter.openActivity();
    }

    @Override
    public void onActivityOpened() {
        Intent recipeIntent = new Intent(SplashActivity.this, RecipeActivity.class);
        recipeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        recipeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(recipeIntent);
    }

    @Override
    protected void onDestroy() {
        mSplashActivityPresenter.destroyView();
        super.onDestroy();
    }
}