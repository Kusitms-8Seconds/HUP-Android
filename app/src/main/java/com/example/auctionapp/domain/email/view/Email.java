package com.example.auctionapp.domain.email.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivitySignupEmailcheckBinding;
import com.example.auctionapp.domain.email.presenter.EmailPresenter;

public class Email extends AppCompatActivity implements EmailView {
    private ActivitySignupEmailcheckBinding binding;
    EmailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupEmailcheckBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new EmailPresenter(this, binding, getApplicationContext());

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        binding.tvEmailCheck.setText(email);

        binding.btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendEmail(email);
            }
        });
        binding.btnCodeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authCode = binding.edtCodeCheck.getText().toString();
                presenter.checkCode(authCode);
            }
        });
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
