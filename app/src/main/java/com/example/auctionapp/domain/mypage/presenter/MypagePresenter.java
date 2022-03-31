package com.example.auctionapp.domain.mypage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityMypageBinding;
import com.example.auctionapp.domain.item.view.AuctionHistory;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.mypage.view.Mypage;
import com.example.auctionapp.domain.mypage.view.MypageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.LogoutRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.dto.DefaultResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitConstants;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.example.auctionapp.domain.email.view.Email;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MypagePresenter implements Presenter{
    // Attributes
    private MypageView mypageView;
    private ActivityMypageBinding binding;
    private Activity activity;

    public static  Mypage mypage = null;
    GoogleSignInClient mGoogleSignInClient;
    OAuthLogin mOAuthLoginModule;

    // Constructor
    public MypagePresenter(MypageView mypageView, ActivityMypageBinding binding, Activity activity){
        this.mypageView = mypageView;
        this.binding = binding;
        this.activity = activity;
    }

    @Override
    public void init() {
        // google 객체
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        // naver 객체
        mOAuthLoginModule = OAuthLogin.getInstance();

        if(Constants.userId != null)
            binding.ivCamera.setVisibility(View.INVISIBLE);
        else
            binding.ivCamera.setVisibility(View.VISIBLE);
    }

    @Override
    public void getUserInfo() {
        if(Constants.userId!=null){
            RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).userDetails(Constants.userId)
                    .enqueue(MainRetrofitTool.getCallback(new UserDetailsInfoCallback()));
        }
    }

    @Override
    public void logout() {
        socialLogOut();     //소셜로그아웃

        //앱 로그아웃
        LogoutRequest logoutRequest = new LogoutRequest(Constants.accessToken, Constants.refreshToken);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).logout(logoutRequest)
                .enqueue(MainRetrofitTool.getCallback(new LogoutCallback()));

        Constants.userId = null;
        Constants.accessToken = null;
        Constants.refreshToken = null;

        mypageView.showToast(MypageConstants.ELogin.logout.getText());
    }

    @Override
    public void socialLogOut() {
        //kakao
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                    }
                });
        //google
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
        //naver
        mOAuthLoginModule.logout(activity);
    }

    public class UserDetailsInfoCallback implements MainRetrofitCallback<UserInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            System.out.println("username"+response.body().getUsername());
            binding.myPageUserName.setText(response.body().getUsername());
            if(response.body().getPicture()!=null){
                Glide.with(activity).load(response.body().getPicture()).into(binding.profileImg);
            }
            binding.loginIcon.setVisibility(View.INVISIBLE);
            binding.logoutButton.setVisibility(View.VISIBLE);
//            if(!(response.body().isActivated())) {
//                Toast.makeText(activity, Constants.EUserServiceImpl.eUserNotActivatedExceptionMessage.getValue(), Toast.LENGTH_SHORT).show();
//                binding.emailButton.setVisibility(View.VISIBLE);
//            } else
//                binding.emailButton.setVisibility(View.INVISIBLE);

            // 이메일 인증 버튼
            binding.emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, Email.class);
                    intent.putExtra("email", response.body().getEmail());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            });
            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<UserInfoResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            mypageView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    public class LogoutCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            binding.myPageUserName.setText(MypageConstants.ELogin.login.getText());
            Glide.with(activity).load(R.drawable.profile).into(binding.profileImg);
            binding.loginIcon.setVisibility(View.VISIBLE);
            binding.logoutButton.setVisibility(View.INVISIBLE);
            binding.userNameLayout.setEnabled(true);

            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            mypageView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
