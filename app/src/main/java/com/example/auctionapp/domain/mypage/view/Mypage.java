package com.example.auctionapp.domain.mypage.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityMypageBinding;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.mypage.notice.view.Notice;
import com.example.auctionapp.domain.mypage.presenter.MypagePresenter;
import com.example.auctionapp.domain.notification.view.NotificationList;
import com.example.auctionapp.domain.scrap.view.Scrap;
import com.example.auctionapp.domain.item.view.SellHistory;
import com.example.auctionapp.domain.item.view.Interests;
import com.example.auctionapp.domain.item.view.AuctionHistory;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.presenter.LoginPresenter;
import com.example.auctionapp.domain.user.view.Login;
import com.example.auctionapp.domain.user.view.MyInfo;

import lombok.SneakyThrows;

public class Mypage extends Fragment implements MypageView{
    private ActivityMypageBinding binding;
    private MypagePresenter presenter;

    @Override
    public void onStart() {
        super.onStart();
        init();
        presenter.init();
        presenter.getUserInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ActivityMypageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        presenter = new MypagePresenter(this, binding, getActivity());

        init();
        presenter.init();
        presenter.getUserInfo();

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* swipe 시 진행할 동작 */
                presenter.init();
                presenter.getUserInfo();
                init();
                /* 업데이트가 끝났음을 알림 */
                binding.swipe.setRefreshing(false);
            }
        });
        binding.notiBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constants.userId != null) {
                    Intent intent = new Intent(getActivity(), NotificationList.class);
                    startActivity(intent);
                } else showToast(MypageConstants.ELogin.afterLogin.getText());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init() {
        binding.myPageUserName.setText(MypageConstants.ELogin.login.getText());
        Glide.with(getContext()).load(R.drawable.profile).into(binding.profileImg);
        System.out.println("userId: "+Constants.userId);
        System.out.println("userToken: "+Constants.accessToken);

        if(Constants.userId != null && Constants.accessToken != null) {
            //로그인 되어있을 때
            binding.logoutButton.setVisibility(View.VISIBLE);
            binding.userNameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MyInfo.class);
                    startActivity(intent);
                }
            });
        } else if(Constants.userId != null && Constants.accessToken == null){
            //이메일 인증이 되어있지 않을 때
            showToast(Constants.EUserServiceImpl.eNotActivatedEmailAuthExceptionMessage.getValue());
            binding.logoutButton.setVisibility(View.VISIBLE);
            binding.userNameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MyInfo.class);
                    startActivity(intent);
                }
            });
        } else {
            //로그인 안 되어있을때
            binding.logoutButton.setVisibility(View.INVISIBLE);
            // 로그인하러 가기
            binding.userNameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            });
        }

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.logout();

//                binding.myPageUserName.setText(MypageConstants.ELogin.login.getText());
//                Glide.with(getContext()).load(R.drawable.profile).into(binding.profileImg);
                init();
                presenter.init();
                presenter.getUserInfo();

//                binding.loginIcon.setVisibility(View.VISIBLE);
//                binding.logoutButton.setVisibility(View.INVISIBLE);
//                binding.userNameLayout.setEnabled(true);

            }
        });
        // 경매 참여 내역
        binding.aucHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constants.userId == null) {
                    showToast(MypageConstants.ELogin.afterLogin.getText());
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
                    showToast(MypageConstants.ELogin.afterLogin.getText());
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
                    showToast(MypageConstants.ELogin.afterLogin.getText());
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
                    showToast(MypageConstants.ELogin.afterLogin.getText());
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
    }


}