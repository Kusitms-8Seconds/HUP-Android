package com.example.auctionapp.domain.mypage.notice.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivityUploadNoticeBinding;

public class UploadNotice extends AppCompatActivity {
    private ActivityUploadNoticeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadNoticeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}
