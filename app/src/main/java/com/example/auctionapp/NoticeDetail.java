package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoticeDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("noticeTitle");
        String date = intent.getStringExtra("noticeDate");

        TextView noticeTitle = (TextView) findViewById(R.id.noticeTitle);
        TextView noticeDate = (TextView) findViewById(R.id.noticeDate);
        noticeTitle.setText(title);
        noticeDate.setText(date);
    }
}
