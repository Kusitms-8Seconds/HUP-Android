package com.example.auctionapp.domain.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.R;

public class NoticeDetail extends AppCompatActivity {

    String temp = "안녕하세요, HUP!입니다.\n" +
            "\n" +
            "더욱 발전된 서비스 제공을 위해 어플리케이션 점검이 진행될 예정입니다. 서비스 점검 시간에는 어플 사용이 잠시 중단됩니다. 사용자 여러분들의 양해를 부탁드립니다.\n" +
            "해당시간 내에 문의 사항이 있으실 경우 HUP! 고객센터 1600-9494로 전화 부탁드립니다.\n" +
            "\n" +
            "점검일시\n" +
            "2021년 11월 26일(금) 오전 3시 - 5시\n" +
            "업데이트 시간은 상황에 따라 단축 또는 연장될 수 있습니다. ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("noticeTitle");
        String date = intent.getStringExtra("noticeDate");

        TextView noticeTitle = (TextView) findViewById(R.id.noticeTitle);
        TextView noticeDate = (TextView) findViewById(R.id.noticeDate);
        TextView noticeContent = (TextView) findViewById(R.id.noticeContent);
        noticeTitle.setText(title);
        noticeDate.setText(date);
        noticeContent.setText(temp);    //dummy data

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
