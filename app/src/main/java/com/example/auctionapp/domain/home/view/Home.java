package com.example.auctionapp.domain.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.auctionapp.databinding.ActivityHomeBinding;
import com.example.auctionapp.domain.home.constant.HomeConstants;
import com.example.auctionapp.domain.home.presenter.MainPresenter;
import com.example.auctionapp.domain.home.model.BestItem;
import com.example.auctionapp.domain.home.adapter.BestItemAdapter;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.home.model.AuctionNow;
import com.example.auctionapp.domain.home.adapter.AuctionNowAdapter;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Home extends Fragment implements MainView{
    private ActivityHomeBinding binding;
    MainPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ActivityHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        presenter = new MainPresenter(this, binding, getContext(), getActivity());

        presenter.init();
        presenter.initializeAuctionNowData();
        presenter.initializeBestData();

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}