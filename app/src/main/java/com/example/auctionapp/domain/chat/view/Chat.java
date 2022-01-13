package com.example.auctionapp.domain.chat.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.auctionapp.databinding.ActivityChatBinding;
import com.example.auctionapp.domain.chat.adapter.chatListAdapter;
import com.example.auctionapp.domain.chat.model.ChatModel;
import com.example.auctionapp.domain.chat.model.chatListData;
import com.example.auctionapp.domain.chat.presenter.ChatPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends Fragment implements ChatView {
    private ActivityChatBinding binding;
    ChatPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ActivityChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        presenter = new ChatPresenter(this, binding, this.getContext());

        presenter.init();
        presenter.getChatList();

        binding.chattingRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                chatListData destUser = (chatListData) adapterView.getItemAtPosition(position);
                String destUid = destUser.getProfileName();
                Long destItemId = destUser.getItemId();
                Intent intent = new Intent(getContext(), ChatRoom.class);
                intent.putExtra("destUid", destUid);
                intent.putExtra("itemId", destItemId);
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}

