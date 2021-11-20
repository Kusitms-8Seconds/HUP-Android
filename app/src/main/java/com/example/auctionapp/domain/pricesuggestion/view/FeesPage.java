package com.example.auctionapp.domain.pricesuggestion.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.view.ItemDetail;

public class FeesPage extends AppCompatActivity {

    Boolean isFolded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_page);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ImageView goSeeItemDetail = (ImageView) findViewById(R.id.goSeeItemDetail);
        goSeeItemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemDetail.class);
                startActivity(intent);
            }
        });
        // 결제수단 창 접기
        isFolded = false;
        ConstraintLayout buymethod = (ConstraintLayout) findViewById(R.id.buymethod);
        ImageView upIcon = (ImageView) findViewById(R.id.upIcon);
        buymethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout ly_buymethod = (ConstraintLayout) findViewById(R.id.ly_buymethod);
                if(isFolded) {
                    ly_buymethod.setVisibility(View.VISIBLE);
                    upIcon.setImageResource(R.drawable.fold_up);
                    isFolded = false;
                }else {
                    ly_buymethod.setVisibility(View.GONE);
                    upIcon.setImageResource(R.drawable.down);
                    isFolded = true;
                }
            }
        });
        // 결제수단 선택
        TextView method_card = (TextView) findViewById(R.id.method_card);
        TextView method_account = (TextView) findViewById(R.id.method_account);
        TextView method_kakaopay = (TextView) findViewById(R.id.method_kakaopay);
        TextView method_no_account = (TextView) findViewById(R.id.method_no_account);
        TextView tv_tv = (TextView) findViewById(R.id.tv_tv);
        TextView tv_buyMethod = (TextView) findViewById(R.id.tv_buyMethod);
        tv_tv.setPaintFlags(tv_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        method_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_card.setBackgroundResource(R.drawable.selected_blue);
                method_card.setTextColor(Color.WHITE);
                method_account.setBackgroundResource(R.drawable.box_edge);
                method_account.setTextColor(Color.BLACK);
                method_kakaopay.setBackgroundResource(R.drawable.box_edge);
                method_kakaopay.setTextColor(Color.BLACK);
                method_no_account.setBackgroundResource(R.drawable.box_edge);
                method_no_account.setTextColor(Color.BLACK);
                tv_tv.setText(method_card.getText().toString() + " 안내");
                tv_buyMethod.setText(method_card.getText().toString());
            }
        });
        method_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_account.setBackgroundResource(R.drawable.selected_blue);
                method_account.setTextColor(Color.WHITE);
                method_card.setBackgroundResource(R.drawable.box_edge);
                method_card.setTextColor(Color.BLACK);
                method_kakaopay.setBackgroundResource(R.drawable.box_edge);
                method_kakaopay.setTextColor(Color.BLACK);
                method_no_account.setBackgroundResource(R.drawable.box_edge);
                method_no_account.setTextColor(Color.BLACK);
                tv_tv.setText(method_account.getText().toString() + " 안내");
                tv_buyMethod.setText(method_account.getText().toString());
            }
        });
        method_kakaopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_kakaopay.setBackgroundResource(R.drawable.selected_blue);
                method_kakaopay.setTextColor(Color.WHITE);
                method_account.setBackgroundResource(R.drawable.box_edge);
                method_account.setTextColor(Color.BLACK);
                method_card.setBackgroundResource(R.drawable.box_edge);
                method_card.setTextColor(Color.BLACK);
                method_no_account.setBackgroundResource(R.drawable.box_edge);
                method_no_account.setTextColor(Color.BLACK);
                tv_tv.setText(method_kakaopay.getText().toString() + " 안내");
                tv_buyMethod.setText(method_kakaopay.getText().toString());
            }
        });
        method_no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_no_account.setBackgroundResource(R.drawable.selected_blue);
                method_no_account.setTextColor(Color.WHITE);
                method_account.setBackgroundResource(R.drawable.box_edge);
                method_account.setTextColor(Color.BLACK);
                method_kakaopay.setBackgroundResource(R.drawable.box_edge);
                method_kakaopay.setTextColor(Color.BLACK);
                method_card.setBackgroundResource(R.drawable.box_edge);
                method_card.setTextColor(Color.BLACK);
                tv_tv.setText(method_no_account.getText().toString() + " 안내");
                tv_buyMethod.setText(method_no_account.getText().toString());
            }
        });

    }
}
