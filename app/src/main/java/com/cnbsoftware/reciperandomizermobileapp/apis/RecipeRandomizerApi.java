package com.cnbsoftware.reciperandomizermobileapp.apis;

import com.cnbsoftware.reciperandomizermobileapp.apis.responses.RecipePreferencesResponse;
import com.cnbsoftware.reciperandomizermobileapp.apis.responses.RecipeResponse;
import com.cnbsoftware.reciperandomizermobileapp.dtos.UserRecipePreferencesDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecipeRandomizerApi {
    @POST("/api/UserRecipePreferences/Add")
    Call<Void> savePreferences(@Body UserRecipePreferencesDto userRecipePreferencesDto);
    @POST("/api/UserRecipePreferences/Update")
    Call<Void> updatePreferences(@Body UserRecipePreferencesDto userRecipePreferencesDto);
    @GET("/api/UserRecipePreferences/{userId}")
    Call<RecipePreferencesResponse> getUserRecipePreferences(@Path("userId") int userId);
    @GET("/api/Recipe/{userId}")
    Call<RecipeResponse> generateRecipeForUser(@Path("userId") int userId);
    @GET("/api/RecipePreferences/{cultureCode}")
    Call<RecipePreferencesResponse> getRecipePreferences(@Path("cultureCode") String cultureCode);
}
