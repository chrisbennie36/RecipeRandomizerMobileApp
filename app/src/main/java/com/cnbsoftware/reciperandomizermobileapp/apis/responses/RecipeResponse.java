package com.cnbsoftware.reciperandomizermobileapp.apis.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeResponse {
    @SerializedName("recipeUrl")
    @Expose
    public String RecipeUrl;
    @SerializedName("errorTraceId")
    @Expose
    public String ErrorTraceId;
}
