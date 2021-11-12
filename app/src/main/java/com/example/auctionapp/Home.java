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
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class Home extends Fragment {
    ViewGroup viewGroup;
    private ArrayList<BestItem> bestItemArrayList;
    //HomeAuctionDataAdapter adapterAuction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);

        initializeBestData();

        ViewPager BestItemViewPager = viewGroup.findViewById(R.id.bestItemViewPager);
//        BestItemViewPager.setClipToPadding(false);
        BestItemViewPager.setAdapter(new BestItemAdapter(getContext(), bestItemArrayList));


        return viewGroup;
    }


    private void init() {



    }
    public void initializeBestData()
    {
        bestItemArrayList = new ArrayList();

        bestItemArrayList.add(new BestItem(R.drawable.testitemimage, "1", "1", 1));
        bestItemArrayList.add(new BestItem(R.drawable.testitemimage, "2", "2", 2));
        bestItemArrayList.add(new BestItem(R.drawable.testitemimage, "3", "3", 3));

    }
}