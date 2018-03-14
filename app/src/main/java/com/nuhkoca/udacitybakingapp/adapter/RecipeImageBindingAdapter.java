package com.nuhkoca.udacitybakingapp.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nuhkoca.udacitybakingapp.App;
import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.helper.Constants;
import com.nuhkoca.udacitybakingapp.module.GlideApp;

import java.util.Objects;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by nuhkoca on 3/13/18.
 */

public class RecipeImageBindingAdapter {

    @BindingAdapter(value = {"android:src", "pbRecipe", "rlRecipe", "recipeName"})
    public static void bindRecipeImage(final ImageView recipeImageView, String image, ProgressBar pbRecipe, final RelativeLayout rlRecipe, String recipeName) {
        if (TextUtils.isEmpty(image)) {

            Drawable resId = null;
            String[] recipeImages = App.getInstance().getResources().getStringArray(R.array.recipeNames);
            Integer[] recipeIds = {R.drawable.nutella_pie, R.drawable.brownies, R.drawable.yellow_cake, R.drawable.cheesecake};

            for (int i = 0; i < recipeImages.length; i++) {
                if (Objects.equals(recipeName, recipeImages[i])) {
                    resId = ContextCompat.getDrawable(recipeImageView.getContext(), recipeIds[i]);
                }
            }

            GlideApp.with(recipeImageView.getContext())
                    .load(resId)
                    .listener(requestListener(pbRecipe))
                    .into(recipeImageView);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (recipeImageView.getDrawable() != null) {
                        Blurry.with(recipeImageView.getContext())
                                .radius(Constants.RADIUS_BLUR)
                                .sampling(Constants.SAMPLING_BLUR)
                                .animate(Constants.ANIMATE_BLUR)
                                .async()
                                .capture(rlRecipe)
                                .into(recipeImageView);
                    }
                }
            }, Constants.MILLISECOND_TO_BLUR);
        }
    }

    private static RequestListener<Drawable> requestListener(final ProgressBar pbRecipe) {
        return new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                pbRecipe.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                pbRecipe.setVisibility(View.GONE);
                return false;
            }
        };
    }
}