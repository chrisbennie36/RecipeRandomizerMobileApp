package com.cnbsoftware.reciperandomizermobileapp.apis.responses;

import com.cnbsoftware.reciperandomizermobileapp.dtos.RecipePreferenceDto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipePreferencesResponse {
    @SerializedName("recipePreferences")
    @Expose
    public ArrayList<RecipePreferenceDto> RecipePreferences;
}
