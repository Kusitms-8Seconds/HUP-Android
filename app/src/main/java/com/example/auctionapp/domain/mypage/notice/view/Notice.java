package com.example.auctionapp.domain.mypage.notice.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivityNoticeBinding;
import com.example.auctionapp.domain.mypage.notice.adapter.NoticeAdapter;
import com.example.auctionapp.domain.mypage.notice.constant.NoticeConstants;
import com.example.auctionapp.domain.mypage.notice.model.NoticeData;

import java.util.ArrayList;

public class Notice extends AppCompatActivity implements NoticeView{
    private ActivityNoticeBinding binding;

    ArrayList<NoticeData> noticeList = new ArrayList<NoticeData>();
    NoticeAdapter noticeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        noticeAdapter = new NoticeAdapter(this.getApplicationContext(), noticeList);
        binding.noticeListView.setAdapter(noticeAdapter);

        init();

        binding.noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String noticeTitle = noticeList.get(position).getNoticeTitle();
                String noticeDate = noticeList.get(position).getNoticeDate();
                Intent intent = new Intent(getApplicationContext(), NoticeDetail.class);
                intent.putExtra(NoticeConstants.ENoticeDetails.noticeTitle.getText(), noticeTitle);
                intent.putExtra(NoticeConstants.ENoticeDetails.noticeDate.getText(), noticeDate);
                startActivity(intent);
            }
        });

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    @Override
    public void init() {
        noticeList.add(new NoticeData("[점검] 서비스 점검 예정 안내 ", "2021.11.24"));
        noticeList.add(new NoticeData("개인정보 처리방침 안내", "2021.11.20"));
        noticeList.add(new NoticeData("안전거래 안내", "2021.11.11"));

        noticeAdapter.notifyDataSetChanged();
    }
}
