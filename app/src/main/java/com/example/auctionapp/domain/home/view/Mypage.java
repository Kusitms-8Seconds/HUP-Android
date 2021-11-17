package com.example.auctionapp.domain.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.scrap.view.Scrap;
import com.example.auctionapp.domain.item.view.SellHistory;
import com.example.auctionapp.domain.item.view.Interests;
import com.example.auctionapp.domain.pricesuggestion.view.AuctionHistory;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.LoginRequest;
import com.example.auctionapp.domain.user.dto.LoginResponse;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.domain.user.view.Login;
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

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Mypage extends Fragment {

    ViewGroup viewGroup;

    TextView goLogin;
    Button logout_button;
    Button signIn_button;

    GoogleSignInClient mGoogleSignInClient;
    OAuthLogin mOAuthLoginModule;

    TextView myName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("온크리에이트뷰");
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_mypage, container, false);
        myName = (TextView) viewGroup.findViewById(R.id.myPage_userName);
        System.out.println("userId"+Constants.userId);
        System.out.println("userToken"+Constants.token);
        if(Constants.userId!=null){
            UserDetailsInfoRequest userDetailsInfoRequest = UserDetailsInfoRequest.of(Constants.userId);
            RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(userDetailsInfoRequest)
                    .enqueue(MainRetrofitTool.getCallback(new UserDetailsInfoCallback()));
        }

        // google 객체
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(), gso);
        // naver 객체
        mOAuthLoginModule = OAuthLogin.getInstance();

        logout_button = (Button) viewGroup.findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // 일반 로그인
//                editor.putBoolean("checked", false);
//                editor.commit();

                // kakao logout
                kakaoSignOut();
                //google logout
                googleSignOut();
                //naver logout
                naverSignOut();

            }
        });
        // 로그인하러 가기
        goLogin = (TextView) viewGroup.findViewById(R.id.goLogin);
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });
        // 경매 참여 내역
        LinearLayout goAuctionHistory = (LinearLayout) viewGroup.findViewById(R.id.auc_history);
        goAuctionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AuctionHistory.class);
                startActivity(intent);
            }
        });
        // 판매 내역
        LinearLayout goSellHistory = (LinearLayout) viewGroup.findViewById(R.id.sell_history);
        goSellHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SellHistory.class);
                startActivity(intent);
            }
        });
        // 스크랩 내역
        ConstraintLayout scrap_layout = (ConstraintLayout) viewGroup.findViewById(R.id.scrap_layout);
        scrap_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Scrap.class);
                startActivity(intent);
            }
        });
        // 관심 카테고리
        ConstraintLayout interest_layout = (ConstraintLayout) viewGroup.findViewById(R.id.interest_layout);
        interest_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Interests.class);
                startActivity(intent);
            }
        });
        // 공지사항
        ConstraintLayout notify_layout = (ConstraintLayout) viewGroup.findViewById(R.id.notify_layout);
        notify_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Notice.class);
                startActivity(intent);
            }
        });
        // 안전거래설정
        ConstraintLayout safety_layout = (ConstraintLayout) viewGroup.findViewById(R.id.safety_layout);
        safety_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), Notice.class);
//                startActivity(intent);
            }
        });

        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void kakaoSignOut() {
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //google logout
    private void googleSignOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void naverSignOut() {
        mOAuthLoginModule.logout(getContext());
        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private class UserDetailsInfoCallback implements MainRetrofitCallback<UserDetailsInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserDetailsInfoResponse> response) {
            System.out.println("username"+response.body().getUsername());
            myName.setText(response.body().getUsername());
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<UserDetailsInfoResponse> response) {
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }



}