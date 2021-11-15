package com.example.auctionapp.domain.item.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.auctionapp.R;
import com.google.android.material.tabs.TabLayout;

public class SellHistory extends AppCompatActivity {

    SellHistoryOngoing sellHistoryOngoing;
    SellHistoryEnd sellHistoryEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_history);

        sellHistoryOngoing = new SellHistoryOngoing();
        sellHistoryEnd = new SellHistoryEnd();

        getSupportFragmentManager().beginTransaction().add(R.id.sell_history_fragment, sellHistoryOngoing).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.sellhistory_tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0){
                    selected = sellHistoryOngoing;
                }else if (position == 1){
                    selected = sellHistoryEnd;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.sell_history_fragment, selected).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
    }
}
