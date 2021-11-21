package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.dto.DefaultResponse;
import com.example.auctionapp.domain.pricesuggestion.view.BidPage;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemDetail extends AppCompatActivity {

    private ArrayList<Integer> itemImageList;
    private static final int DP = 24;
    Long id = null;     //item ID (임시)
    public Boolean isHeart = false ;
    private Long itemId;
    private ImageView heart;
    private Long scrapId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();
        String getItemId = intent.getExtras().getString("itemId");
        this.itemId = Long.valueOf(getItemId);

        ImageView sellerImageView = (ImageView) findViewById(R.id.sellerImage);
        Glide.with(this).load(R.drawable.testuserimage).circleCrop().into(sellerImageView);

        ImageView participateButton = (ImageView) findViewById(R.id.participateButton);
        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BidPage.class);
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

        ViewPager viewPager = findViewById(R.id.itemDetailViewPager);
        viewPager.setAdapter(new ItemDetailViewPagerAdapter(this, itemImageList));

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

        itemImageList.add(R.drawable.testitemimage);
        itemImageList.add(R.drawable.testitemimage);
        itemImageList.add(R.drawable.testitemimage);
        itemImageList.add(R.drawable.testitemimage);
        itemImageList.add(R.drawable.testitemimage);
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
}
