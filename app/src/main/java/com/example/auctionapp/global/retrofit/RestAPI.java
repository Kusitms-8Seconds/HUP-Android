package com.example.auctionapp.global.retrofit;

import com.example.auctionapp.domain.user.dto.OAuth2GoogleLoginRequest;
import com.example.auctionapp.domain.user.dto.LoginResponse;
import com.example.auctionapp.domain.user.dto.OAuth2KakaoLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2NaverLoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestAPI {

    @POST("oauth2/google/validation")
    Call<LoginResponse> googleIdTokenValidation(@Body OAuth2GoogleLoginRequest oAuth2GoogleLoginRequest);
    @POST("oauth2/kakao/validation")
    Call<LoginResponse> kakaoAccessTokenValidation(@Body OAuth2KakaoLoginRequest oAuth2KakaoLoginRequest);
    @POST("oauth2/naver/validation")
    Call<LoginResponse> naverAccessTokenValidation(@Body OAuth2NaverLoginRequest oAuth2NaverLoginRequest);
}