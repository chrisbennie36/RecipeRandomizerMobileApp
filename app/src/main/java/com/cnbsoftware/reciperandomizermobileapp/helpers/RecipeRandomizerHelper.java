package com.cnbsoftware.reciperandomizermobileapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.cnbsoftware.reciperandomizermobileapp.SetPreferencesActivity;
import com.cnbsoftware.reciperandomizermobileapp.apis.RecipeRandomizerApi;
import com.cnbsoftware.reciperandomizermobileapp.apis.responses.RecipePreferencesResponse;
import com.cnbsoftware.reciperandomizermobileapp.apis.responses.RecipeResponse;
import com.cnbsoftware.reciperandomizermobileapp.dtos.RecipePreferenceDto;
import com.cnbsoftware.reciperandomizermobileapp.dtos.UserRecipePreferencesDto;
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

    Context context;
    Intent mainActivityIntent;
    RecipeRandomizerApi recipeRandomizerApi;
    ArrayList<RecipePreferenceDto> configuredRecipePreferences = new ArrayList<>();

    public RecipeRandomizerHelper() throws MalformedURLException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(new URL("http://localhost:5179/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeRandomizerApi = retrofit.create(RecipeRandomizerApi.class);
    }
    public RecipeRandomizerHelper(Context context, Intent mainActivityIntent) throws MalformedURLException {
        this.context = context;
        this.mainActivityIntent = mainActivityIntent;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(new URL("http://localhost:5179/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeRandomizerApi = retrofit.create(RecipeRandomizerApi.class);
    }

    public void InspireUser(int userId, Activity activity) {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call call = recipeRandomizerApi.generateRecipeForUser(userId);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    Log.e("RecipeRandomizerHelper", "GenerateRecipeForUser API call unsuccessful");
                    return;
                }

                String recipeUrl = null;
                try {
                    recipeUrl = getRecipeResponseFromJson(response.body()).RecipeUrl;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                //mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(recipeUrl));
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("RecipeRandomizerHelper", "GenerateRecipeForUser API call failure");
            }
        });
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

                RecipePreferencesResponse userRecipePreferencesResponse = null;
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
                Call getConfiguredRecipePreferencesCall = recipeRandomizerApi.getRecipePreferences();
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

                    mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mainActivityIntent);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("RecipeRandomizerHelper", "SaveRecipePreferences API call failure");
                }
            });
        }
    }

    public ArrayList<RecipePreferenceDto> GetConfiguredRecipePreferences() {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ArrayList<RecipePreferenceDto> result = new ArrayList<>();

        Call call = recipeRandomizerApi.getRecipePreferences();

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

                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainActivityIntent);
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
