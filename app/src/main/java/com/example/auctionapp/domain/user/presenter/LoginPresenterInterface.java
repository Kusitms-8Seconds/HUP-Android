package com.example.auctionapp.domain.user.presenter;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.auctionapp.domain.user.dto.LoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2GoogleLoginRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.io.IOException;

public interface LoginPresenterInterface {

    void appLoginCallback(LoginRequest loginRequest) throws InterruptedException;
    void kakaoLoginCallback(String accessToken) throws InterruptedException;
    void googleLoginCallback(String idToken) throws InterruptedException;
    void naverLoginCallback(String accessToken) throws InterruptedException;

    void kakaoLogin();
    void googleLogin() throws InterruptedException;
    void googleSignIn();
    void naverSignIn();

    void onDestroy();
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    void handleSignInResult(Task<GoogleSignInAccount> completedTask);

    void exceptionToast(int statusCode);
    void goMain();
}
