package com.example.auctionapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BidPage extends AppCompatActivity {

    Dialog dialog01;
    PTAdapter ptAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_page);

        init();
        getData();

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

        EditText editPrice = (EditText) findViewById(R.id.editPrice);
        ImageView bidButton = (ImageView) findViewById(R.id.bidbutton);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                String price = editPrice.getText().toString();
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
                dialog01.dismiss();

                Intent intent = new Intent(BidPage.this, MainActivity.class);
                startActivity(intent);
                BidPage.this.finish();
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

    private void init(){
        RecyclerView ptRecyclerView = findViewById(R.id.participants_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        ptRecyclerView.setLayoutManager(linearLayoutManager);

        ptAdapter = new PTAdapter();
        ptRecyclerView.setAdapter(ptAdapter);
    }

    private void getData(){
        //일단 레이아웃만
        BidParticipants data = new BidParticipants(R.drawable.testuserimage, "참여자1", 530000, "14:46");
        ptAdapter.addItem(data);
        data = new BidParticipants(R.drawable.testuserimage, "참여자2", 500000, "12:46");
        ptAdapter.addItem(data);
        data = new BidParticipants(R.drawable.testuserimage, "참여자3", 480000, "11:46");
        ptAdapter.addItem(data);
        data = new BidParticipants(R.drawable.testuserimage, "참여자4", 370000, "10:46");
        ptAdapter.addItem(data);


    }
}
