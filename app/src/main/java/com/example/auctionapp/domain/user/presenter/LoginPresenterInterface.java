package com.example.auctionapp.domain.user.presenter;

import com.example.auctionapp.domain.user.dto.LoginRequest;

public interface LoginPresenterInterface {
    void appLoginCallback(LoginRequest loginRequest);
    void kakaoLoginCallback(String accessToken);
    void googleLoginCallback(String idToken);
    void naverLoginCallback(String accessToken);

    void kakaoLogin();
    void googleLogin();
    void googleSignIn();
    void naverSignIn();

    void onDestroy();
}
