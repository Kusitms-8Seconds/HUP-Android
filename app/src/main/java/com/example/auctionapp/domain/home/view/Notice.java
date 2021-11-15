package com.example.auctionapp.domain.home.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.R;

import java.util.ArrayList;

public class Notice extends AppCompatActivity {

    ArrayList<NoticeData> noticeList = new ArrayList<NoticeData>();
    NoticeAdapter noticeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        ListView noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeAdapter = new NoticeAdapter(this.getApplicationContext(), noticeList);
        noticeListView.setAdapter(noticeAdapter);

        init();

        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String noticeTitle = noticeList.get(position).getNoticeTitle();
                String noticeDate = noticeList.get(position).getNoticeDate();
                Intent intent = new Intent(getApplicationContext(), NoticeDetail.class);
                intent.putExtra("noticeTitle", noticeTitle);
                intent.putExtra("noticeDate", noticeDate);
                startActivity(intent);
            }
        });

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.bringToFront();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    public void init() {
        noticeList.add(new NoticeData("[점검] 서비스 점검 예정 안내 ", "2021.11.24"));
        noticeList.add(new NoticeData("개인정보 처리방침 안내", "2021.11.20"));
        noticeList.add(new NoticeData("안전거래 안내", "2021.11.11"));

        noticeAdapter.notifyDataSetChanged();
    }
}

class NoticeData{
    private String noticeTitle;
    private String noticeDate;

    public NoticeData(){

    }

    public NoticeData(String noticeTitle, String noticeDate){
        this.noticeTitle = noticeTitle;
        this.noticeDate = noticeDate;
    }
    public String getNoticeTitle() {
        return this.noticeTitle;
    }
    public String getNoticeDate(){
        return this.noticeDate;
    }

}

class NoticeAdapter extends BaseAdapter {
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
