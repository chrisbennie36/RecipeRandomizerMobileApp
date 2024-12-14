package com.cnbsoftware.reciperandomizermobileapp.apis.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProblemDetailsResponse {
    @JsonIgnore
    @SerializedName("type")
    @Expose
    public String Type;
    @SerializedName("title")
    @Expose
    public String Title;
    @SerializedName("status")
    @Expose
    public int Status;
    @SerializedName("traceId")
    @Expose
    public String TraceId;
}
