package com.example.auctionapp.domain.scrap.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityChatRoomBinding;
import com.example.auctionapp.databinding.ActivityHomeBinding;
import com.example.auctionapp.databinding.ActivityScrapBinding;
import com.example.auctionapp.domain.item.vc.ItemDetail;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.scrap.adapter.ScrapAdapter;
import com.example.auctionapp.domain.scrap.model.ScrapItem;
import com.example.auctionapp.domain.scrap.dto.ScrapDetailsResponse;
import com.example.auctionapp.domain.scrap.presenter.ScrapPresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Scrap extends AppCompatActivity implements ScrapView{
    private ActivityScrapBinding binding;
    ScrapPresenter presenter = new ScrapPresenter(this);

    ScrapAdapter adapter;
    ScrapItem data;
    List<ScrapItem> scrapItemsList = new ArrayList<>();
    int maximumPriceCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScrapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init();
        presenter.getData();

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public void init(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.scrapRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ScrapAdapter();
        binding.scrapRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ScrapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ItemDetail.class);
                intent.putExtra("itemId", scrapItemsList.get(position).getItemId());
                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.scrapRecyclerView.getContext(), new LinearLayoutManager(getApplicationContext()).getOrientation());
        binding.scrapRecyclerView.addItemDecoration(dividerItemDecoration);

    }




}
