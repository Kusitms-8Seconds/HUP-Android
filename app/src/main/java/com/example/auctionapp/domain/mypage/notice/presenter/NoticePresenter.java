package com.example.auctionapp.domain.mypage.notice.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.example.auctionapp.databinding.ActivityNoticeBinding;
import com.example.auctionapp.domain.mypage.notice.adapter.NoticeAdapter;
import com.example.auctionapp.domain.mypage.notice.constant.NoticeConstants;
import com.example.auctionapp.domain.mypage.notice.model.NoticeData;
import com.example.auctionapp.domain.mypage.notice.view.NoticeDetail;
import com.example.auctionapp.domain.mypage.notice.view.NoticeView;

import java.util.ArrayList;

public class NoticePresenter implements NoticePresenterInterface {
    // Attributes
    private NoticeView noticeView;
    private ActivityNoticeBinding binding;
    private Context context;

    ArrayList<NoticeData> noticeList = new ArrayList<NoticeData>();
    NoticeAdapter noticeAdapter;

    // Constructor
    public NoticePresenter(NoticeView noticeView, ActivityNoticeBinding binding, Context context){
        this.noticeView = noticeView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void init() {
        noticeAdapter = new NoticeAdapter(context, noticeList);
        binding.noticeListView.setAdapter(noticeAdapter);

        binding.noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String noticeTitle = noticeList.get(position).getNoticeTitle();
                String noticeDate = noticeList.get(position).getNoticeDate();
                Intent intent = new Intent(context, NoticeDetail.class);
                intent.putExtra(NoticeConstants.ENoticeDetails.noticeTitle.getText(), noticeTitle);
                intent.putExtra(NoticeConstants.ENoticeDetails.noticeDate.getText(), noticeDate);
                context.startActivity(intent);
            }
        });

        noticeAdapter.notifyDataSetChanged();
    }
}
