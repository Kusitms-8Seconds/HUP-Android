package com.example.auctionapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class AuctionHistory extends AppCompatActivity {

    AuctionHistoryOngoing auctionHistoryOngoing;
    AuctionHistoryEnd auctionHistoryEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_history);

        auctionHistoryOngoing = new AuctionHistoryOngoing();
        auctionHistoryEnd = new AuctionHistoryEnd();

        getSupportFragmentManager().beginTransaction().add(R.id.auction_history_fragment, auctionHistoryOngoing).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0){
                    selected = auctionHistoryOngoing;
                }else if (position == 1){
                    selected = auctionHistoryEnd;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.auction_history_fragment, selected).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
