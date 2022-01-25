package com.example.auctionapp.domain.user.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityLoginBinding;
import com.example.auctionapp.domain.home.constant.HomeConstants;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.LoginRequest;
import com.example.auctionapp.domain.user.dto.LoginResponse;
import com.example.auctionapp.domain.user.dto.OAuth2GoogleLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2KakaoLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2NaverLoginRequest;
import com.example.auctionapp.domain.user.view.Login;
import com.example.auctionapp.domain.user.view.LoginView;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitConstants;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LoginPresenter implements LoginPresenterInterface {
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0116;  //google login request code
    OAuthLogin mOAuthLoginModule;
    
    // Attributes
    private LoginView loginView;
    private ActivityLoginBinding binding;
    private Context context;
    private Activity activity;

    // Constructor
    public LoginPresenter(LoginView loginView, ActivityLoginBinding binding, Context context, Activity activity){
        this.loginView = loginView;
        this.binding = binding;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void appLoginCallback(LoginRequest loginRequest) {
        RetrofitTool.getAPIWithNullConverter().login(loginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("userId", Constants.userId);
        intent.putExtra("token", Constants.token);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void kakaoLoginCallback(String accessToken) {
        OAuth2KakaoLoginRequest oAuth2KakaoLoginRequest = new OAuth2KakaoLoginRequest(accessToken);
        RetrofitTool.getAPIWithNullConverter().kakaoAccessTokenValidation(oAuth2KakaoLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
    }

    @Override
    public void googleLoginCallback(String idToken) {
        OAuth2GoogleLoginRequest oAuth2GoogleLoginRequest = new OAuth2GoogleLoginRequest(idToken);
        RetrofitTool.getAPIWithNullConverter().googleIdTokenValidation(oAuth2GoogleLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
    }

    @Override
    public void naverLoginCallback(String accessToken) {
        OAuth2NaverLoginRequest oAuth2NaverLoginRequest = new OAuth2NaverLoginRequest(accessToken);
        RetrofitTool.getAPIWithNullConverter().naverAccessTokenValidation(oAuth2NaverLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
    }

    @Override
    public void kakaoLogin() {
        //kakao login
        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        session.open(AuthType.KAKAO_LOGIN_ALL, activity);
    }

    @Override
    public void googleLogin() {
        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
        // DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.ELoginCallback.eGoogleRequestIdToken.getText())
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(context);
        // 로그인 되어있는 경우
        if (gsa != null) {
            String idToken = gsa.getIdToken();
            googleLoginCallback(idToken);

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else
            googleSignIn();
    }

    @Override
    public void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void naverSignIn() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                context
                ,context.getString(R.string.naver_client_id)
                ,context.getString(R.string.naver_client_secret)
                ,context.getString(R.string.naver_client_name)
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );

        @SuppressLint("HandlerLeak")
        OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    String accessToken = mOAuthLoginModule.getAccessToken(context);
                    String refreshToken = mOAuthLoginModule.getRefreshToken(context);
                    long expiresAt = mOAuthLoginModule.getExpiresAt(context);
                    String tokenType = mOAuthLoginModule.getTokenType(context);

                    Log.i(Constants.ELoginCallback.eNaverTAG.getText(),Constants.ELoginCallback.eAccessToken.getText()+ accessToken);
                    Log.i(Constants.ELoginCallback.eNaverTAG.getText(),Constants.ELoginCallback.eRefreshToken.getText()+ refreshToken);
                    Log.i(Constants.ELoginCallback.eNaverTAG.getText(),Constants.ELoginCallback.eExpiresAt.getText()+ expiresAt);
                    Log.i(Constants.ELoginCallback.eNaverTAG.getText(),Constants.ELoginCallback.eTokenType.getText()+ tokenType);

                    naverLoginCallback(accessToken);
//                    onBackPressed();

                } else {
                    String errorCode = mOAuthLoginModule
                            .getLastErrorCode(context).getCode();
                    String errorDesc = mOAuthLoginModule.getLastErrorDesc(context);
                    Log.i(Constants.ELoginCallback.eNaverTAG.getText(),errorCode + errorDesc);
                }
            };
        };
        mOAuthLoginModule.startOauthLoginActivity(activity, mOAuthLoginHandler);
    }

    @Override
    public void onDestroy() {
        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        // google
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        if (requestCode == 12501) { return; }   // google account 선택 안 했을 때
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                String idToken = acct.getIdToken();

                Log.d(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.eNickname.getText() +personName);
                Log.d(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.ePersonGivenName.getText()+personGivenName);
                Log.d(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.eEmail.getText()+personEmail);
                Log.d(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.ePersonId.getText()+personId);
                Log.d(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.ePersonFamilynName.getText()+personFamilyName);
                Log.d(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.eProfilImg.getText()+personPhoto);
                Log.d(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.eAccessToken.getText()+idToken);

                googleLoginCallback(idToken);

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.eGoogleErrorCode.getText() + e.getStatusCode());

        }
    }

    @Override
    public void exceptionToast(int statusCode) {
        String errorMsg = "";
        if(statusCode==401) errorMsg = RetrofitConstants.ERetrofitCallback.eUnauthorized.getText();
        else if(statusCode==403) errorMsg = RetrofitConstants.ERetrofitCallback.eForbidden.getText();
        else if(statusCode==404) errorMsg = RetrofitConstants.ERetrofitCallback.eNotFound.getText();
        else errorMsg = String.valueOf(statusCode);
        Toast.makeText(context, Constants.ELoginCallback.TAG.getText() + statusCode + "_" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    private class LoginCallback implements MainRetrofitCallback<LoginResponse> {
        @Override
        public void onSuccessResponse(Response<LoginResponse> response) {
            Constants.userId = response.body().getUserId();
            Constants.token = response.body().getToken();
            Log.d(Constants.ELoginCallback.TAG.getText(), Constants.ELoginCallback.eSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<LoginResponse> response) throws IOException, JSONException {
//            System.out.println("login error: "+response.errorBody().string());
            exceptionToast(response.code());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Log.d(Constants.ELoginCallback.TAG.getText(), jObjError.getString("error"));
            } catch (Exception e) {
                Log.d(Constants.ELoginCallback.TAG.getText(), e.getMessage());
            }
            Log.d(Constants.ELoginCallback.TAG.getText(), Constants.ELoginCallback.eFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(Constants.ELoginCallback.eConnectionFail.getText(), t.getMessage());
        }
    }
    
    //kakao session callback
    public class SessionCallback implements ISessionCallback {
        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }
        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e(Constants.ELoginCallback.eKakaoSessionCallback.getText(), Constants.ELoginCallback.eSessionOpenFailed.getText() + exception.getMessage());
        }
        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) { Log.e(Constants.ELoginCallback.eKakaoTAG.getText(), Constants.ELoginCallback.eKakaoSessionError.getText()+ errorResult); }
                        @Override
                        public void onFailure(ErrorResult errorResult) { Log.e(Constants.ELoginCallback.eKakaoTAG.getText(), Constants.ELoginCallback.eKakaoUserError.getText() + errorResult); }
                        @Override
                        public void onSuccess(MeV2Response result) {
//                            onBackPressed();

                            Log.i(Constants.ELoginCallback.eKakaoTAG.getText(), Constants.ELoginCallback.ePersonId.getText() + result.getId());

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if (kakaoAccount != null) {
                                // 이메일
                                String email = kakaoAccount.getEmail();
                                if (email != null) {
                                    Log.i(Constants.ELoginCallback.eKakaoTAG.getText(), Constants.ELoginCallback.eEmail.getText()  + email);
                                } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                                } else {
                                    // 이메일 획득 불가
                                }
                                // 프로필
                                Profile profile = kakaoAccount.getProfile();

                                if (profile != null) {
                                    Log.d(Constants.ELoginCallback.eKakaoTAG.getText(), Constants.ELoginCallback.eNickname.getText()  + profile.getNickname());
                                    Log.d(Constants.ELoginCallback.eKakaoTAG.getText(), Constants.ELoginCallback.eProfilImg.getText()  + profile.getProfileImageUrl());
                                    Log.d(Constants.ELoginCallback.eKakaoTAG.getText(), Constants.ELoginCallback.eThumbnailImg.getText()  + profile.getThumbnailImageUrl());
                                    String accessToken = AuthApiClient.getInstance().getTokenManagerProvider()
                                            .getManager().getToken().getAccessToken();
                                    Log.d(Constants.ELoginCallback.eKakaoTAG.getText(), Constants.ELoginCallback.eAccessToken.getText() + accessToken);

                                    kakaoLoginCallback(accessToken);

                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 프로필 정보 획득 가능
                                } else {
                                    // 프로필 획득 불가
                                }
                            }
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
        }
    }

}
