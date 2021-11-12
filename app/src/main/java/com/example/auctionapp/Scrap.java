package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Scrap extends AppCompatActivity {

    ScrapAdapter adapter;
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
        RecyclerView recyclerView = findViewById(R.id.scrapRecyclerView);

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

    }
}
