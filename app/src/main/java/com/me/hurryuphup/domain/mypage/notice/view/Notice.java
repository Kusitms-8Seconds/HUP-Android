package com.me.hurryuphup.domain.mypage.notice.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivityNoticeBinding;
import com.me.hurryuphup.domain.mypage.notice.presenter.NoticePresenter;
import com.me.hurryuphup.domain.user.constant.Constants;

public class Notice extends AppCompatActivity implements NoticeView{
    private ActivityNoticeBinding binding;
    private NoticePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new NoticePresenter(this, binding, getApplicationContext());

        presenter.init();

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //관리자일때 (admin)
        if(Constants.userId == null || Constants.userId != 100) binding.goUploadNotice.setVisibility(View.GONE);
        else if(Constants.userId == 100) binding.goUploadNotice.setVisibility(View.VISIBLE);

        binding.goUploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadNotice.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
//    @Override
//    public void init() {
////        noticeList.add(new NoticeData("[점검] 서비스 점검 예정 안내 ", "2021.11.24"));
////        noticeList.add(new NoticeData("개인정보 처리방침 안내", "2021.11.20"));
////        noticeList.add(new NoticeData("안전거래 안내", "2021.11.11"));
//    }
}
