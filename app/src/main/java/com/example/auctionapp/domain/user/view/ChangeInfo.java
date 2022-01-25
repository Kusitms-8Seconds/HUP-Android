package com.example.auctionapp.domain.user.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivityChangeInfoBinding;
import com.example.auctionapp.domain.user.presenter.ChangeInfoPresenter;

public class ChangeInfo extends AppCompatActivity implements ChangeInfoView{
    private ActivityChangeInfoBinding binding;
    ChangeInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new ChangeInfoPresenter(this, binding, getApplicationContext());
    }
}
