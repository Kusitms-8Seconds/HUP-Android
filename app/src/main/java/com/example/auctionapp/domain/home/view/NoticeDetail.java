package com.example.auctionapp.domain.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.R;

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

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.bringToFront();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
