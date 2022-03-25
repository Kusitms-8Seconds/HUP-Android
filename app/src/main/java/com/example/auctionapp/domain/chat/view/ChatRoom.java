package com.example.auctionapp.domain.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.auctionapp.databinding.ActivityChatBinding;
import com.example.auctionapp.domain.chat.model.chatListData;
import com.example.auctionapp.domain.chat.presenter.ChatRoomPresenter;
import com.example.auctionapp.domain.user.constant.Constants;

import lombok.SneakyThrows;

public class ChatRoom extends Fragment implements ChatRoomView {
    private ActivityChatBinding binding;
    ChatRoomPresenter presenter;

    @Override
    public void onResume() {
        init();
        super.onResume();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ActivityChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init() {
        presenter = new ChatRoomPresenter(this, binding, this.getContext());
        if(Constants.userId != null) {
            binding.afterLogin.setVisibility(View.GONE);
            binding.chattingRoomListView.setVisibility(View.VISIBLE);
            binding.image.setVisibility(View.VISIBLE);
            presenter.init();
        }
        else {
            binding.afterLogin.setVisibility(View.VISIBLE);
            binding.chattingRoomListView.setVisibility(View.GONE);
            binding.image.setVisibility(View.GONE);
        }

        binding.chattingRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                chatListData destUser = (chatListData) adapterView.getItemAtPosition(position);
                Long destUid = destUser.getDestId();
                Long destItemId = destUser.getItemId();
                Long chatRoomId = destUser.getChatroomId();
                Intent intent = new Intent(getContext(), ChatMessage.class);
                intent.putExtra("destUid", destUid);
                intent.putExtra("itemId", destItemId);
                intent.putExtra("chatRoomId", chatRoomId);
                startActivity(intent);
            }
        });
    }
}

