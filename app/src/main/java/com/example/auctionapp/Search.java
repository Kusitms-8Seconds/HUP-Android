package com.example.auctionapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        DBHelper helper;
        SQLiteDatabase db;
        helper = new DBHelper(Search.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        SearchView searchView = (SearchView) findViewById(R.id.searchView2);
        // SearchView 검색어 입력/검색 이벤트 처리
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Search.this, "[검색버튼클릭] 검색어 = "+query, Toast.LENGTH_LONG).show();
                ContentValues values = new ContentValues();
                values.put("word", query);
                db.insert("recentsearch", null, values);
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
        while(c.moveToNext()){
            String temp = c.getString(c.getColumnIndex("word"));    //빨간줄 왜 뜨지
            searchwordList.add(temp);
            adapter.notifyDataSetChanged();
        }


    }
}
