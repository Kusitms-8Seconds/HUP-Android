package com.me.hurryuphup.domain.pricesuggestion.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivityBidPageBinding;
import com.me.hurryuphup.domain.pricesuggestion.presenter.BidPagePresenter;
import com.me.hurryuphup.domain.user.constant.Constants;

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
