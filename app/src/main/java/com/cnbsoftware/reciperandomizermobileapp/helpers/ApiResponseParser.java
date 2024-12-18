package com.cnbsoftware.reciperandomizermobileapp.helpers;

import com.cnbsoftware.reciperandomizermobileapp.apis.responses.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiResponseParser {
    private final ObjectMapper mapper;

    public ApiResponseParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T extends ApiResponse>  T parseApiResponse(Object apiResponse, Class<T> parsedResponseType) throws JsonProcessingException {
        String responseJson = mapper.writeValueAsString(apiResponse);
        return mapper.readValue(responseJson, parsedResponseType);
    }
}
