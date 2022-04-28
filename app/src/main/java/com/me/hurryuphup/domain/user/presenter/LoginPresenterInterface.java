package com.me.hurryuphup.domain.user.presenter;

import android.app.Dialog;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.me.hurryuphup.domain.user.dto.LoginRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface LoginPresenterInterface {

    void appLoginCallback(LoginRequest loginRequest) throws InterruptedException;
    void kakaoLoginCallback(String accessToken) throws InterruptedException;
    void googleLoginCallback(String idToken) throws InterruptedException;
    void naverLoginCallback(String accessToken) throws InterruptedException;

    void kakaoLogin();
    void googleLogin() throws InterruptedException;
    void googleSignIn();
    void naverSignIn();

    void showFindIDDialog(Dialog dialog);
    void showResetPWDialog(Dialog dialog);

    void onDestroy();
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    void handleSignInResult(Task<GoogleSignInAccount> completedTask);

    void goMain();
}
