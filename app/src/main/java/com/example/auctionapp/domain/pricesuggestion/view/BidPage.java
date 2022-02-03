package com.example.auctionapp.domain.pricesuggestion.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.databinding.ActivityBidPageBinding;
import com.example.auctionapp.domain.item.adapter.PTAdapter;
import com.example.auctionapp.domain.item.model.BidParticipants;
import com.example.auctionapp.domain.item.view.AuctionHistory;
import com.example.auctionapp.domain.pricesuggestion.presenter.BidPagePresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.stomp.HupStomp;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;

import org.json.JSONException;

import java.util.ArrayList;

import lombok.SneakyThrows;

public class BidPage extends AppCompatActivity implements BidPageView {
    private ActivityBidPageBinding binding;
    private BidPagePresenter presenter;

    Dialog dialog01;
    Dialog dialog02;
    Dialog dialog03;
    PTAdapter ptAdapter;
    private HupStomp hupstomp;

    Long itemId;
    Long participantId;
    int finalPrice;
    Long myId = Constants.userId;

    TextView tv_timer;
    ConstraintLayout ly_editPrice;

    private int userCount;
    PTAdapter adapter;
    private ArrayList<BidParticipants> bidParticipants;
    RecyclerView ptRecyclerView;

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBidPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new BidPagePresenter(this, binding, getApplicationContext());

        Intent intent = getIntent();
        String getItemId = intent.getExtras().getString("itemId");
        this.itemId = Long.valueOf(getItemId);

        presenter.init(itemId);
        presenter.initializeData(itemId);

//        hupstomp = new HupStomp();
//        hupstomp.initStomp(adapter, bidParticipants, binding.highPrice, binding.participants);

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dialog01 = new Dialog(BidPage.this);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.custom_dialog01);


        // -----------만약 낙찰되었을 때에----------(임시) //
        finalPrice = Integer.parseInt(binding.highPrice.getText().toString());
        if(binding.itemLeftTime.getText().toString().equals("0분 전")) {
            for (int i = 0; i < ptAdapter.getItemCount() - 1; i++) {
                int maxprice = bidParticipants.get(i).getPtPrice();
                if (maxprice == finalPrice) {
                    participantId = bidParticipants.get(i).getUserId();
                }
            }
            dialog02 = new Dialog(BidPage.this);
            dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog02.setContentView(R.layout.custom_dialog02);
            showDialog02();

            dialog03 = new Dialog(BidPage.this);
            dialog03.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog03.setContentView(R.layout.custom_dialog03);
        }
    }
    // dialog01을 디자인하는 함수
    @Override
    public void showDialog01(){
        dialog01.show(); // 다이얼로그 띄우기

        // 홈으로 돌아가기 버튼
        dialog01.findViewById(R.id.goHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog01.dismiss();
                Intent intent = new Intent(BidPage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        // 참여내역 확인 버튼
        dialog01.findViewById(R.id.check_bidlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BidPage.this, AuctionHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog01.dismiss();
            }
        });
    }
    // dialog02을 디자인하는 함수
    @Override
    public void showDialog02(){
        dialog02.show(); // 다이얼로그 띄우기

        // 거절버튼
        ImageView goHome = dialog02.findViewById(R.id.refuseAuction);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog02.dismiss();
            }
        });
        // 거래 진행 버튼
        dialog02.findViewById(R.id.ongoAuction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeesDialog();
            }
        });
    }
    // dialog03을 디자인하는 함수
    @Override
    public void showFeesDialog(){
        dialog03.show(); // 다이얼로그 띄우기

        ImageView timer = dialog03.findViewById(R.id.iv_timer);
        Glide.with(this).load(R.raw.timer).into(timer);

        tv_timer = (TextView) dialog03.findViewById(R.id.tv_timer);
        class MyTimer extends CountDownTimer
        {
            public MyTimer(long millisInFuture, long countDownInterval)
            {
                super(millisInFuture, countDownInterval);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                tv_timer.setText(millisUntilFinished/1000 + 1 + "");
            }

            @Override
            public void onFinish() {
                Intent tt = new Intent(BidPage.this, FeesPage.class);
                tt.putExtra("itemId", itemId);
                tt.putExtra("participantId", participantId);
                tt.putExtra("finalPrice", finalPrice);
                tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tt);
            }
        }
        MyTimer myTimer = new MyTimer(3000, 1000);
        myTimer.start();
    }
}
