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
import com.example.auctionapp.domain.pricesuggestion.view.BidPage;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemDetail extends AppCompatActivity {

    private ArrayList<Integer> itemImageList;
    private static final int DP = 24;
    int id = 2;     //item ID
    Boolean isHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

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

        //간단히 구현 - 나중에 수정필요
        ImageView heart = (ImageView) findViewById(R.id.isheart);
        isHeart = false;
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHeart) {
                    heart.setImageResource(R.drawable.heartx);
                    isHeart = false;
                }else {
                    heart.setImageResource(R.drawable.hearto);
                    isHeart = true;
                }
            }
        });

        //item delete method
        /*
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitTool.getAPIWithNullConverter().deleteItem(id)
                        .enqueue(MainRetrofitTool.getCallback(new ItemDetail.DeleteItemCallback()));
            }
        });
        */

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
    /*
    private class DeleteItemCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            DefaultResponse result = response.body();
            Log.d(TAG, "retrofit success: " + result.toString());
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
     */
}
