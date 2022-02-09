package com.example.auctionapp.domain.user.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.databinding.ActivityLoginBinding;
import com.example.auctionapp.domain.mypage.view.Mypage;
import com.example.auctionapp.domain.mypage.view.MypageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.LoginRequest;
import com.example.auctionapp.domain.user.presenter.LoginPresenter;
import com.example.auctionapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.auth.Session;
import com.kakao.sdk.common.KakaoSdk;

import lombok.SneakyThrows;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity implements LoginView{
    private ActivityLoginBinding binding;
    LoginPresenter presenter;

    Context mContext;

    //private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new LoginPresenter(this, binding, getApplicationContext(), Login.this);

        KakaoSdk.init(this, getString(R.string.kakao_app_key));
        binding.goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        // App 로그인
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View view) {
                LoginRequest loginRequest = LoginRequest.of(binding.editID.getText().toString(), binding.editPW.getText().toString(), Constants.targetToken);
                presenter.appLoginCallback(loginRequest);
            }
        });

        //카카오 로그인
        binding.btnKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.kakaoLogin();
            }
        });
        //구글 로그인
        binding.btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View view) {
                presenter.googleLogin();
                onBackPressed();
            }
        });
        //네이버 로그인
        mContext = getApplicationContext();
        binding.btnNaverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.naverSignIn();
            }
        });

        // 앱 회원가입
        binding.signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void goMain() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}