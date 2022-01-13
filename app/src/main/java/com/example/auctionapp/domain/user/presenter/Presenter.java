package com.example.auctionapp.domain.user.presenter;

import com.example.auctionapp.domain.user.dto.LoginRequest;

public interface Presenter {
    void appLogin(LoginRequest loginRequest);
    void kakaoLogin(String accessToken);
    void googleLogin(String idToken);
    void naverLogin(String accessToken);
}
