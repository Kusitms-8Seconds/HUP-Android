package com.example.auctionapp.global.retrofit;

import retrofit2.Response;

public interface MainRetrofitCallback<T> {
        void onSuccessResponse(Response<T> response);
        void onFailResponse(Response<T> response);
        void onConnectionFail(Throwable t);
    }

