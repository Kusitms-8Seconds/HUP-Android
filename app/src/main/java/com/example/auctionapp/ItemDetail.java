package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemDetail extends AppCompatActivity {

    private ArrayList<Integer> itemImageList;
    private static final int DP = 24;
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
        // viewpager detail
//        viewPager.setClipToPadding(false);
//        float density = getResources().getDisplayMetrics().density;
//        int margin = (int) (DP * density);
//        viewPager.setPadding(margin, 0, margin, 0);
//        viewPager.setPageMargin(margin/2);

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
}
