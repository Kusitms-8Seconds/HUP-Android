package com.example.auctionapp.domain.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivityChatRoomBinding;
import com.example.auctionapp.domain.chat.model.User;
import com.example.auctionapp.domain.chat.presenter.ChatMessagePresenter;

public class ChatMessage extends AppCompatActivity implements ChatMessageView {
    private ActivityChatRoomBinding binding;
    private ChatMessagePresenter presenter;

    //uid
    private String destUid;     //상대방 uid
    private User destUser;
    String profileUrlStr;
    private Long EndItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new ChatMessagePresenter(this, binding, getApplicationContext());

        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        destUid = intent.getStringExtra("destUid");
        EndItemId = intent.getLongExtra("itemId", 0);

        presenter.init(destUid, EndItemId);
        presenter.sendMsg();
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}

