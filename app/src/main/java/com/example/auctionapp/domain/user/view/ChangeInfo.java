package com.example.auctionapp.domain.user.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivityChangeInfoBinding;
import com.example.auctionapp.domain.user.presenter.ChangeInfoPresenter;

public class ChangeInfo extends AppCompatActivity implements ChangeInfoView{
    private ActivityChangeInfoBinding binding;
    ChangeInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new ChangeInfoPresenter(this, binding, getApplicationContext());

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = presenter.UpdateCheck();
                if(intent !=null){
                    Toast.makeText(getApplicationContext(), "정보수정완료", Toast.LENGTH_SHORT).show();
                    startActivity(intent); } }
        });
        binding.checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.duplicateLoginIdCheck(binding.edtLoginId.getText().toString());
            }
        });
    }
}
