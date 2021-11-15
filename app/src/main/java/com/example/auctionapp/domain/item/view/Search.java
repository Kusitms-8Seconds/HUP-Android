package com.example.auctionapp.domain.item.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.R;
import com.example.auctionapp.global.util.DBHelper;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageView goBack = (ImageView) findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
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

        SearchView searchView = (SearchView) findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ListView listview = (ListView) findViewById(R.id.searchWordList);
        listview.setAdapter(adapter);

        Cursor c = db.query("recentsearch",null,null,null,null,null,null,null);
        int total = c.getCount();
        c.moveToPosition(total);    // db 거꾸로 읽기
        while(c.moveToPrevious()){
            String temp = c.getString(c.getColumnIndex("word"));    //빨간줄?
            searchwordList.add(temp);
            adapter.notifyDataSetChanged();
        }

        // 전체삭제
        TextView removeAll = (TextView) findViewById(R.id.removeAll);
        removeAll.setOnClickListener(new View.OnClickListener() {
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
