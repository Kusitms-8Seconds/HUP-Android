package com.me.hurryuphup.domain.mypage.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivitySafeTransactionSettingBinding;


public class SafeTransactionSetting extends AppCompatActivity {
    private ActivitySafeTransactionSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySafeTransactionSettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}

