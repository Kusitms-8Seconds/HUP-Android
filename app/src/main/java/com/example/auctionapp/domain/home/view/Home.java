package com.example.auctionapp.domain.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.view.BestItem;
import com.example.auctionapp.domain.item.view.BestItemAdapter;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.pricesuggestion.view.AuctionNow;
import com.example.auctionapp.domain.pricesuggestion.view.AuctionNowAdapter;

import java.util.ArrayList;

public class Home extends Fragment {
    ViewGroup viewGroup;
    private ArrayList<BestItem> bestItemArrayList;

    AuctionNowAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);

        init();
        initializeBestData();
        initializeAuctionNowData();

        ViewPager BestItemViewPager = viewGroup.findViewById(R.id.bestItemViewPager);
        BestItemViewPager.setAdapter(new BestItemAdapter(getContext(), bestItemArrayList));

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){
        RecyclerView AuctionNowRecyclerView = viewGroup.findViewById(R.id.AuctionNowView);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),2);
        AuctionNowRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AuctionNowAdapter();
        AuctionNowRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AuctionNowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), ItemDetail.class);
                startActivity(intent);
            }
        });


    }


    public void initializeBestData()
    {
        bestItemArrayList = new ArrayList();

        bestItemArrayList.add(new BestItem(R.drawable.testitemimage, "1", "1", 1));
        bestItemArrayList.add(new BestItem(R.drawable.testitemimage, "2", "2", 2));
        bestItemArrayList.add(new BestItem(R.drawable.testitemimage, "3", "3", 3));

    }
    public void initializeAuctionNowData()
    {
        AuctionNow data = new AuctionNow(R.drawable.testitemimage, "아이폰 11 프로 256GB", 530000, "1","1");
        adapter.addItem(data);
        data = new AuctionNow(R.drawable.testitemimage, "아이폰 11 프로 64GB", 500000, "82:33", "2");
        adapter.addItem(data);
        data = new AuctionNow(R.drawable.testitemimage, "아이폰 11 미니 A급 256GB", 420000, "34:07", "3");
        adapter.addItem(data);
        data = new AuctionNow(R.drawable.testitemimage, "아이폰 11 미니 256GB 레드 미개봉 중고", 552000, "34:04", "4");
        adapter.addItem(data);
    }
}