package com.nuhkoca.udacitybakingapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nuhkoca on 3/13/18.
 */

public class Steps extends BaseObservable implements Parcelable {
    @Expose
    @SerializedName("id")
    private byte stepId;
    @Expose
    @SerializedName("shortDescription")
    private String shortDescription;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("videoURL")
    private String videoURL;
    @Expose
    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public Steps() {
    }

    protected Steps(Parcel in) {
        stepId = in.readByte();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    @Bindable
    public byte getStepId() {
        return stepId;
    }

    public void setStepId(byte stepId) {
        this.stepId = stepId;
        notifyPropertyChanged(BR.stepId);
    }

    @Bindable
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        notifyPropertyChanged(BR.shortDescription);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
        notifyPropertyChanged(BR.videoURL);
    }

    @Bindable
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
        notifyPropertyChanged(BR.thumbnailURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(stepId);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }
}