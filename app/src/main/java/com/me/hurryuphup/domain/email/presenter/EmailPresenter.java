package com.me.hurryuphup.domain.email.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.me.hurryuphup.MainActivity;
import com.me.hurryuphup.databinding.ActivitySignupEmailcheckBinding;
import com.me.hurryuphup.domain.email.dto.CheckAuthCodeRequest;
import com.me.hurryuphup.domain.email.dto.EmailAuthCodeRequest;
import com.me.hurryuphup.domain.email.view.EmailView;
import com.me.hurryuphup.global.dto.DefaultResponse;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;
import com.me.hurryuphup.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class EmailPresenter implements EmailPresenterInterface {
    // Attributes
    private EmailView emailView;
    private ActivitySignupEmailcheckBinding binding;
    private Context context;
    ErrorMessageParser errorMessageParser;

    // Constructor
    public EmailPresenter(EmailView emailView, ActivitySignupEmailcheckBinding binding, Context context) {
        this.emailView = emailView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void sendEmail(String email) {
        RetrofitTool.getAPIWithNullConverter().sendActivateAuthCode(new EmailAuthCodeRequest(email))
                .enqueue(MainRetrofitTool.getCallback(new sendEmailCallback()));
        // 버튼 한번 누르면 비활성화
        binding.btnSendEmail.setEnabled(false);
        binding.btnSendEmail.setText("발송되었습니다");
        binding.btnSendEmail.setBackgroundColor(Color.GRAY);
        binding.lyCheckAuthcode.setVisibility(View.VISIBLE);
    }

    @Override
    public void checkCode(String code) {
        RetrofitTool.getAPIWithNullConverter().checkAuthCode(new CheckAuthCodeRequest(code))
                .enqueue(MainRetrofitTool.getCallback(new checkCodeCallback()));
    }

    class sendEmailCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            Log.d(TAG, "sending email success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    class checkCodeCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            emailView.showToast("이메일 인증 성공");
            Log.d(TAG, "checking code success, idToken: " + response.body().toString());
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
