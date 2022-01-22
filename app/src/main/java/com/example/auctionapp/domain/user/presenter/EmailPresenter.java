package com.example.auctionapp.domain.user.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.databinding.ActivitySignupEmailcheckBinding;
import com.example.auctionapp.domain.user.dto.CheckAuthCodeRequest;
import com.example.auctionapp.domain.user.dto.EmailAuthCodeRequest;
import com.example.auctionapp.domain.user.view.EmailView;
import com.example.auctionapp.global.dto.DefaultResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONObject;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class EmailPresenter implements EmailPresenterInterface{
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
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) {
            try {

                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Log.d("error", jObjError.getString("message"));
                showToast(jObjError.getString("message"));
            } catch (Exception e) {
                Log.d("error", e.getMessage());
            }
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
            showToast("이메일인증성공");
            Log.d(TAG, "checking code success, idToken: " + response.body().toString());
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) {
            try {
                showToast("이메일인증실패");
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Log.d("fail error", jObjError.getString("message"));
                showToast(jObjError.getString("message"));
            } catch (Exception e) {
                Log.d("error", e.getMessage());
            }
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
