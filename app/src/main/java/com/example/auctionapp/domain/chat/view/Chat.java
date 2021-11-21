package com.example.auctionapp.domain.chat.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.user.constant.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends Fragment {

    ViewGroup viewGroup;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //uid
    String myuid = "판매자";       //String myuid = String.valueOf(Constants.userId);
    String chatRoomUid;
    //chatting room list
    ArrayList<chatListData> chatroomList = new ArrayList<chatListData>();
    chatListAdapter chatListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_chat, container, false);

        database = FirebaseDatabase.getInstance("https://auctionapp-f3805-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = database.getReference();

        ListView chattingRoomListView = (ListView) viewGroup.findViewById(R.id.chattingRoomListView);
        chatListAdapter = new chatListAdapter(this.getContext(), chatroomList);
        chattingRoomListView.setAdapter(chatListAdapter);

        databaseReference.child("chatrooms").orderByChild("users/"+myuid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
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
//                            chatroomList.add(temp2);
                            setChatList(chatRoomUid, temp2);
                        }
                    }

                }
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        chattingRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                chatListData destUser = (chatListData) adapterView.getItemAtPosition(position);
                String destUid = destUser.getProfileName();
                Intent intent = new Intent(getContext(), ChatRoom.class);
                intent.putExtra("destUid", destUid);
                startActivity(intent);
            }
        });

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void setChatList(String chatRoomUid, String oppName) {
        databaseReference.child("chatrooms/" + chatRoomUid + "/comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String lastChat = "";
                String lastChatTime = "";
                String lastChatTimeStr = "";
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) //마지막 채팅, 시간 가져오기
                {
                    lastChat = dataSnapshot.child("message").getValue(String.class);
                    lastChatTimeStr = dataSnapshot.child("timestamp").getValue(String.class);
                }
                String [] array = lastChatTimeStr.split("-");
                String month = array[1];
                String [] array2 = array[2].split("T");
                String [] array3 = array2[1].split(":");
                lastChatTime = month + "월 " + array2[0] + "일 " + array3[0] + ":" + array3[1];
                chatroomList.add(new chatListData("temp", oppName, lastChatTime, lastChat));
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
class chatListData {
    private String itemImage;
    private String profileName;
    private String chatTime;
    private String lastChat;

    public chatListData(){

    }

    public chatListData(String itemImage, String profileName, String chatTime, String lastChat){
        this.itemImage = itemImage;
        this.profileName = profileName;
        this.chatTime = chatTime;
        this.lastChat = lastChat;
    }
    public String getItemImage() {
        return this.itemImage;
    }
    public String getProfileName(){
        return this.profileName;
    }
    public String getChatTime(){
        return this.chatTime;
    }
    public String getLastChat(){
        return this.lastChat;
    }
}
class chatListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<chatListData> data;
    private ImageView itemImageImageView;
    private TextView profileNameTextView;
    private TextView chatTimeTextView;
    private TextView lastChatTextView;


    public chatListAdapter() {}
    public chatListAdapter(Context context, ArrayList<chatListData> dataArray) {
        mContext = context;
        data = dataArray;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public chatListData getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.custom_chatlist_listview, null);

        itemImageImageView = (ImageView) view.findViewById(R.id.iv_chatlist_itemImage);
//        Glide.with(view.getContext()).load(url).into(itemImageImageView);
        profileNameTextView = (TextView) view.findViewById(R.id.tv_chatlist_profileName);
        profileNameTextView.setText(data.get(position).getProfileName());
        chatTimeTextView = (TextView) view.findViewById(R.id.tv_chatlist_lastChatTime);
        chatTimeTextView.setText(data.get(position).getChatTime());
        lastChatTextView = (TextView) view.findViewById(R.id.tv_chatlist_lastChat);
        lastChatTextView.setText(data.get(position).getLastChat());

        return view;
    }

}