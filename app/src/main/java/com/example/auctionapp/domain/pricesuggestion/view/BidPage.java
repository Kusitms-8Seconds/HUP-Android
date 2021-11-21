package com.example.auctionapp.domain.pricesuggestion.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
    PTAdapter ptAdapter;
    private HupStomp hupstomp;

    Long itemId;

    TextView highPrice;
    TextView participants;
    TextView itemLeftTime;
    ImageView auctionState;
    ImageView bidImage;
    private int userCount;
    PTAdapter adapter;

    private ArrayList<BidParticipants> bidParticipants;

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
//                showDialog();
//                String price = editPrice.getText().toString();
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

    private PTAdapter init(){
        RecyclerView ptRecyclerView = findViewById(R.id.participants_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        ptRecyclerView.setLayoutManager(linearLayoutManager);

        ptAdapter = new PTAdapter();
        ptRecyclerView.setAdapter(ptAdapter);
        return ptAdapter;
    }

    private void getData(){
        //일단 레이아웃만
//        BidParticipants data = new BidParticipants(R.drawable.testuserimage, "참여자1", 530000, "14:46");
//        ptAdapter.addItem(data);
//        data = new BidParticipants(R.drawable.testuserimage, "참여자2", 500000, "12:46");
//        ptAdapter.addItem(data);
//        data = new BidParticipants(R.drawable.testuserimage, "참여자3", 480000, "11:46");
//        ptAdapter.addItem(data);
//        data = new BidParticipants(R.drawable.testuserimage, "참여자4", 370000, "10:46");
//        ptAdapter.addItem(data);

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
            String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));
            itemLeftTime.setText(minutes+"분 전");
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
                BidParticipants data = new BidParticipants(R.drawable.testuserimage, null,
                        response.body().getData().get(i).getSuggestionPrice(), "12:46");
                bidParticipants.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(UserDetailsInfoRequest.of(response.body().getData().get(i).getUserId()))
                        .enqueue(MainRetrofitTool.getCallback(new getUserDetailsCallback()));
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
}
