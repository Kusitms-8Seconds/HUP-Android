package com.me.hurryuphup.domain.user.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivityLoginBinding;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.domain.user.dto.LoginRequest;
import com.me.hurryuphup.domain.user.presenter.LoginPresenter;
import com.me.hurryuphup.R;
import com.kakao.sdk.common.KakaoSdk;

import lombok.SneakyThrows;

public class Login extends AppCompatActivity implements LoginView{
    private ActivityLoginBinding binding;
    LoginPresenter presenter;

    Context mContext;

    Dialog findIDDialog;
    Dialog resetPWDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new LoginPresenter(this, binding, getApplicationContext(), Login.this);


        findIDDialog = new Dialog(Login.this);
        resetPWDialog = new Dialog(Login.this);
        findIDDialog.setContentView(R.layout.dialog_find_id);
        resetPWDialog.setContentView(R.layout.dialog_reset_password);

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
        // 아이디 찾기
        binding.findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showFindIDDialog(findIDDialog);
            }
        });
        // 비번 재설정
        binding.resetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showResetPWDialog(resetPWDialog);
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
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}