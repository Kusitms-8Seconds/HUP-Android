package com.example.auctionapp.global.retrofit;

import org.json.JSONException;

import java.io.IOException;

import retrofit2.Response;

public interface MainRetrofitCallback<T> {
        void onSuccessResponse(Response<T> response) throws IOException, JSONException;
        void onFailResponse(Response<T> response) throws IOException, JSONException, InterruptedException;
        void onConnectionFail(Throwable t);
    }

