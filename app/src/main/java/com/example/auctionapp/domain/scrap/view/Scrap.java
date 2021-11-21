package com.example.auctionapp.domain.scrap.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.view.ItemDetail;

public class Scrap extends AppCompatActivity {

    ScrapAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);

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
    }
    private void init(){
        recyclerView = findViewById(R.id.scrapRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ScrapAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ScrapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // temp
                Intent intent = new Intent(getApplicationContext(), ItemDetail.class);
                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getApplicationContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    private void getData(){
        //일단 레이아웃만
        ScrapItem data = new ScrapItem(R.drawable.rectangle, "아이폰 11 프로 256GB", 530000, "14:46");
        adapter.addItem(data);
        data = new ScrapItem(R.drawable.rectangle, "아이폰 11 프로 64GB", 500000, "82:33");
        adapter.addItem(data);
        data = new ScrapItem(R.drawable.rectangle, "아이폰 11 미니 A급 256GB", 420000, "34:07") ;
        adapter.addItem(data);
        data = new ScrapItem(R.drawable.rectangle, "아이폰 11 미니 256GB 레드 미개봉 중고", 552000, "34:04");
        adapter.addItem(data);

//        setAnimation();
    }
    public void setAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(200, 0, 200, 0);
        Animation alphaAnimation = new AlphaAnimation(0, 1);
        translateAnimation.setDuration(500);
        alphaAnimation.setDuration(1300);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(translateAnimation);
        animation.addAnimation(alphaAnimation);
        recyclerView.setAnimation(animation);


//        Animation animation = new AlphaAnimation(0, 1);
//        animation.setDuration(1500);
//        recyclerView.setAnimation(animation);
    }
}
