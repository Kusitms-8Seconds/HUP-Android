package com.example.auctionapp.domain.pricesuggestion.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.item.view.ItemDetailViewPagerAdapter;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionListResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.stomp.HupStomp;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class BidPage extends AppCompatActivity {

    Dialog dialog01;
    Dialog dialog02;
    Dialog dialog03;
    PTAdapter ptAdapter;
    private HupStomp hupstomp;

    Long itemId;
    Long participantId;
    int finalPrice;
    Long myId = Constants.userId;

    TextView highPrice;
    TextView participants;
    TextView itemLeftTime;
    ImageView auctionState;
    ImageView bidImage;
    TextView tv_timer;
    ConstraintLayout ly_editPrice;

    private int userCount;
    PTAdapter adapter;
    private ArrayList<BidParticipants> bidParticipants;
    RecyclerView ptRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_page);

        Intent intent = getIntent();
        String getItemId = intent.getExtras().getString("itemId");
        this.itemId = Long.valueOf(getItemId);
        bidParticipants = new ArrayList<>();
        adapter = init();

        getData();

        itemLeftTime = findViewById(R.id.itemLeftTime);
        highPrice = findViewById(R.id.highPrice);
        participants = findViewById(R.id.participants);
        auctionState = findViewById(R.id.auctionState);
        bidImage = findViewById(R.id.bidImage);
        ly_editPrice = findViewById(R.id.ly_editPrice);

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getItemDetailsCallback()));
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getMaximumPrice(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceCallback()));
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getParticipants(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));

        hupstomp = new HupStomp();
        hupstomp.initStomp(adapter, bidParticipants, highPrice, participants);

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
                try {
                    if(!editPrice.getText().toString().equals("")){
                        hupstomp.sendMessage(itemId, Constants.userId, editPrice.getText().toString());
                    }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
                showDialog01();
//                String price = editPrice.getText().toString();
            }
        });

        // -----------만약 낙찰되었을 때에----------(임시) //
        finalPrice = Integer.parseInt(highPrice.getText().toString());
        if(itemLeftTime.getText().toString().equals("0분 전")) {
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
    public void showDialog01(){
        dialog01.show(); // 다이얼로그 띄우기

        // 홈으로 돌아가기 버튼
        ImageView goHome = dialog01.findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
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

    private PTAdapter init(){
        ptRecyclerView = findViewById(R.id.participants_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        ptRecyclerView.setLayoutManager(linearLayoutManager);

        ptAdapter = new PTAdapter();
        ptRecyclerView.setAdapter(ptAdapter);
        return ptAdapter;
    }

    private void getData(){
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllPriceSuggestionByItemId(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getAllPriceSuggestionCallback()));
    }

    private class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {

        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            if(response.body().getFileNames().size()!=0){
                Glide.with(getApplicationContext()).load(Constants.imageBaseUrl+response.body().getFileNames().get(0))
                        .into(bidImage); }
            LocalDateTime startDateTime = LocalDateTime.now();
            LocalDateTime endDateTime = response.body().getAuctionClosingDate();
            long tmp = ChronoUnit.MINUTES.between(startDateTime, endDateTime); // 시작시간 ~ 끝나는 시간을 minute으로 환산
            long tmpMinute = 0;
            long tmpHour = 0;
            long tmpDay = 0;

            tmpMinute = tmp % 60;
            tmpHour = tmp / 60;

            tmpDay = tmpHour/24;
            tmpHour = tmpHour%24;

            String days = String.valueOf(tmpDay);
            String hours = String.valueOf(tmpHour);
            String minutes = String.valueOf(tmpMinute);
            itemLeftTime.setText(days+"일 "+hours+"시간 "+minutes+"분 전");
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getMaximumPriceCallback implements MainRetrofitCallback<MaximumPriceResponse> {

        @Override
        public void onSuccessResponse(Response<MaximumPriceResponse> response) throws IOException {

            highPrice.setText(String.valueOf(response.body().getMaximumPrice()));
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getParticipantsCallback implements MainRetrofitCallback<ParticipantsResponse> {

        @Override
        public void onSuccessResponse(Response<ParticipantsResponse> response) {
            participants.setText(String.valueOf(response.body().getParticipantsCount()));
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ParticipantsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getAllPriceSuggestionCallback implements MainRetrofitCallback<PaginationDto<List<PriceSuggestionListResponse>>> {

        @Override
        public void onSuccessResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                BidParticipants data = new BidParticipants(response.body().getData().get(i).getUserId(), R.drawable.hearto, null,
                        response.body().getData().get(i).getSuggestionPrice(), "12:46");
                bidParticipants.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(UserDetailsInfoRequest.of(response.body().getData().get(i).getUserId()))
                        .enqueue(MainRetrofitTool.getCallback(new getUserDetailsCallback()));
                setAnimation();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getUserDetailsCallback implements MainRetrofitCallback<UserDetailsInfoResponse> {

        @Override
        public void onSuccessResponse(Response<UserDetailsInfoResponse> response) {

            bidParticipants.get(userCount).setPtName(response.body().getUsername());
            ptAdapter.addItem(bidParticipants.get(userCount));
            ptAdapter.notifyDataSetChanged();
            userCount++;
            if(response.body().getUserId().equals(myId)) {
                ly_editPrice.setVisibility(View.GONE);
            }else {
                ly_editPrice.setVisibility(View.VISIBLE);
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<UserDetailsInfoResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    public void setAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(200, 0, 0, 0);
        Animation alphaAnimation = new AlphaAnimation(0, 1);
        translateAnimation.setDuration(500);
        alphaAnimation.setDuration(1300);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(translateAnimation);
        animation.addAnimation(alphaAnimation);
        ptRecyclerView.setAnimation(animation);


//        Animation animation = new AlphaAnimation(0, 1);
//        animation.setDuration(1500);
//        recyclerView.setAnimation(animation);
    }
}
