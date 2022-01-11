package com.example.auctionapp.domain.mypage.vc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityMypageBinding;
import com.example.auctionapp.domain.mypage.notice.vc.Notice;
import com.example.auctionapp.domain.scrap.vc.Scrap;
import com.example.auctionapp.domain.item.vc.SellHistory;
import com.example.auctionapp.domain.item.vc.Interests;
import com.example.auctionapp.domain.item.vc.AuctionHistory;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.domain.user.vc.Login;
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

public class Mypage extends Fragment {
    private ActivityMypageBinding binding;

    TextView logout_button;

    GoogleSignInClient mGoogleSignInClient;
    OAuthLogin mOAuthLoginModule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ActivityMypageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        Glide.with(getContext()).load(R.drawable.profile).into(binding.profileImg);
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

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socialLogOut();
                Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                Constants.token = null;
                Constants.userId = null;

                binding.myPageUserName.setText("로그인하기");
                Glide.with(getContext()).load(R.drawable.profile).into(binding.profileImg);
                binding.loginIcon.setVisibility(View.VISIBLE);

            }
        });
        // 로그인하러 가기
        binding.userNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });
        // 경매 참여 내역
        binding.aucHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constants.userId == null) {
                    Toast.makeText(getContext(), "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), AuctionHistory.class);
                    startActivity(intent);
                }
            }
        });
        // 판매 내역
        binding.sellHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constants.userId == null) {
                    Toast.makeText(getContext(), "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), SellHistory.class);
                    startActivity(intent);
                }
            }
        });
        // 스크랩 내역
        binding.scrapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constants.userId == null) {
                    Toast.makeText(getContext(), "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), Scrap.class);
                    startActivity(intent);
                }
            }
        });
        // 관심 카테고리
        binding.interestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constants.userId == null) {
                    Toast.makeText(getContext(), "로그인 후 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), Interests.class);
                    startActivity(intent);
                }
            }
        });
        // 공지사항
        binding.notifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Notice.class);
                startActivity(intent);
            }
        });
        // 안전거래설정
        binding.safetyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), Notice.class);
//                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void socialLogOut() {
        //kakao
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        //google
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        //naver
        mOAuthLoginModule.logout(getContext());
        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private class UserDetailsInfoCallback implements MainRetrofitCallback<UserDetailsInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserDetailsInfoResponse> response) {
            System.out.println("username"+response.body().getUsername());
            binding.myPageUserName.setText(response.body().getUsername());
            if(response.body().getPicture()!=null){
                Glide.with(getContext()).load(response.body().getPicture()).into(binding.profileImg);
            }
            binding.loginIcon.setVisibility(View.INVISIBLE);
            logout_button.setVisibility(View.VISIBLE);
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<UserDetailsInfoResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }



}