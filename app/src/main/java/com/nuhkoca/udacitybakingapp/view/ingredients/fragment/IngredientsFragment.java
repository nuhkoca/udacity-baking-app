package com.nuhkoca.udacitybakingapp.view.ingredients.fragment;


import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.nuhkoca.udacitybakingapp.App;
import com.nuhkoca.udacitybakingapp.BR;
import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.databinding.FragmentIngredientsBinding;
import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.nuhkoca.udacitybakingapp.model.RecipeResponse;
import com.nuhkoca.udacitybakingapp.presenter.ingredients.fragment.IngredientsFragmentPresenter;
import com.nuhkoca.udacitybakingapp.presenter.ingredients.fragment.IngredientsFragmentPresenterImpl;
import com.nuhkoca.udacitybakingapp.view.ingredients.adapter.IngredientsAdapter;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment implements IngredientsFragmentView {

    private FragmentIngredientsBinding mFragmentIngredientsBinding;
    private IngredientsFragmentPresenter mIngredientsFragmentPresenter;
    private RecipeResponse mRecipeResponse;
    private int mWhichItem;

    private SimpleExoPlayer mExoPlayer;
    private DataSource.Factory mMediaDataSourceFactory;
    private DefaultTrackSelector mTrackSelector;

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private boolean mShouldAutoPlay;

    public static IngredientsFragment getInstance(RecipeResponse recipeResponse, int whichItem) {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_MODEL_INTENT_EXTRA, recipeResponse);
        bundle.putInt(Constants.RECIPE_MODEL_STEPS_ID_INTENT_EXTRA, whichItem);

        ingredientsFragment.setArguments(bundle);

        return ingredientsFragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentIngredientsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false);

        return mFragmentIngredientsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mIngredientsFragmentPresenter = new IngredientsFragmentPresenterImpl(this);
        mIngredientsFragmentPresenter.loadIngredients();
        mIngredientsFragmentPresenter.initializePlayer();
    }

    @Override
    public void onIngredientsLoaded() {
        if (getArguments() != null) {
            mRecipeResponse = getArguments().getParcelable(Constants.RECIPE_MODEL_INTENT_EXTRA);
            mWhichItem = getArguments().getInt(Constants.RECIPE_MODEL_STEPS_ID_INTENT_EXTRA);
        }

        switch (mRecipeResponse.getName()) {
            case Constants.NUTELLA_PIE_CASE:
                mFragmentIngredientsBinding.sepvIngredients.setDefaultArtwork(BitmapFactory.decodeResource(
                        getResources(), R.drawable.nutella_pie));
                break;

            case Constants.BROWNIES_CASE:
                mFragmentIngredientsBinding.sepvIngredients.setDefaultArtwork(BitmapFactory.decodeResource(
                        getResources(), R.drawable.brownies));
                break;

            case Constants.YELLOW_CAKE_CASE:
                mFragmentIngredientsBinding.sepvIngredients.setDefaultArtwork(BitmapFactory.decodeResource(
                        getResources(), R.drawable.yellow_cake));
                break;

            case Constants.CHEESECAKE_CASE:
                mFragmentIngredientsBinding.sepvIngredients.setDefaultArtwork(BitmapFactory.decodeResource(
                        getResources(), R.drawable.cheesecake));
                break;

            default:
                break;
        }

        mShouldAutoPlay = false;
        mMediaDataSourceFactory = buildDataSourceFactory(true);
        mFragmentIngredientsBinding.sepvIngredients.requestFocus();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mFragmentIngredientsBinding.rvIngredients.setLayoutManager(linearLayoutManager);

        mFragmentIngredientsBinding.rvIngredients.setNestedScrollingEnabled(false);
        mFragmentIngredientsBinding.rvIngredients.setHasFixedSize(true);
        mFragmentIngredientsBinding.rvIngredients.setFocusable(false);

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(mRecipeResponse.getIngredients());
        mFragmentIngredientsBinding.rvIngredients.setAdapter(ingredientsAdapter);
        ingredientsAdapter.swapData();

        String formattedStepTitle;

        if (!mRecipeResponse.getSteps().get(mWhichItem).getVideoURL().equals("")) {
            formattedStepTitle = String.format(getString(R.string.step_title_place_holder),
                    mRecipeResponse.getSteps().get(mWhichItem).getShortDescription());
        } else if (!mRecipeResponse.getSteps().get(mWhichItem).getThumbnailURL().equals("")) {
            formattedStepTitle = String.format(getString(R.string.step_title_place_holder),
                    mRecipeResponse.getSteps().get(mWhichItem).getShortDescription());
        } else {
            formattedStepTitle = getString(R.string.step_title_error_place_holder);
        }

        String formattedIngredientCounter = String.format(getString(R.string.ingredients_counter_place_holder),
                mRecipeResponse.getIngredients().size());

        mFragmentIngredientsBinding.lStepsLayout.setVariable(BR.steps, mRecipeResponse.getSteps().get(mWhichItem));
        mFragmentIngredientsBinding.setVariable(BR.formattedIngredientCount, formattedIngredientCounter);
        mFragmentIngredientsBinding.lStepsLayout.setVariable(BR.formattedStepTitle, formattedStepTitle);

        mFragmentIngredientsBinding.lStepsLayout.executePendingBindings();
    }

    @Override
    public void onPlayerInitialized() {
        boolean needNewPlayer = mExoPlayer == null;

        if (needNewPlayer) {
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            mTrackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity());
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, mTrackSelector, loadControl);
            mFragmentIngredientsBinding.sepvIngredients.setPlayer(mExoPlayer);

            mExoPlayer.setPlayWhenReady(mShouldAutoPlay);

            if (!mRecipeResponse.getSteps().get(mWhichItem).getVideoURL().equals("")) {
                mExoPlayer.prepare(buildMediaSource(Uri.parse(mRecipeResponse.getSteps().get(mWhichItem).getVideoURL())));
            } else {
                mExoPlayer.prepare(buildMediaSource(Uri.parse(mRecipeResponse.getSteps().get(mWhichItem).getThumbnailURL())));
            }
        }
    }

    private DataSource.Factory buildDataSourceFactory(@SuppressWarnings("SameParameterValue") final boolean useBandwidthMeter) {
        return new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024);

                if (getActivity() != null && getContext() != null) {
                    SimpleCache simpleCache = new SimpleCache(new File(getActivity().getCacheDir(), "media_cache"), evictor);
                    return new CacheDataSource(simpleCache, ((App) getContext().getApplicationContext()).buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null).createDataSource(),
                            CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                            10 * 1024 * 1024);
                }

                return null;
            }
        };
    }

    private MediaSource buildMediaSource(
            Uri uri) {
        return new ExtractorMediaSource.Factory(mMediaDataSourceFactory)
                .createMediaSource(uri, null, null);
    }

    @Override
    public void onPlayerReleased() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer.release();
            mExoPlayer = null;
            mTrackSelector = null;
            mShouldAutoPlay = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            mIngredientsFragmentPresenter.initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            mIngredientsFragmentPresenter.initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            mIngredientsFragmentPresenter.releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            mIngredientsFragmentPresenter.releasePlayer();
        }
    }

    @Override
    public void onDetach() {
        mIngredientsFragmentPresenter.destroyView();

        super.onDetach();
    }
}