package com.example.auctionapp.domain.scrap.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.auctionapp.databinding.ActivityScrapBinding;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.scrap.adapter.ScrapAdapter;
import com.example.auctionapp.domain.scrap.model.ScrapItem;
import com.example.auctionapp.domain.scrap.presenter.ScrapPresenter;

import java.util.ArrayList;
import java.util.List;

public class Scrap extends AppCompatActivity implements ScrapView{
    private ActivityScrapBinding binding;
    ScrapPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScrapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new ScrapPresenter(this, binding, getApplicationContext());

        presenter.init();
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
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
