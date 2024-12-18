package com.cnbsoftware.reciperandomizermobileapp.apis.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse extends ApiResponse{
    @SerializedName("id")
    @Expose
    public int Id;
    @SerializedName("username")
    @Expose
    public String Username;
    @SerializedName("password")
    @Expose
    public String Password;
    @SerializedName("role")
    @Expose
    public int Role;
}
