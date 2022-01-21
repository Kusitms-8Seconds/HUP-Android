package com.example.auctionapp.domain.chat.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivityChatRoomBinding;
import com.example.auctionapp.domain.chat.presenter.ChatRoomPresenter;

public class ChatRoom extends AppCompatActivity implements ChatRoomView{
    private ActivityChatRoomBinding binding;
    private ChatRoomPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new ChatRoomPresenter(this, binding, getApplicationContext());

        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        presenter.init();
        presenter.sendMsg();
    }

}

