package com.cnbsoftware.reciperandomizermobileapp.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.cnbsoftware.reciperandomizermobileapp.CustomErrorPageActivity;
import com.cnbsoftware.reciperandomizermobileapp.MainActivity;
import com.cnbsoftware.reciperandomizermobileapp.SetPreferencesActivity;
import com.cnbsoftware.reciperandomizermobileapp.apis.RecipeRandomizerApi;
import com.cnbsoftware.reciperandomizermobileapp.apis.responses.ProblemDetailsResponse;
import com.cnbsoftware.reciperandomizermobileapp.apis.responses.RecipePreferencesResponse;
import com.cnbsoftware.reciperandomizermobileapp.apis.responses.RecipeResponse;
import com.cnbsoftware.reciperandomizermobileapp.dtos.RecipePreferenceDto;
import com.cnbsoftware.reciperandomizermobileapp.dtos.UserRecipePreferencesDto;
import com.cnbsoftware.reciperandomizermobileapp.managers.ActivityManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeRandomizerHelper {

    ActivityManager activityManager;
    RecipeRandomizerApi recipeRandomizerApi;
    ArrayList<RecipePreferenceDto> configuredRecipePreferences = new ArrayList<>();

    public RecipeRandomizerHelper(ActivityManager activityManager) throws MalformedURLException {
        this.activityManager = activityManager;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(new URL("http://localhost:5179"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeRandomizerApi = retrofit.create(RecipeRandomizerApi.class);
    }

    public void InspireUser(int userId) {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call call = recipeRandomizerApi.generateRecipeForUser(userId);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e("RecipeRandomizerHelper", String.format("IOException when calling the GenerateRecipeForUser API method: %s", e.getMessage()));
            return;
        }

        if (!response.isSuccessful()) {
            Bundle bundle = new Bundle();
            try {
                bundle.putString("ProblemDetails", response.errorBody().string());
            } catch (IOException e) {
                Log.e("RecipeRandomizerHelper", String.format("IOException when calling the GenerateRecipeForUser API method: %s", e.getMessage()));
            }
            activityManager.OpenActivity(CustomErrorPageActivity.class, bundle);
        } else {
            RecipeResponse recipeResponse;
            try {
                recipeResponse = getRecipeResponseFromJson(response.body());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            if(recipeResponse.RecipeUrl != null) {;
                activityManager.OpenUrl(recipeResponse.RecipeUrl);
            }
        }
    }

    public void GetUserRecipePreferences(int userId, Activity activity) {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call call = recipeRandomizerApi.getUserRecipePreferences(userId);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    Log.e("RecipeRandomizerHelper", "GetUserRecipePreferences API call unsuccessful");
                    return;
                }

                RecipePreferencesResponse userRecipePreferencesResponse;
                try {
                    userRecipePreferencesResponse = getRecipePreferencesResponseFromJson(response.body());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                Intent setPreferencesIntent = new Intent(activity.getApplicationContext(), SetPreferencesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("UserId", userId);
                bundle.putParcelableArrayList("UserRecipePreferences", userRecipePreferencesResponse.RecipePreferences);

                //ToDo: Cache these
                Call getConfiguredRecipePreferencesCall = recipeRandomizerApi.getRecipePreferences("en-GB");
                try {
                    Response getConfiguredRecipePreferencesResponse = getConfiguredRecipePreferencesCall.execute();

                    if(!getConfiguredRecipePreferencesResponse.isSuccessful()) {
                        Log.e("RecipeRandomizerHelper", "GetRecipePreferences API call unsuccessful");
                        return;
                    }

                    configuredRecipePreferences = getRecipePreferencesResponseFromJson(getConfiguredRecipePreferencesResponse.body()).RecipePreferences;
                    bundle.putParcelableArrayList("ConfiguredRecipePreferences", configuredRecipePreferences);
                    setPreferencesIntent.putExtras(bundle);
                    activity.startActivity(setPreferencesIntent);

                } catch (IOException e) {
                    Log.e("RecipeRandomizerHelper", String.format("Error when retrieving configured Recipe Preferences: %s", e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("RecipeRandomizerHelper", "GetUserRecipePreferences API call failure");
            }
        });
    }

    public void SaveRecipePreferences(UserRecipePreferencesDto userRecipePreferencesDto) {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(userRecipePreferencesDto.HasRemovedPreferences(r -> r.Excluded)) {
            updateRecipePreferences(userRecipePreferencesDto);
        }
        else {
            Call call = recipeRandomizerApi.savePreferences(userRecipePreferencesDto);

            call.enqueue(new Callback() {

                @Override
                public void onResponse(Call call, Response response) {
                    if(!response.isSuccessful()) {
                        Log.e("RecipeRandomizerHelper", "SaveRecipePreferences API call unsuccessful");
                        return;
                    }

                    activityManager.OpenActivity(MainActivity.class);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("RecipeRandomizerHelper", "SaveRecipePreferences API call failure");
                }
            });
        }
    }

    public ArrayList<RecipePreferenceDto> GetConfiguredRecipePreferences(String cultureCode) {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ArrayList<RecipePreferenceDto> result = new ArrayList<>();

        Call call = recipeRandomizerApi.getRecipePreferences(cultureCode != null ? cultureCode : "en-GB");

        try {
            Response response = call.execute();

            if(!response.isSuccessful()) {
                Log.e("RecipeRandomizerHelper", "GetRecipePreferences API call unsuccessful");
                return result;
            }

            result = getRecipePreferencesResponseFromJson(response.body()).RecipePreferences;
            return result;

        } catch (IOException e) {
            Log.e("RecipeRandomizerHelper", String.format("Error when retrieving configured Recipe Preferences: %s", e.getMessage()));
            return result;
        }
    }

    private void updateRecipePreferences(UserRecipePreferencesDto userRecipePreferencesDto) {
        Call call = recipeRandomizerApi.updatePreferences(userRecipePreferencesDto);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                if(!response.isSuccessful()) {
                    Log.e("RecipeRandomizerHelper", "UpdateRecipePreferences API call unsuccessful");
                    return;
                }

                activityManager.OpenActivity(MainActivity.class);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("RecipeRandomizerHelper", "UpdateRecipePreferences API call failure");
            }
        });
    }

    private RecipeResponse getRecipeResponseFromJson(Object responseObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(responseObject);
        return mapper.readValue(responseJson, RecipeResponse.class);
    }

    private RecipePreferencesResponse getRecipePreferencesResponseFromJson(Object responseObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(responseObject);
        return mapper.readValue(responseJson, RecipePreferencesResponse.class);
    }
}
