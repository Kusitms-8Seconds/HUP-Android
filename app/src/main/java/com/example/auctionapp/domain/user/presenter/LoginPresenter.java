package com.example.auctionapp.domain.user.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityLoginBinding;
import com.example.auctionapp.databinding.ActivityMypageBinding;
import com.example.auctionapp.domain.email.dto.CheckAuthCodeRequest;
import com.example.auctionapp.domain.email.dto.EmailAuthCodeRequest;
import com.example.auctionapp.domain.email.presenter.EmailPresenter;
import com.example.auctionapp.domain.mypage.presenter.MypagePresenter;
import com.example.auctionapp.domain.mypage.view.Mypage;
import com.example.auctionapp.domain.mypage.view.MypageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.EmailResetPasswordResponse;
import com.example.auctionapp.domain.user.dto.FindLoginIdRequest;
import com.example.auctionapp.domain.user.dto.FindLoginIdResponse;
import com.example.auctionapp.domain.user.dto.LoginRequest;
import com.example.auctionapp.domain.user.dto.LoginResponse;
import com.example.auctionapp.domain.user.dto.OAuth2GoogleLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2KakaoLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2NaverLoginRequest;
import com.example.auctionapp.domain.user.dto.ResetPasswordRequest;
import com.example.auctionapp.domain.user.dto.ResetPasswordResponse;
import com.example.auctionapp.domain.user.view.Login;
import com.example.auctionapp.domain.user.view.LoginView;
import com.example.auctionapp.global.dto.DefaultResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitConstants;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;
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

