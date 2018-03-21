package com.nuhkoca.udacitybakingapp.view.ingredients.fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ThumbnailImageViewTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
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
import com.nuhkoca.udacitybakingapp.helper.DatabaseHandler;
import com.nuhkoca.udacitybakingapp.model.RecipeResponse;
import com.nuhkoca.udacitybakingapp.module.GlideApp;
import com.nuhkoca.udacitybakingapp.presenter.ingredients.fragment.IngredientsFragmentPresenter;
import com.nuhkoca.udacitybakingapp.presenter.ingredients.fragment.IngredientsFragmentPresenterImpl;
import com.nuhkoca.udacitybakingapp.provider.BakingContract;
import com.nuhkoca.udacitybakingapp.test.SimpleIdlingResource;
import com.nuhkoca.udacitybakingapp.util.ConfigurationDetector;
import com.nuhkoca.udacitybakingapp.util.ConnectionSniffer;
import com.nuhkoca.udacitybakingapp.util.SnackbarPopper;
import com.nuhkoca.udacitybakingapp.view.ingredients.adapter.IngredientsAdapter;
import com.nuhkoca.udacitybakingapp.view.recipe.fragment.RecipeFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment implements IngredientsFragmentView {

    private FragmentIngredientsBinding mFragmentIngredientsBinding;
    private IngredientsFragmentPresenter mIngredientsFragmentPresenter;
    private RecipeResponse mRecipeResponse;
    private int mWhichItem;
    private int mResId = 0;
    private static long mVideoPosition;
    private static boolean mShouldAutoPlay;

    private SimpleExoPlayer mExoPlayer;
    private DataSource.Factory mMediaDataSourceFactory;
    private DefaultTrackSelector mTrackSelector;

    private int mOrientation;

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    @NonNull
    public static IngredientsFragment getInstance() {
        return new IngredientsFragment();
    }

    public static IngredientsFragment getInstance(RecipeResponse recipeResponse, int whichItem) {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_MODEL_INTENT_EXTRA, recipeResponse);
        bundle.putInt(Constants.RECIPE_MODEL_STEPS_ID_INTENT_EXTRA, whichItem);

        ingredientsFragment.setArguments(bundle);

        return ingredientsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIdlingResource();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentIngredientsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false);

        if (savedInstanceState == null) {
            mShouldAutoPlay = true;
        }

        setHasOptionsMenu(true);
        mOrientation = ConfigurationDetector.isTabletInLandscapeMode(getActivity());

        return mFragmentIngredientsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mIngredientsFragmentPresenter = new IngredientsFragmentPresenterImpl(this);
        mIngredientsFragmentPresenter.loadIngredients();
        mIngredientsFragmentPresenter.initializePlayer();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ingredient_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();

        switch (itemThatWasClicked) {
            case R.id.add_widget:
                mIngredientsFragmentPresenter.addItemsInDatabase();
                return true;

            case R.id.remove_widget:
                mIngredientsFragmentPresenter.removeItemsFromDatabase();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onIngredientsLoaded() {
        if (getArguments() != null) {
            mRecipeResponse = getArguments().getParcelable(Constants.RECIPE_MODEL_INTENT_EXTRA);
            mWhichItem = getArguments().getInt(Constants.RECIPE_MODEL_STEPS_ID_INTENT_EXTRA);
        }

        switch (mRecipeResponse.getName()) {
            case Constants.NUTELLA_PIE_CASE:
                mResId = R.drawable.nutella_pie;
                break;

            case Constants.BROWNIES_CASE:
                mResId = R.drawable.brownies;
                break;

            case Constants.YELLOW_CAKE_CASE:
                mResId = R.drawable.yellow_cake;
                break;

            case Constants.CHEESECAKE_CASE:
                mResId = R.drawable.cheesecake;
                break;

            default:
                break;
        }

        if (!TextUtils.isEmpty(mRecipeResponse.getSteps().get(mWhichItem).getVideoURL())) {
            mFragmentIngredientsBinding.ivIngredients.setVisibility(View.GONE);
            mFragmentIngredientsBinding.pvIngredients.setVisibility(View.VISIBLE);
            mFragmentIngredientsBinding.pvIngredients.setUseController(true);

        } else {

            if (!getResources().getBoolean(R.bool.isTablet) || (getResources().getBoolean(R.bool.isTablet)
                    || mOrientation == Configuration.ORIENTATION_PORTRAIT)) {

                if (getActivity() != null) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
            } else {
                if (getActivity() != null) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
            }

            if (TextUtils.isEmpty(mRecipeResponse.getSteps().get(mWhichItem).getThumbnailURL())) {
                mFragmentIngredientsBinding.ivIngredients.setVisibility(View.GONE);
                mFragmentIngredientsBinding.pvIngredients.setVisibility(View.VISIBLE);
                mFragmentIngredientsBinding.pvIngredients.setUseController(false);

                mFragmentIngredientsBinding.pvIngredients.setDefaultArtwork(BitmapFactory.decodeResource(
                        getResources(), mResId));
            } else {
                mFragmentIngredientsBinding.ivIngredients.setVisibility(View.VISIBLE);
                mFragmentIngredientsBinding.pvIngredients.setVisibility(View.GONE);

                if (getActivity() != null) {
                    GlideApp.with(getActivity())
                            .load(mRecipeResponse.getSteps().get(mWhichItem).getThumbnailURL())
                            .into(mFragmentIngredientsBinding.ivIngredients);
                }
            }
        }

        mMediaDataSourceFactory = buildDataSourceFactory(true);
        mFragmentIngredientsBinding.pvIngredients.requestFocus();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mFragmentIngredientsBinding.rvIngredients.setLayoutManager(linearLayoutManager);

        mFragmentIngredientsBinding.rvIngredients.setNestedScrollingEnabled(false);
        mFragmentIngredientsBinding.rvIngredients.setHasFixedSize(true);
        mFragmentIngredientsBinding.rvIngredients.setFocusable(false);

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(mRecipeResponse.getIngredients());
        mFragmentIngredientsBinding.rvIngredients.setAdapter(ingredientsAdapter);
        ingredientsAdapter.swapData();

        String formattedStepTitle;

        if (!TextUtils.isEmpty(mRecipeResponse.getSteps().get(mWhichItem).getVideoURL())) {
            formattedStepTitle = String.format(getString(R.string.step_title_place_holder),
                    mRecipeResponse.getSteps().get(mWhichItem).getShortDescription());
        } else {
            formattedStepTitle = String.format(getString(R.string.step_title_error_place_holder), mRecipeResponse.getSteps().get(mWhichItem).getShortDescription());
        }

        String formattedIngredientCounter = String.format(getString(R.string.ingredients_counter_place_holder),
                mRecipeResponse.getIngredients().size());

        mFragmentIngredientsBinding.lStepsLayout.setVariable(BR.steps, mRecipeResponse.getSteps().get(mWhichItem));
        mFragmentIngredientsBinding.setVariable(BR.formattedIngredientCount, formattedIngredientCounter);
        mFragmentIngredientsBinding.lStepsLayout.setVariable(BR.formattedStepTitle, formattedStepTitle);

        mFragmentIngredientsBinding.lStepsLayout.executePendingBindings();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
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
            mFragmentIngredientsBinding.pvIngredients.setPlayer(mExoPlayer);

            mExoPlayer.setPlayWhenReady(mShouldAutoPlay);


            String videoUrl = "";
            if (!TextUtils.isEmpty(mRecipeResponse.getSteps().get(mWhichItem).getVideoURL())) {
                videoUrl = mRecipeResponse.getSteps().get(mWhichItem).getVideoURL();
            }

            mExoPlayer.prepare(buildMediaSource(Uri.parse(videoUrl)));
            mExoPlayer.seekTo(mVideoPosition);
        }

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
    }

    private DataSource.Factory buildDataSourceFactory(@SuppressWarnings("SameParameterValue") final boolean useBandwidthMeter) {
        return new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(Constants.EXO_PLAYER_VIDEO_CACHE_DURATION);

                if (getActivity() != null && getContext() != null) {
                    SimpleCache simpleCache = new SimpleCache(new File(getActivity().getCacheDir(), "media_cache"), evictor);
                    return new CacheDataSource(simpleCache, ((App) getContext().getApplicationContext()).buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null).createDataSource(),
                            CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                            Constants.EXO_PLAYER_VIDEO_CACHE_DURATION);
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
            mShouldAutoPlay = mExoPlayer.getPlayWhenReady();
            mVideoPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer = null;
            mTrackSelector = null;
        }
    }

    @Override
    public void onItemsAddedInDatabase() {
        List<String> quantities = new ArrayList<>();

        for (int i = 0; i < mRecipeResponse.getIngredients().size(); i++) {
            quantities.add(String.valueOf(mRecipeResponse.getIngredients().get(i).getQuantity()) + " "
                    + mRecipeResponse.getIngredients().get(i).getMeasure() + " x " +
                    mRecipeResponse.getIngredients().get(i).getIngredient());
        }

        String quantityAndMeasure = TextUtils.join("\n", quantities);

        ContentValues contentValues = new ContentValues();
        contentValues.put(BakingContract.COLUMN_FOOD_NAME, mRecipeResponse.getName());
        contentValues.put(BakingContract.COLUMN_QUANTITY_MEASURE_INGREDIENTS, quantityAndMeasure);

        Uri returnedValue = null;

        try {
            returnedValue = new DatabaseHandler.Splicer(contentValues).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Timber.d(Arrays.toString(e.getStackTrace()));
        }

        if (returnedValue != null) {
            SnackbarPopper.pop(mFragmentIngredientsBinding.llIngredientsMain,
                    String.format(getString(R.string.added_to_widget), mRecipeResponse.getName()));
        } else {
            SnackbarPopper.pop(mFragmentIngredientsBinding.llIngredientsMain,
                    getString(R.string.error_while_adding_to_widget));
        }
    }

    @Override
    public void onItemsRemovedFromDatabase() {
        int returnedValue = 0;

        try {
            returnedValue = new DatabaseHandler.
                    Remover(mRecipeResponse.getName()).
                    execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Timber.d(Arrays.toString(e.getStackTrace()));
        }

        if (returnedValue > 0) {
            SnackbarPopper.pop(mFragmentIngredientsBinding.llIngredientsMain,
                    String.format(getString(R.string.remove_from_widget), mRecipeResponse.getName()));
        } else {
            SnackbarPopper.pop(mFragmentIngredientsBinding.llIngredientsMain,
                    getString(R.string.error_while_removing_from_widget));
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

        mIngredientsFragmentPresenter = new IngredientsFragmentPresenterImpl(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIngredientsFragmentPresenter = new IngredientsFragmentPresenterImpl(this);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(Constants.EXO_PLAYER_VIDEO_POSITION, mVideoPosition);
        outState.putBoolean(Constants.EXO_PLAYER_PLAYING_STATE, mShouldAutoPlay);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mVideoPosition = savedInstanceState.getLong(Constants.EXO_PLAYER_VIDEO_POSITION);
            mShouldAutoPlay = savedInstanceState.getBoolean(Constants.EXO_PLAYER_PLAYING_STATE);
        }
    }
}