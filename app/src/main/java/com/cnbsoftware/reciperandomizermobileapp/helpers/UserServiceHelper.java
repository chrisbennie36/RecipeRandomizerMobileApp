package com.cnbsoftware.reciperandomizermobileapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.cnbsoftware.reciperandomizermobileapp.apis.UserServiceApi;
import com.cnbsoftware.reciperandomizermobileapp.apis.responses.UserResponse;
import com.cnbsoftware.reciperandomizermobileapp.dtos.RecipePreferenceDto;
import com.cnbsoftware.reciperandomizermobileapp.dtos.UserDto;
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

public class UserServiceHelper {

    UserServiceApi apiService;
    RecipeRandomizerHelper recipeRandomizerHelper;
    Context context;
    Intent setPreferencesIntent;
    Intent mainActivityIntent;

    boolean loginSuccessful = true;

    public UserServiceHelper(Context context, Intent setPreferencesIntent, Intent mainActivityIntent) throws MalformedURLException {
        this.context = context;
        this.setPreferencesIntent = setPreferencesIntent;
        this.mainActivityIntent = mainActivityIntent;
        this.recipeRandomizerHelper = new RecipeRandomizerHelper(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(new URL("http://localhost:5175/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(UserServiceApi.class);
    }

    public boolean Login(UserDto userDto) {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Call call = apiService.Login(userDto);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {

                if(!response.isSuccessful()) {
                    if(response.code() == 401) {
                        loginSuccessful = false;
                    }
                    Log.e("UserServiceHelper", "Login API call unsuccessful");
                    //custom error page
                    return;
                }

                if(response.code() == 400) {
                    Log.i("UserServiceHelper", "Login attempt failed, wrong username or password");
                }

                Call getUserByCredentialsCall = apiService.GetUserByCredentials(userDto);

                getUserByCredentialsCall.enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) {
                        int userId = 0;

                        if(!response.isSuccessful()) {
                            Log.e("UserServiceHelper", "GetUserByUsername API call unsuccessful");
                            return;
                        }

                        if(response.body() != null) {
                            UserResponse userResponse = null;
                            try {
                                userResponse = getResponseFromJson(response.body());
                                userId = userResponse.Id;
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putBoolean("UserLoggedIn", true);
                            bundle.putInt("UserId", userId);
                            bundle.putString("UserLanguage", userDto.SelectedLanguage);
                            mainActivityIntent.putExtras(bundle);
                            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(mainActivityIntent);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("UserServiceHelper", "AddUser API call failure");
                    }
                });
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("UserServiceHelper", "AddUser API call failure");
            }
        });

        return loginSuccessful;
    }

    public void GetUser(int userId) {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call call = apiService.GetUser(userId);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                if(!response.isSuccessful()) {
                    Log.e("UserServiceHelper", "AddUser API call unsuccessful");
                    return;
                }

                if(response.body() != null) {
                    Log.i("UserServiceHelper",response.body().toString());
                }

                if(response.isSuccessful() && response.code() == 200) {

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("UserServiceHelper", "AddUser API call failure");
            }
        });
    }

    public void saveUser(UserDto userDto) throws IOException {
        //ToDo: Not advisable => make async instead
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Call call = apiService.CreateUser(userDto);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                if(!response.isSuccessful()) {
                    Log.e("UserServiceHelper", "AddUser API call unsuccessful");
                    return;
                }

                if(response.body() != null) {
                    try {
                        UserResponse userResponse = getResponseFromJson(response.body());
                        Bundle setPreferencesBundle = new Bundle();
                        setPreferencesBundle.putInt("UserId", userResponse.Id);
                        setPreferencesBundle.putBoolean("ShowDietryRequirements", true);

                        ArrayList<RecipePreferenceDto> configuredRecipePreferences = recipeRandomizerHelper.GetConfiguredRecipePreferences(userDto.SelectedLanguage);
                        setPreferencesBundle.putParcelableArrayList("ConfiguredRecipePreferences", configuredRecipePreferences);
                        
                        setPreferencesIntent.putExtras(setPreferencesBundle);
                        context.startActivity(setPreferencesIntent);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("UserServiceHelper", "AddUser API call failure");
            }
        });
    }

    String getRequestBodyJson(UserDto userDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(userDto);
    }

    UserResponse getResponseFromJson(Object responseObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(responseObject);
        return mapper.readValue(responseJson, UserResponse.class);
    }

}
