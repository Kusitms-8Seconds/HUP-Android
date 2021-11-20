package com.example.auctionapp.domain.chat.view;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
//    String myuid = String.valueOf(Constants.userId);
    String myuid = "판매자";
    //chatting room list
    List<String> chatroomList = new ArrayList<>();
    String chatRoomUid;

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

        databaseReference.child("chatrooms").orderByChild("users/"+myuid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) //나, 상대방 id 가져온다.
                {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    chatRoomUid = dataSnapshot.getKey();
                    String temp = chatModel.users.toString();
                    String [] array = temp.split("=true");
                    for(int i=0; i<array.length; i++) {
                        String temp2 = array[i];
//                        System.out.println(temp2);
                        if(temp2.contains(myuid)) continue;
                        temp2 = temp2.replace("{","");
                        temp2 = temp2.replace("}","");
                        temp2 = temp2.replace(",","");
                        temp2 = temp2.replace(" ","");
                        if(!temp2.equals(myuid) && !temp2.equals("")) {
                            chatroomList.add(temp2);
                            System.out.println(temp2);
                            adpater.notifyDataSetChanged();
                        }
                    }

                }
//                adpater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
//    public void addChattingRoomList(String chatRoomUid) {
//        databaseReference.child("chatrooms").child(chatRoomUid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()) //나, 상대방 id 가져온다.
//                {
////                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
////                    if(chatModel.users.containsKey(myuid)){           //내 id 포함돼 있을때 채팅방 key 가져옴
////                        chatRoomUid = dataSnapshot.getKey();
////                        addChattingRoomList(chatRoomUid);
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//    }

}