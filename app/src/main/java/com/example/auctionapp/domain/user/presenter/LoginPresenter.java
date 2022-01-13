package com.example.auctionapp.domain.user.presenter;

import android.util.Log;

import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.LoginRequest;
import com.example.auctionapp.domain.user.dto.LoginResponse;
import com.example.auctionapp.domain.user.dto.OAuth2GoogleLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2KakaoLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2NaverLoginRequest;
import com.example.auctionapp.domain.user.view.LoginView;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LoginPresenter implements Presenter{

    private final LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    @Override
    public void appLogin(LoginRequest loginRequest) {
        RetrofitTool.getAPIWithNullConverter().login(loginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
    }

    @Override
    public void kakaoLogin(String accessToken) {
        OAuth2KakaoLoginRequest oAuth2KakaoLoginRequest = new OAuth2KakaoLoginRequest(accessToken);
        RetrofitTool.getAPIWithNullConverter().kakaoAccessTokenValidation(oAuth2KakaoLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
    }

    @Override
    public void googleLogin(String idToken) {
        OAuth2GoogleLoginRequest oAuth2GoogleLoginRequest = new OAuth2GoogleLoginRequest(idToken);
        RetrofitTool.getAPIWithNullConverter().googleIdTokenValidation(oAuth2GoogleLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
    }

    @Override
    public void naverLogin(String accessToken) {
        OAuth2NaverLoginRequest oAuth2NaverLoginRequest = new OAuth2NaverLoginRequest(accessToken);
        RetrofitTool.getAPIWithNullConverter().naverAccessTokenValidation(oAuth2NaverLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
    }

    private class LoginCallback implements MainRetrofitCallback<LoginResponse> {
        @Override
        public void onSuccessResponse(Response<LoginResponse> response) {
            Constants.userId = response.body().getUserId();
            Constants.token = response.body().getToken();
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<LoginResponse> response) {
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
