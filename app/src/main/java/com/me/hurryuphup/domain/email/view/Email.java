package com.me.hurryuphup.domain.email.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivitySignupEmailcheckBinding;
import com.me.hurryuphup.domain.email.presenter.EmailPresenter;

public class Email extends AppCompatActivity implements EmailView {
    private ActivitySignupEmailcheckBinding binding;
    EmailPresenter presenter;
    MyTimer myTimer;

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
                myTimer = new MyTimer(1800000, 1000);
                myTimer.start();
            }
        });
        binding.btnReSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendEmail(email);
                // timer
                myTimer.cancel();
                myTimer = new MyTimer(1800000, 1000);
                myTimer.start();
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
    // update time with handler and timertask
    class MyTimer extends CountDownTimer
    {
        public MyTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            long minute = millisUntilFinished/60000;
            long second = (millisUntilFinished/1000) % 60;
            if(second < 10) binding.tvTimer.setText(minute + ":0" + second);
            else binding.tvTimer.setText(minute + ":" + second);
        }
        @Override
        public void onFinish() {
            binding.tvTimer.setText("0");
        }
    }
}
