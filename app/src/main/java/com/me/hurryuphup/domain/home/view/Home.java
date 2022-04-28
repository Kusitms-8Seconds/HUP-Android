package com.me.hurryuphup.domain.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.me.hurryuphup.databinding.ActivityHomeBinding;
import com.me.hurryuphup.domain.home.presenter.MainPresenter;
import com.me.hurryuphup.domain.mypage.constant.MypageConstants;
import com.me.hurryuphup.domain.notification.view.NotificationList;
import com.me.hurryuphup.domain.user.constant.Constants;

public class Home extends Fragment implements MainView{
    private ActivityHomeBinding binding;
    MainPresenter presenter;

    @Override
    public void onResume() {
        presenter = new MainPresenter(this, binding, getContext(), getActivity());

        presenter.init();
        presenter.initializeAuctionNowData();
        presenter.initializeBestData();

        super.onResume();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ActivityHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        presenter = new MainPresenter(this, binding, getContext(), getActivity());

        presenter.init();
        presenter.initializeAuctionNowData();
        presenter.initializeBestData();

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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}