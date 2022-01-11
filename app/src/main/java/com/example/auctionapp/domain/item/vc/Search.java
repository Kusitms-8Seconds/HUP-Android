package com.example.auctionapp.domain.item.vc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivitySearchBinding;
import com.example.auctionapp.global.util.DBHelper;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 최근 검색어 db
        DBHelper helper;
        SQLiteDatabase db;
        helper = new DBHelper(Search.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        binding.searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(Search.this, "[검색버튼클릭] 검색어 = "+query, Toast.LENGTH_LONG).show();
                ContentValues values = new ContentValues();
                values.put("word", query);
                db.insert("recentsearch", null, values);
                // 화면 새로고침
                finish();
                overridePendingTransition(0, 0);
                Intent intent = getIntent();
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(MainActivity.this, "입력하고있는 단어 = "+newText, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        // 최근검색어 리스트뷰
        ArrayList searchwordList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, searchwordList);
        binding.searchWordList.setAdapter(adapter);

        Cursor c = db.query("recentsearch",null,null,null,null,null,null,null);
        int total = c.getCount();
        c.moveToPosition(total);    // db 거꾸로 읽기
        while(c.moveToPrevious()){
            String temp = c.getString(c.getColumnIndex("word"));    //빨간줄?
            searchwordList.add(temp);
            adapter.notifyDataSetChanged();
        }

        // 전체삭제
        binding.removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql3 = "DELETE FROM recentsearch;";
                db.execSQL(sql3);
                // 화면 새로고침
                finish();
                overridePendingTransition(0, 0);
                Intent intent = getIntent();
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }
}
