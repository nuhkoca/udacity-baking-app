package com.nuhkoca.udacitybakingapp.view.ingredients.fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.nuhkoca.udacitybakingapp.provider.BakingContract;
import com.nuhkoca.udacitybakingapp.provider.BakingProvider;
import com.nuhkoca.udacitybakingapp.view.ingredients.adapter.IngredientsAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment implements IngredientsFragmentView {

    private FragmentIngredientsBinding mFragmentIngredientsBinding;
    private IngredientsFragmentPresenter mIngredientsFragmentPresenter;
    private RecipeResponse mRecipeResponse;
    private int mWhichItem;
    private static long mVideoPosition;
    private static String mRecipeName;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setHasOptionsMenu(true);
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onIngredientsLoaded() {
        if (getArguments() != null) {
            mRecipeResponse = getArguments().getParcelable(Constants.RECIPE_MODEL_INTENT_EXTRA);
            if (mRecipeResponse != null) {
                mRecipeName = mRecipeResponse.getName();
            }
            mWhichItem = getArguments().getInt(Constants.RECIPE_MODEL_STEPS_ID_INTENT_EXTRA);
        }

        int resId = 0;

        switch (mRecipeResponse.getName()) {
            case Constants.NUTELLA_PIE_CASE:
                resId = R.drawable.nutella_pie;
                break;

            case Constants.BROWNIES_CASE:
                resId = R.drawable.brownies;
                break;

            case Constants.YELLOW_CAKE_CASE:
                resId = R.drawable.yellow_cake;
                break;

            case Constants.CHEESECAKE_CASE:
                resId = R.drawable.cheesecake;
                break;

            default:
                break;
        }

        mFragmentIngredientsBinding.sepvIngredients.setDefaultArtwork(BitmapFactory.decodeResource(
                getResources(), resId));

        mShouldAutoPlay = true;
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

            if (!TextUtils.isEmpty(mRecipeResponse.getSteps().get(mWhichItem).getVideoURL())) {
                mExoPlayer.prepare(buildMediaSource(Uri.parse(mRecipeResponse.getSteps().get(mWhichItem).getVideoURL())));
            } else {
                mExoPlayer.prepare(buildMediaSource(Uri.parse(mRecipeResponse.getSteps().get(mWhichItem).getThumbnailURL())));
            }

            mExoPlayer.seekTo(mVideoPosition);
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
            mShouldAutoPlay = mExoPlayer.getPlayWhenReady();
            mVideoPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer = null;
            mTrackSelector = null;
        }
    }

    @Override
    public void onItemsAddedInDatabase() {
        List<String> quantities = new ArrayList<>();
        List<String> ingredients = new ArrayList<>();

        for (int i = 0; i < mRecipeResponse.getIngredients().size(); i++) {
            quantities.add(String.valueOf(mRecipeResponse.getIngredients().get(i).getQuantity()) + " "
                    + mRecipeResponse.getIngredients().get(i).getMeasure());

            ingredients.add(mRecipeResponse.getIngredients().get(i).getIngredient());
        }

        String quantityAndMeasure = TextUtils.join(", ", quantities);
        String ingredient = TextUtils.join(", ", ingredients);

        ContentValues contentValues = new ContentValues();
        contentValues.put(BakingContract.COLUMN_FOOD_NAME, mRecipeResponse.getName());
        contentValues.put(BakingContract.COLUMN_QUANTITY_MEASURE, quantityAndMeasure);
        contentValues.put(BakingContract.COLUMN_INGREDIENTS, ingredient);


        new MyCustomDatabaseSplicer(contentValues).execute();
        new MyCustomDatabaseSplicer1().execute();
    }

    private static class MyCustomDatabaseSplicer extends AsyncTask<Void, Void, Uri> {

        private ContentValues contentValues;

        MyCustomDatabaseSplicer(ContentValues contentValues) {
            this.contentValues = contentValues;
        }

        @Override
        protected Uri doInBackground(Void... voids) {
            try {
                return App.getInstance().getContentResolver().insert(BakingProvider.BakingIngredients.CONTENT_URI, contentValues);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Uri uri) {
            if (uri != null) {
                Toast.makeText(App.getInstance().getApplicationContext(),
                        String.format(App.getInstance().getString(R.string.added_to_widget), mRecipeName), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(App.getInstance().getApplicationContext(),
                        App.getInstance().getString(R.string.error_while_adding_to_widget), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class MyCustomDatabaseSplicer1 extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            try {
                return App.getInstance().getContentResolver().query(
                        BakingProvider.BakingIngredients.CONTENT_URI, null, null, null, null);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            cursor.moveToFirst();

            int foodId = cursor.getColumnIndex(BakingContract.COLUMN_FOOD_NAME);
            int quantityId = cursor.getColumnIndex(BakingContract.COLUMN_QUANTITY_MEASURE);
            int ingId = cursor.getColumnIndex(BakingContract.COLUMN_INGREDIENTS);

            while (!cursor.isAfterLast()) {
                String food = cursor.getString(foodId);
                String quantity = cursor.getString(quantityId);
                String ing = cursor.getString(ingId);

                Timber.d(food + " " + quantity + " " + ing);
                cursor.moveToNext();

            }
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(Constants.EXO_PLAYER_VIDEO_POSITION, mVideoPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mVideoPosition = savedInstanceState.getLong(Constants.EXO_PLAYER_VIDEO_POSITION);
        }
    }
}