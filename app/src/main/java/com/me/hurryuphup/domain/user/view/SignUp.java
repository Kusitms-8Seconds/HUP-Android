package com.me.hurryuphup.domain.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivitySignUpBinding;
import com.me.hurryuphup.domain.user.presenter.SignUpPresenter;

public class SignUp extends AppCompatActivity implements SignUpView {
    private ActivitySignUpBinding binding;
    SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new SignUpPresenter(this, binding, getApplicationContext());

        binding.radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! binding.radioButton5.isChecked()){
                    binding.radioButton.setChecked(false);
                    binding.radioButton2.setChecked(false);
                    binding.radioButton3.setChecked(false);
                    binding.radioButton4.setChecked(false);
                }else {
                    binding.radioButton.setChecked(true);
                    binding.radioButton2.setChecked(true);
                    binding.radioButton3.setChecked(true);
                    binding.radioButton4.setChecked(true);
                }
            }
        });
        binding.checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.duplicateLoginIdCheck(binding.edtUserId.getText().toString());
            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = presenter.signUpCheck();
                if(intent !=null){
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

