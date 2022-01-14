package com.example.auctionapp.domain.mypage.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.auctionapp.databinding.ActivityChatBinding;
import com.example.auctionapp.databinding.ActivityMypageBinding;
import com.example.auctionapp.domain.chat.view.ChatView;
import com.example.auctionapp.domain.mypage.view.MypageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

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
    }

    @Override
    public void getUserInfo() {
        if(Constants.userId!=null){
            UserDetailsInfoRequest userDetailsInfoRequest = UserDetailsInfoRequest.of(Constants.userId);
            RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(userDetailsInfoRequest)
                    .enqueue(MainRetrofitTool.getCallback(new UserDetailsInfoCallback()));
        }
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

    public class UserDetailsInfoCallback implements MainRetrofitCallback<UserDetailsInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserDetailsInfoResponse> response) {
            System.out.println("username"+response.body().getUsername());
            binding.myPageUserName.setText(response.body().getUsername());
            if(response.body().getPicture()!=null){
                Glide.with(activity).load(response.body().getPicture()).into(binding.profileImg);
            }
            binding.loginIcon.setVisibility(View.INVISIBLE);
            binding.logoutButton.setVisibility(View.VISIBLE);
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<UserDetailsInfoResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(activity, jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}