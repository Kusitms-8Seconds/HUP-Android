package com.me.hurryuphup.domain.item.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.me.hurryuphup.R;
import com.me.hurryuphup.databinding.ActivityAuctionHistoryBinding;
import com.google.android.material.tabs.TabLayout;

public class AuctionHistory extends AppCompatActivity {
    private ActivityAuctionHistoryBinding binding;

    AuctionHistoryOngoing auctionHistoryOngoing;
    AuctionHistoryEnd auctionHistoryEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuctionHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auctionHistoryOngoing = new AuctionHistoryOngoing();
        auctionHistoryEnd = new AuctionHistoryEnd();

        getSupportFragmentManager().beginTransaction().add(R.id.auction_history_fragment, auctionHistoryOngoing).commit();

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
