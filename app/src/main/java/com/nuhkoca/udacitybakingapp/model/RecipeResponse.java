package com.nuhkoca.udacitybakingapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nuhkoca on 3/13/18.
 */

public class RecipeResponse extends BaseObservable implements Parcelable {
    @Expose
    @SerializedName("id")
    private byte recipeId;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("servings")
    private byte servings;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("ingredients")
    private List<Ingredients> ingredients = null;
    @Expose
    @SerializedName("steps")
    private List<Steps> steps = null;

    public RecipeResponse() {}

    protected RecipeResponse(Parcel in) {
        recipeId = in.readByte();
        name = in.readString();
        servings = in.readByte();
        image = in.readString();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        steps = in.createTypedArrayList(Steps.CREATOR);
    }

    public static final Creator<RecipeResponse> CREATOR = new Creator<RecipeResponse>() {
        @Override
        public RecipeResponse createFromParcel(Parcel in) {
            return new RecipeResponse(in);
        }

        @Override
        public RecipeResponse[] newArray(int size) {
            return new RecipeResponse[size];
        }
    };

    @Bindable
    public byte getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(byte recipeId) {
        this.recipeId = recipeId;
        notifyPropertyChanged(BR.recipeId);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public byte getServings() {
        return servings;
    }

    public void setServings(byte servings) {
        this.servings = servings;
        notifyPropertyChanged(BR.servings);
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
        notifyPropertyChanged(BR.ingredients);
    }

    @Bindable
    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
        notifyPropertyChanged(BR.steps);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(recipeId);
        dest.writeString(name);
        dest.writeByte(servings);
        dest.writeString(image);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }
}