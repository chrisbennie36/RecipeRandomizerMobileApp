package com.cnbsoftware.reciperandomizermobileapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cnbsoftware.reciperandomizermobileapp.dtos.RecipePreferenceDto;

import java.util.ArrayList;

public class ConfiguredRecipePreferencesModel implements Parcelable {

    public ConfiguredRecipePreferencesModel(ArrayList<RecipePreferenceDto> recipePreferences) {
        RecipePreferences = recipePreferences;
    }

    public ArrayList<RecipePreferenceDto> RecipePreferences;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeList(RecipePreferences);
    }

    public static final Parcelable.Creator<ConfiguredRecipePreferencesModel> CREATOR
            = new Parcelable.Creator<>() {
        public ConfiguredRecipePreferencesModel createFromParcel(Parcel in) {
            return new ConfiguredRecipePreferencesModel(in);
        }

        public ConfiguredRecipePreferencesModel[] newArray(int size) {
            return new ConfiguredRecipePreferencesModel[size];
        }
    };

    private ConfiguredRecipePreferencesModel(Parcel in) {
        this.RecipePreferences = in.readArrayList(null);
    }
}
