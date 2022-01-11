package com.example.auctionapp.domain.item.vc;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivitySellHistoryBinding;
import com.google.android.material.tabs.TabLayout;

public class SellHistory extends AppCompatActivity {
    private ActivitySellHistoryBinding binding;

    SellHistoryOngoing sellHistoryOngoing;
    SellHistoryEnd sellHistoryEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sellHistoryOngoing = new SellHistoryOngoing();
        sellHistoryEnd = new SellHistoryEnd();

        getSupportFragmentManager().beginTransaction().add(R.id.sell_history_fragment, sellHistoryOngoing).commit();

        binding.sellhistoryTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
