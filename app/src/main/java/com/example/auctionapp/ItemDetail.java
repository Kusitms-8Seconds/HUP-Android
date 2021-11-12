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
        viewPager.setClipToPadding(false);
        // viewpager detail
        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        viewPager.setAdapter(new ItemDetailViewPagerAdapter(this, itemImageList));

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
