package com.example.auctionapp.domain.pricesuggestion.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.databinding.ActivityBidPageBinding;
import com.example.auctionapp.domain.item.adapter.PTAdapter;
import com.example.auctionapp.domain.item.model.BidParticipants;
import com.example.auctionapp.domain.item.view.AuctionHistory;
import com.example.auctionapp.domain.pricesuggestion.presenter.BidPagePresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.stomp.PriceSuggestionStomp;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;

import java.util.ArrayList;

import lombok.SneakyThrows;

public class BidPage extends AppCompatActivity implements BidPageView {
    private ActivityBidPageBinding binding;
    private BidPagePresenter presenter;

    Long itemId;
    Long myId = Constants.userId;


    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBidPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new BidPagePresenter(this, binding, getApplicationContext());

        Intent intent = getIntent();
        this.itemId = intent.getLongExtra("itemId", 0);

        presenter.init(itemId);
        presenter.initializeData(itemId);

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
