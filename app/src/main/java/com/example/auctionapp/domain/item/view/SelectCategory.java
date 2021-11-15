package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.home.view.UploadPage;

import static android.graphics.Color.GRAY;

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

        TextView completeBut = (TextView) findViewById(R.id.completeB);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                listview.setSelector(new PaintDrawable(GRAY));
                String category = (String) parent.getItemAtPosition(position);
                completeBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SelectCategory.this, UploadPage.class);
                        intent.putExtra("itemCategory", category);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });




    }
}
