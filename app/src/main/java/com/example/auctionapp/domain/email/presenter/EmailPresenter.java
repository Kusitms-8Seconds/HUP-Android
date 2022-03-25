package com.example.auctionapp.domain.email.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.databinding.ActivitySignupEmailcheckBinding;
import com.example.auctionapp.domain.email.dto.CheckAuthCodeRequest;
import com.example.auctionapp.domain.email.dto.EmailAuthCodeRequest;
import com.example.auctionapp.domain.email.view.EmailView;
import com.example.auctionapp.domain.home.constant.HomeConstants;
import com.example.auctionapp.global.dto.DefaultResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitConstants;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class EmailPresenter implements EmailPresenterInterface {
    // Attributes
    private EmailView emailView;
    private ActivitySignupEmailcheckBinding binding;
    private Context context;

    // Constructor
    public EmailPresenter(EmailView emailView, ActivitySignupEmailcheckBinding binding, Context context) {
        this.emailView = emailView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void sendEmail(String email) {
        RetrofitTool.getAPIWithNullConverter().sendAuthCode(new EmailAuthCodeRequest(email))
                .enqueue(MainRetrofitTool.getCallback(new sendEmailCallback()));
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
            binding.btnSendEmail.setEnabled(false);
            binding.btnSendEmail.setText("발송되었습니다");
            binding.btnSendEmail.setBackgroundColor(Color.GRAY);
            binding.lyCheckAuthcode.setVisibility(View.VISIBLE);
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            emailView.showToast(errorMessageParser.getParsedErrorMessage());
            binding.btnSendEmail.setEnabled(true);
            binding.btnSendEmail.setText("이메일이 발송되지 않았습니다.");
//            binding.btnSendEmail.setBackgroundColor(Color.BLUE);
            binding.lyCheckAuthcode.setVisibility(View.INVISIBLE);
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
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            emailView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
