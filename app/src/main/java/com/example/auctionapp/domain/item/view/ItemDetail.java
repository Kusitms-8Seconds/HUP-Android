package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.home.view.Home;
import com.example.auctionapp.domain.item.dto.DefaultResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.pricesuggestion.view.BidPage;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemDetail extends AppCompatActivity {

    private ArrayList<String> itemImageList;
    private static final int DP = 24;
    Long id = null;     //item ID (임시)
    public Boolean isHeart = false ;
    private Long itemId;
    private ImageView heart;
    private Long scrapId;
    TextView highPrice;
    TextView participants;
    TextView sellerName;
    ImageView sellerImageView;
    TextView itemName;
    TextView itemContent;
    TextView category;
    TextView itemLeftTime;

    ItemDetailViewPagerAdapter itemDetailViewPagerAdapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();
        String getItemId = intent.getExtras().getString("itemId");
        this.itemId = Long.valueOf(getItemId);

        sellerImageView = (ImageView) findViewById(R.id.sellerImage);
        //Glide.with(this).load(R.drawable.testuserimage).circleCrop().into(sellerImageView);

        ImageView participateButton = (ImageView) findViewById(R.id.participateButton);
        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BidPage.class);
                intent.putExtra("itemId", String.valueOf(itemId));
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

        // viewpager
        this.initializeImageData();

        viewPager = findViewById(R.id.itemDetailViewPager);
        itemDetailViewPagerAdapter = new ItemDetailViewPagerAdapter(this, itemImageList);
        viewPager.setAdapter(itemDetailViewPagerAdapter);

        //나중에 수정필요
        heart = (ImageView) findViewById(R.id.isheart);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                .isCheckedHeart(ScrapCheckedRequest.of(Constants.userId, itemId))
                .enqueue(MainRetrofitTool.getCallback(new isCheckedHeartCallback()));
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHeart) {
                        RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                                .deleteHeart(scrapId)
                                .enqueue(MainRetrofitTool.getCallback(new deleteScrapCallback()));
                }else {
                    RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                            .createScrap(ScrapRegisterRequest.of(Constants.userId, itemId))
                            .enqueue(MainRetrofitTool.getCallback(new createScrapCallback()));
                }
            }
        });

        highPrice = findViewById(R.id.highPrice);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getMaximumPrice(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceCallback()));

        participants = findViewById(R.id.participants);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getParticipants(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));

        sellerName = findViewById(R.id.sellerName);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(UserDetailsInfoRequest.of(Constants.userId))
                .enqueue(MainRetrofitTool.getCallback(new getUserDetailsCallback()));

        itemName = findViewById(R.id.itemName);
        itemContent = findViewById(R.id.itemContent);
        category = findViewById(R.id.category);
        itemLeftTime = findViewById(R.id.itemLeftTime);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getItemDetailsCallback()));


        //item delete method
//        Button deleteButton = (Button) findViewById(R.id.deleteButton);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).deleteItem(id)
//                        .enqueue(MainRetrofitTool.getCallback(new ItemDetail.DeleteItemCallback()));
//            }
//        });

    }
    public void initializeImageData()
    {
        itemImageList = new ArrayList();

//        itemImageList.add(R.drawable.testitemimage);
//        itemImageList.add(R.drawable.testitemimage);
//        itemImageList.add(R.drawable.testitemimage);
//        itemImageList.add(R.drawable.testitemimage);
//        itemImageList.add(R.drawable.testitemimage);
    }

    private class DeleteItemCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
//            heart.setImageResource(R.drawable.heartx);
//            isHeart = false;
//            scrapId = null;
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<DefaultResponse> response) {
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class deleteScrapCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            heart.setImageResource(R.drawable.heartx);
            isHeart = false;
            scrapId = null;
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<DefaultResponse> response) {
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class createScrapCallback implements MainRetrofitCallback<ScrapRegisterResponse> {
        @Override
        public void onSuccessResponse(Response<ScrapRegisterResponse> response) {
            heart.setImageResource(R.drawable.hearto);
            isHeart = true;
            scrapId = response.body().getScrapId();
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<ScrapRegisterResponse> response) {
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class isCheckedHeartCallback implements MainRetrofitCallback<ScrapCheckedResponse> {
        @Override
        public void onSuccessResponse(Response<ScrapCheckedResponse> response) {

            if(response.body().getScrapId()!=null){
                heart.setImageResource(R.drawable.hearto);
                isHeart = true;
                scrapId = response.body().getScrapId();
            } else{
                heart.setImageResource(R.drawable.heartx);
                isHeart = false;
                scrapId = null;
            }
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<ScrapCheckedResponse> response) {
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

    private class getUserDetailsCallback implements MainRetrofitCallback<UserDetailsInfoResponse> {

        @Override
        public void onSuccessResponse(Response<UserDetailsInfoResponse> response) {
            sellerName.setText(response.body().getUsername());
            if(!response.body().getPicture().isEmpty()){
                Glide.with(getApplicationContext()).load(response.body().getPicture()).into(sellerImageView);
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

    private class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {

        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            itemName.setText(response.body().getItemName());
            itemContent.setText(response.body().getDescription());
            if(response.body().getFileNames().size()!=0){
                for (int i=0; i<response.body().getFileNames().size(); i++) {
                    itemImageList.add(response.body().getFileNames().get(i));
                }
                itemDetailViewPagerAdapter = new ItemDetailViewPagerAdapter(getApplicationContext(), itemImageList);
                viewPager.setAdapter(itemDetailViewPagerAdapter);
            }
            category.setText(response.body().getCategory().getName());
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
}
