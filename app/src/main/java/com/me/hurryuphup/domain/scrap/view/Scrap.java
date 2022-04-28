package com.me.hurryuphup.domain.scrap.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivityScrapBinding;
import com.me.hurryuphup.domain.scrap.presenter.ScrapPresenter;

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
