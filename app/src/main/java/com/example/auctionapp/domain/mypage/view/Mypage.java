package com.example.auctionapp.domain.mypage.view;

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
import com.example.auctionapp.domain.mypage.notice.view.Notice;
import com.example.auctionapp.domain.mypage.presenter.MypagePresenter;
import com.example.auctionapp.domain.scrap.view.Scrap;
import com.example.auctionapp.domain.item.view.SellHistory;
import com.example.auctionapp.domain.item.view.Interests;
import com.example.auctionapp.domain.item.view.AuctionHistory;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.domain.user.view.Login;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
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

public class Mypage extends Fragment implements MypageView{
    private ActivityMypageBinding binding;
    private MypagePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ActivityMypageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        presenter = new MypagePresenter(this, binding, getActivity());

        Glide.with(getContext()).load(R.drawable.profile).into(binding.profileImg);
        System.out.println("userId"+Constants.userId);
        System.out.println("userToken"+Constants.token);

        presenter.init();
        presenter.getUserInfo();

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.socialLogOut();
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


}