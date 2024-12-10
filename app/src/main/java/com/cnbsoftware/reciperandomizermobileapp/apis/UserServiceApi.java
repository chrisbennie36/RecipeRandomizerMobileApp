package com.cnbsoftware.reciperandomizermobileapp.apis;

import com.cnbsoftware.reciperandomizermobileapp.apis.responses.UserResponse;
import com.cnbsoftware.reciperandomizermobileapp.dtos.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServiceApi {
    @POST("/api/User")
    Call<UserResponse> CreateUser(@Body UserDto userDto);
    @GET("/api/User/{userId}")
    Call<UserResponse> GetUser(@Path("userId") int userId);
    @POST("/api/User/GetUserByCredentials")
    Call<UserResponse> GetUserByCredentials(@Body UserDto userDto);
    @POST("/api/Login")
    Call<Void> Login(@Body UserDto userDto);
}
