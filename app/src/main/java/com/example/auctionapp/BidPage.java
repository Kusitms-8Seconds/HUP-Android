package com.example.auctionapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class BidPage extends AppCompatActivity {

    Dialog dialog01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_page);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.bringToFront();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dialog01 = new Dialog(BidPage.this);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.custom_dialog01);

        ImageView bidButton = (ImageView) findViewById(R.id.bidbutton);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }
    // dialog01을 디자인하는 함수
    public void showDialog(){
        dialog01.show(); // 다이얼로그 띄우기

        // 홈으로 돌아가기 버튼
        ImageView goHome = dialog01.findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog01.dismiss(); // 다이얼로그 닫기
            }
        });
        // 참여내역 확인 버튼
        dialog01.findViewById(R.id.check_bidlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog01.dismiss(); // 다이얼로그 닫기
            }
        });
    }
}
