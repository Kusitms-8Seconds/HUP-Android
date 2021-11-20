package com.example.auctionapp.domain.chat.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.user.constant.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chat extends Fragment {

    ViewGroup viewGroup;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //user
    String myuid = String.valueOf(Constants.userId);
    //chatting room list
    List<String> chatroomList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_chat, container, false);

        database = FirebaseDatabase.getInstance("https://auctionapp-f3805-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = database.getReference();

        ListView chattingRoomListView = (ListView) viewGroup.findViewById(R.id.chattingRoomListView);
        ArrayAdapter<String> adpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, chatroomList);
        chattingRoomListView.setAdapter(adpater);


        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}