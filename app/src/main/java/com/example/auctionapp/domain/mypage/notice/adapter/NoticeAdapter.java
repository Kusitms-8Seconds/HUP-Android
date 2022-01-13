package com.example.auctionapp.domain.mypage.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.mypage.notice.model.NoticeData;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<NoticeData> data;
    private TextView noticeTitleTextView;
    private TextView noticeDateTextView;


    public NoticeAdapter() {}
    public NoticeAdapter(Context context, ArrayList<NoticeData> dataArray) {
        mContext = context;
        data = dataArray;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public NoticeData getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.custom_notice_listview, null);

        noticeTitleTextView = (TextView) view.findViewById(R.id.noticeTitleTxt);
        noticeTitleTextView.setText(data.get(position).getNoticeTitle());
        noticeDateTextView = (TextView) view.findViewById(R.id.noticeDateTxt);
        noticeDateTextView.setText(data.get(position).getNoticeDate());

        return view;
    }
}
