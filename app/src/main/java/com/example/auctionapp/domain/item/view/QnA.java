package com.example.auctionapp.domain.item.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.auctionapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QnA extends AppCompatActivity {

    ArrayList<qnaData> qnaList = new ArrayList<qnaData>();
    qnaAdapter adapter;
//    TextView qnacount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //QNA dumidata
//        qnacount = (TextView) findViewById(R.id.qaCount);
        ListView qnaListView = (ListView) findViewById(R.id.qna_qnalist);
        adapter = new qnaAdapter(this.getApplicationContext(), qnaList);
        qnaListView.setAdapter(adapter);
        initializeQnAData();

        qnaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                qnaData data = (qnaData) parent.getItemAtPosition(position);
                Boolean isFolded = data.getFolded();
                if(isFolded) {
                    data.setFolded(false);
                    adapter.notifyDataSetChanged();
                }else {
                    data.setFolded(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        Intent intent = getIntent();
        Long itemId = intent.getLongExtra("itemId", 0);
        //go edit qna
        TextView goEditQNA = (TextView) findViewById(R.id.goEditQNA);
        goEditQNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditQnA.class);
                intent.putExtra("itemId", itemId);
                startActivity(intent);
            }
        });
    }
    public void initializeQnAData() {
        qnaList.add(new qnaData("자전거 많이 무겁나요?", "2021.11.27", "hoa9***", false, true));
        qnaList.add(new qnaData("기능 어떤 것들이 있나요?", "2021.11.26", "둠***", true, true));
        qnaList.add(new qnaData("자전거 브랜드 궁금합니다.", "2021.11.26", "우왕***", true, true));

//        qnacount.setText("(" + String.valueOf(qnaList.size()-1) + ")");
    }
}
