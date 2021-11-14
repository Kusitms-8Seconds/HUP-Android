package com.example.auctionapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestAPI {

    //http://localhost:8080/oauth2/google/validation
    // @GET( EndPoint-자원위치(URI) )
    @POST("oauth2/google/validation")
    Call<LoginResponse> getIDtoken(@Body OAuth2GoogleLoginRequest oAuth2GoogleLoginRequest);
}