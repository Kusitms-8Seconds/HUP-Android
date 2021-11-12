package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Home extends Fragment {
    ViewGroup viewGroup;
    BestItemAdapter adapterBest;
    //HomeAuctionDataAdapter adapterAuction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);
        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        RecyclerView BestrecyclerView = viewGroup.findViewById(R.id.BestItemRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        BestrecyclerView.setLayoutManager(linearLayoutManager);

        adapterBest = new BestItemAdapter();
        BestrecyclerView.setAdapter(adapterBest);

        adapterBest.setOnItemClickListener(new BestItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                Toast.makeText(getContext(), "pos: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ItemDetail.class);
                startActivity(intent);
            }
        });
    }
    private void getData(){
        //일단 레이아웃만
        BestItem dataBest = new BestItem(R.drawable.testitemimage, "아이폰 11 프로 256GB", "11월 10일", 100);
        adapterBest.addItem(dataBest);
        dataBest = new BestItem(R.drawable.testitemimage, "아이폰 11 프로 64GB", "11월 10일", 200);
        adapterBest.addItem(dataBest);
        dataBest = new BestItem(R.drawable.testitemimage, "아이폰 11 미니 A급 256GB", "11월 10일", 300);
        adapterBest.addItem(dataBest);
        dataBest = new BestItem(R.drawable.testitemimage, "아이폰 11 미니 256GB 레드 미개봉 중고", "11월 10일", 400);
        adapterBest.addItem(dataBest);

    }
}