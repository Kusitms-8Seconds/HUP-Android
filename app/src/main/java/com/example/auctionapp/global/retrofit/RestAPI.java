package com.example.auctionapp.global.retrofit;

import com.example.auctionapp.domain.user.dto.OAuth2GoogleLoginRequest;
import com.example.auctionapp.domain.user.view.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestAPI {

    //http://localhost:8080/oauth2/google/validation
    // @GET( EndPoint-자원위치(URI) )
    @POST("oauth2/google/validation")
    Call<LoginResponse> getIDtoken(@Body OAuth2GoogleLoginRequest oAuth2GoogleLoginRequest);
}