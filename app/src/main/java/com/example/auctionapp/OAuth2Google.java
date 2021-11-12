package com.example.auctionapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OAuth2Google {

    //http://localhost:8080/oauth2/google/validation
    // @GET( EndPoint-자원위치(URI) )
    @GET("oauth2/google/validation/")
    Call<OAuth2GoogleLogin> getIDtoken();
}