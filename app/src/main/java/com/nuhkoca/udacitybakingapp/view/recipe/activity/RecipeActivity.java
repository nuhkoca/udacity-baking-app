package com.nuhkoca.udacitybakingapp.view.recipe.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.nuhkoca.udacitybakingapp.ErrorFragment;
import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.callback.IErrorCallbackListener;
import com.nuhkoca.udacitybakingapp.databinding.ActivityRecipeBinding;
import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.nuhkoca.udacitybakingapp.presenter.recipe.activity.RecipeActivityPresenter;
import com.nuhkoca.udacitybakingapp.presenter.recipe.activity.RecipeActivityPresenterImpl;
import com.nuhkoca.udacitybakingapp.view.about.AboutActivity;
import com.nuhkoca.udacitybakingapp.view.recipe.fragment.RecipeFragment;

public class RecipeActivity extends AppCompatActivity implements RecipeActivityView, IErrorCallbackListener {

    private ActivityRecipeBinding mActivityRecipeBinding;
    private RecipeActivityPresenter mRecipeActivityPresenter;

    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRecipeBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);

        mRecipeActivityPresenter = new RecipeActivityPresenterImpl(this);
        mRecipeActivityPresenter.invokeFirstRun();

        if (savedInstanceState == null) {
            mRecipeActivityPresenter.attachFragment();
        }
    }

    @Override
    public void onFragmentAttached() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flRecipesHolder, RecipeFragment.getInstance(this))
                .commit();
    }

    @Override
    public void onFirstRun() {
        setSupportActionBar(mActivityRecipeBinding.lRecipeToolbar.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();

        switch (itemThatWasClicked) {
            case R.id.main_about:
                Intent aboutIntent = new Intent(RecipeActivity.this, AboutActivity.class);
                startActivityForResult(aboutIntent, Constants.CHILD_ACTIVITY_REQUEST_CODE);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int timeDelay = getResources().getInteger(R.integer.delay_in_seconds_to_close);

        if (mBackPressed + timeDelay > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.twice_press_to_exit),
                    Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecipeActivityPresenter = new RecipeActivityPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        mRecipeActivityPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onErrorScreenShown(boolean visible) {
        if (visible) {
            if (getResources().getBoolean(R.bool.isTablet)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flRecipesHolder, ErrorFragment.getInstance(this))
                    .commit();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flRecipesHolder, RecipeFragment.getInstance(this))
                    .commit();
        }
    }
}