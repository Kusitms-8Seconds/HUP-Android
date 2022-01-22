package com.example.auctionapp.domain.user.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivitySignUpBinding;
import com.example.auctionapp.databinding.ActivitySignupEmailcheckBinding;
import com.example.auctionapp.domain.user.presenter.EmailPresenter;
import com.example.auctionapp.domain.user.presenter.SignUpPresenter;

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
        System.out.println("email: "+email);

        binding.btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnSendEmail.setEnabled(false);
                binding.btnSendEmail.setText("발송되었습니다");
                binding.btnSendEmail.setTextColor(Color.WHITE);
                binding.lyCheckAuthcode.setVisibility(View.VISIBLE);
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
}
