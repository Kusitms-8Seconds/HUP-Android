package com.example.auctionapp.domain.chat.presenter;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.auctionapp.domain.chat.model.ChatModel;
import com.example.auctionapp.domain.chat.model.chatListData;
import com.example.auctionapp.domain.chat.view.ChatView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatPresenter {
    private ChatView view;
    private chatListData model;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //uid
    String myuid = "상대방";
    String chatRoomUid;
    Long itemId;
    //chatting room list
    ArrayList<chatListData> chatroomList = new ArrayList<chatListData>();
    com.example.auctionapp.domain.chat.adapter.chatListAdapter chatListAdapter;

    public ChatPresenter(ChatView view) {
        this.view = view;
        this.model = new chatListData();
    }



}
