package com.nuhkoca.udacitybakingapp.view.steps.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.databinding.ActivityStepsBinding;
import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.nuhkoca.udacitybakingapp.model.RecipeResponse;
import com.nuhkoca.udacitybakingapp.presenter.steps.activity.StepsActivityPresenter;
import com.nuhkoca.udacitybakingapp.presenter.steps.activity.StepsActivityPresenterImpl;
import com.nuhkoca.udacitybakingapp.view.steps.fragment.StepsFragment;

import java.util.List;

public class StepsActivity extends AppCompatActivity implements StepsActivityView {

    private ActivityStepsBinding mActivityStepsBinding;
    private StepsActivityPresenter mStepsActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityStepsBinding = DataBindingUtil.setContentView(this, R.layout.activity_steps);

        mStepsActivityPresenter = new StepsActivityPresenterImpl(this);
        mStepsActivityPresenter.invokeFirstRun();

        if (savedInstanceState == null) {
            mStepsActivityPresenter.attachFragment();
        }
    }

    @Override
    public void onFirstRun() {
        setSupportActionBar(mActivityStepsBinding.lStepsToolbar.toolbar);
        setTitle("");
        mActivityStepsBinding.lStepsToolbar.tvToolbarHeader.setText(getString(R.string.steps_header));

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onFragmentAttached() {
        RecipeResponse recipeResponse = getIntent().getParcelableExtra(Constants.RECIPE_MODEL_INTENT_EXTRA);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.flStepsFragmentHolder, StepsFragment.getInstance(recipeResponse))
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mStepsActivityPresenter.destroyView();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mStepsActivityPresenter = new StepsActivityPresenterImpl(this);

        super.onResume();
    }
}