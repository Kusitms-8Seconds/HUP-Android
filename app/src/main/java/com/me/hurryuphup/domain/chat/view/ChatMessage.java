package com.me.hurryuphup.domain.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivityChatRoomBinding;
import com.me.hurryuphup.domain.chat.presenter.ChatMessagePresenter;

import lombok.SneakyThrows;

public class ChatMessage extends AppCompatActivity implements ChatMessageView {
    private ActivityChatRoomBinding binding;
    private ChatMessagePresenter presenter;

    //uid
    private Long destUid;     //상대방 uid
    private Long EndItemId;
    private Long chatRoomId;

    @SneakyThrows
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
        destUid = intent.getLongExtra("destUid", 0);
        EndItemId = intent.getLongExtra("itemId", 0);
        chatRoomId = intent.getLongExtra("chatRoomId", 0);

        presenter.init(chatRoomId, destUid, EndItemId);
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}

