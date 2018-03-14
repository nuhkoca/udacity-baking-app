package com.nuhkoca.udacitybakingapp.view.recipe.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuhkoca.udacitybakingapp.BR;
import com.nuhkoca.udacitybakingapp.R;
import com.nuhkoca.udacitybakingapp.callback.IRecipeItemClickListener;
import com.nuhkoca.udacitybakingapp.databinding.RecipeItemCardBinding;
import com.nuhkoca.udacitybakingapp.model.RecipeResponse;

import java.util.List;

/**
 * Created by nuhkoca on 3/14/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<RecipeResponse> mRecipeResponse;
    private IRecipeItemClickListener mIRecipeItemClickListener;

    public RecipeAdapter(List<RecipeResponse> mRecipeResponse, IRecipeItemClickListener mIRecipeItemClickListener) {
        this.mRecipeResponse = mRecipeResponse;
        this.mIRecipeItemClickListener = mIRecipeItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        RecipeItemCardBinding recipeItemCardBinding = DataBindingUtil.inflate(inflater,
                R.layout.recipe_item_card,
                parent,
                false);

        return new ViewHolder(recipeItemCardBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeResponse recipeResponse = mRecipeResponse.get(position);

        holder.bindViews(recipeResponse);
        holder.checkViews();
    }

    public void swapData() {
        if (mRecipeResponse.size() == 0)
            return;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRecipeResponse.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RecipeItemCardBinding mRecipeItemCardBinding;

        ViewHolder(View itemView) {
            super(itemView);

            mRecipeItemCardBinding = DataBindingUtil.bind(itemView);
        }

        void bindViews(RecipeResponse recipeResponse) {
            mRecipeItemCardBinding.setVariable(BR.recipe, recipeResponse);
            mRecipeItemCardBinding.setVariable(BR.recipeListener, mIRecipeItemClickListener);
            mRecipeItemCardBinding.executePendingBindings();
        }

        void checkViews() {
            if (mRecipeItemCardBinding.tvRecipeName.getText().length() > 0) {
                mRecipeItemCardBinding.vRecipeLine.setVisibility(View.VISIBLE);
            }
        }
    }
}