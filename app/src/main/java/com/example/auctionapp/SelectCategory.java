package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectCategory extends AppCompatActivity {

    static final String[] LIST_MENU = {"디지털기기", "생활가전", "가구/인테리어", "유아동", "생활/가공식품"
            ,"유아도서", "스포츠/레저", "여성잡화", "여성의류", "남성패션/잡화", "게임/취미", "뷰티/미용",
            "반려동물용품", "도서/티켓/음반", "식물"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.bringToFront();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        ListView listview = (ListView) findViewById(R.id.selectCategoryList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String category = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(SelectCategory.this, UploadPage.class);
                intent.putExtra("itemCategory", category);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }) ;

    }
}