import lombok.SneakyThrows;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LoginPresenter implements LoginPresenterInterface {
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0116;  //google login request code
    private int NOT_SELECTED = 12501;
    OAuthLogin mOAuthLoginModule;
    //find id dialog
    Dialog findIDDialog;
    //reset Pw dialog
    Dialog resetPWDialog;
    // userId
    Long userId;
    
    // Attributes
    private LoginView loginView;
    private ActivityLoginBinding binding;
    private Context context;
    private Activity activity;
    ErrorMessageParser errorMessageParser;

    // Constructor
    public LoginPresenter(LoginView loginView, ActivityLoginBinding binding, Context context, Activity activity){
        this.loginView = loginView;
        this.binding = binding;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void appLoginCallback(LoginRequest loginRequest) throws InterruptedException {
        RetrofitTool.getAPIWithNullConverter().login(loginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
        Thread.sleep(1000);
        goMain();
    }

    @Override
    public void kakaoLoginCallback(String accessToken) throws InterruptedException {
        OAuth2KakaoLoginRequest oAuth2KakaoLoginRequest = new OAuth2KakaoLoginRequest(accessToken, Constants.targetToken);
        RetrofitTool.getAPIWithNullConverter().kakaoAccessTokenValidation(oAuth2KakaoLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
        Thread.sleep(1000);
        goMain();
    }

    @Override
    public void googleLoginCallback(String idToken) throws InterruptedException {
        OAuth2GoogleLoginRequest oAuth2GoogleLoginRequest = new OAuth2GoogleLoginRequest(idToken, Constants.targetToken);
        RetrofitTool.getAPIWithNullConverter().googleIdTokenValidation(oAuth2GoogleLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
        Thread.sleep(1000);
        goMain();
    }

    @Override
    public void naverLoginCallback(String accessToken) throws InterruptedException {
        OAuth2NaverLoginRequest oAuth2NaverLoginRequest = new OAuth2NaverLoginRequest(accessToken, Constants.targetToken);
        RetrofitTool.getAPIWithNullConverter().naverAccessTokenValidation(oAuth2NaverLoginRequest)
                .enqueue(MainRetrofitTool.getCallback(new LoginCallback()));
        Thread.sleep(1000);
        goMain();
    }

    @Override
    public void kakaoLogin() {
        //kakao login
        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        session.open(AuthType.KAKAO_LOGIN_ALL, activity);
    }

    @Override
    public void googleLogin() throws InterruptedException {
        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
        // DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.ELoginCallback.eGoogleRequestIdToken.getText())
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        // 기존에 로그인 했던 계정을 확인한다.
//        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(context);
//        // 로그인 되어있는 경우
//        if (gsa != null) {
//            String idToken = gsa.getIdToken();
//            googleLoginCallback(idToken);
//        } else
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
            @SneakyThrows
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
    public void showFindIDDialog(Dialog dialog) {
        this.findIDDialog = dialog;
        findIDDialog.show(); // 다이얼로그 띄우기

        EditText email_tv = findIDDialog.findViewById(R.id.edit_email);
        // 찾기 버튼
        findIDDialog.findViewById(R.id.bt_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_tv.getText().toString();
                FindLoginIdRequest findLoginIdRequest = new FindLoginIdRequest(email);
                RetrofitTool.getAPIWithNullConverter().findId(findLoginIdRequest)
                        .enqueue(MainRetrofitTool.getCallback(new FindIDCallback()));
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(email_tv.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void showResetPWDialog(Dialog dialog) {
        this.resetPWDialog = dialog;
        resetPWDialog.show();

        //attributes
        EditText email_tv = resetPWDialog.findViewById(R.id.edit_email);
        Button sendEmail = resetPWDialog.findViewById(R.id.bt_send_email);
        Button resendEmail = resetPWDialog.findViewById(R.id.btn_reSend);
        ConstraintLayout ly_codeCheck = resetPWDialog.findViewById(R.id.ly_check_authcode);
        ConstraintLayout ly_reset_pw = resetPWDialog.findViewById(R.id.ly_reset_pw);
        EditText edt_codeCheck = resetPWDialog.findViewById(R.id.edt_codeCheck);
        Button btn_codeCheck = resetPWDialog.findViewById(R.id.btn_codeCheck);
        EditText edt_reset_pw = resetPWDialog.findViewById(R.id.edt_reset_pw);
        EditText edt_reset_pw_check = resetPWDialog.findViewById(R.id.edt_reset_pw_check);
        Button btn_reset_pw = resetPWDialog.findViewById(R.id.btn_reset_pw);

        ly_codeCheck.setVisibility(View.GONE);
        ly_reset_pw.setVisibility(View.GONE);

        class checkCodeCallback implements MainRetrofitCallback<EmailResetPasswordResponse> {
            @Override
            public void onSuccessResponse(Response<EmailResetPasswordResponse> response) {
                loginView.showToast("이메일 인증 성공");
                ly_reset_pw.setVisibility(View.VISIBLE);
                Constants.userId = response.body().getUserId();
                Log.d(TAG, "checking code success, idToken: " + response.body().toString());
            }
            @Override
            public void onFailResponse(Response<EmailResetPasswordResponse> response) throws IOException, JSONException {
                errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
                ly_reset_pw.setVisibility(View.GONE);
                Log.d(TAG, "onFailResponse");
            }
            @Override
            public void onConnectionFail(Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        }

        // 전송 버튼
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_tv.getText().toString();
                EmailAuthCodeRequest emailAuthCodeRequest = new EmailAuthCodeRequest(email);
                RetrofitTool.getAPIWithNullConverter().sendAuthCode(emailAuthCodeRequest)
                        .enqueue(MainRetrofitTool.getCallback(new sendEmailCallback()));
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(email_tv.getWindowToken(), 0);
                sendEmail.setEnabled(false);
                sendEmail.setBackgroundColor(Color.GRAY);
                ly_codeCheck.setVisibility(View.VISIBLE);
            }
        });
        // 재전송 버튼
        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_tv.getText().toString();
                EmailAuthCodeRequest emailAuthCodeRequest = new EmailAuthCodeRequest(email);
                RetrofitTool.getAPIWithNullConverter().sendAuthCode(emailAuthCodeRequest)
                        .enqueue(MainRetrofitTool.getCallback(new sendEmailCallback()));
//                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(email_tv.getWindowToken(), 0);
            }
        });
        // 인증번호 확인 버튼
        btn_codeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkCode = edt_codeCheck.getText().toString();
                CheckAuthCodeRequest checkAuthCodeRequest = new CheckAuthCodeRequest(checkCode);
                RetrofitTool.getAPIWithNullConverter().resetPWAuthCode(checkAuthCodeRequest)
                        .enqueue(MainRetrofitTool.getCallback(new checkCodeCallback()));
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_codeCheck.getWindowToken(), 0);
            }
        });
        // 비밀번호 재설정 버튼
        btn_reset_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = edt_reset_pw.getText().toString();
                String passwordCheck = edt_reset_pw_check.getText().toString();
                ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(Constants.userId, password, passwordCheck);
                RetrofitTool.getAPIWithNullConverter().resetPW(resetPasswordRequest)
                        .enqueue(MainRetrofitTool.getCallback(new ResetPWCallback()));
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_reset_pw_check.getWindowToken(), 0);
            }
        });
        //TODO : 더해
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
        if (requestCode == NOT_SELECTED) { return; }   // google account 선택 안 했을 때
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
            }
        } catch (ApiException | InterruptedException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(Constants.ELoginCallback.eGoogleTAG.getText(), Constants.ELoginCallback.eGoogleErrorCode.getText() + e.getMessage());

        }
    }

    @Override
    public void goMain() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    private class LoginCallback implements MainRetrofitCallback<LoginResponse> {
        @Override
        public void onSuccessResponse(Response<LoginResponse> response) {
            Constants.userId = response.body().getUserId();
            Constants.accessToken = response.body().getAccessToken();
            Constants.refreshToken = response.body().getRefreshToken();
            Log.d(Constants.ELoginCallback.TAG.getText(), Constants.ELoginCallback.eSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<LoginResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            loginView.showToast(errorMessageParser.getParsedErrorMessage());
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
                        @SneakyThrows
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
                        }
                    });
        }
    }
    private class FindIDCallback implements MainRetrofitCallback<FindLoginIdResponse> {
        @Override
        public void onSuccessResponse(Response<FindLoginIdResponse> response) {
            String loginId = response.body().getLoginId();
            Long userId = response.body().getUserId();

            LinearLayout ly = findIDDialog.findViewById(R.id.ly_show_id);
            TextView tv_showId = findIDDialog.findViewById(R.id.tv_show_id);
            ly.setVisibility(View.VISIBLE);
            tv_showId.setText(loginId);
            Log.d(Constants.ELoginCallback.TAG.getText(), Constants.ELoginCallback.eSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<FindLoginIdResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            loginView.showToast(errorMessageParser.getParsedErrorMessage());

            LinearLayout ly = findIDDialog.findViewById(R.id.ly_show_id);
            ly.setVisibility(View.GONE);
            Log.d("findID", Constants.ELoginCallback.eFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(Constants.ELoginCallback.eConnectionFail.getText(), t.getMessage());
        }
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
    private class ResetPWCallback implements MainRetrofitCallback<ResetPasswordResponse> {
        @Override
        public void onSuccessResponse(Response<ResetPasswordResponse> response) {

            Log.d(Constants.ELoginCallback.TAG.getText(), Constants.ELoginCallback.eSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ResetPasswordResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            loginView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d("findID", Constants.ELoginCallback.eFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(Constants.ELoginCallback.eConnectionFail.getText(), t.getMessage());
        }
    }
}
