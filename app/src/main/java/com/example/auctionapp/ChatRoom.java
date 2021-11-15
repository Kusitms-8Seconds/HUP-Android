package com.example.auctionapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {

    private ListView chat_recyclerView;
    private ChattingAdapter adapter;
    private ArrayList<User> chatList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

//        database = FirebaseDatabase.getInstance("https://fir-chat-7bf48-default-rtdb.asia-southeast1.firebasedatabase.app");
//        databaseReference = database.getReference("User");

        chat_recyclerView = findViewById(R.id.chattingRecyclerView);
        chatList = new ArrayList<>(); // 유저 객체를 담음

        adapter = new ChattingAdapter(ChatRoom.this, chatList);
        chat_recyclerView.setAdapter(adapter);

        chatList.add(new User("profile","name1","안녕하세요^^"));
        chatList.add(new User("profile","user","3333-10-1234567 카카오뱅크로 낙찰가 100000원 보내주세요~"));
        chatList.add(new User("profile","name1","네! 보냈습니다."));
        chatList.add(new User("profile","user","확인했습니다. 경매상품 안전하게 보내드릴게요~"));
        chatList.add(new User("profile","user","aaa"));
        chatList.add(new User("profile","user","aaa"));
        chatList.add(new User("profile","user","aaa"));
        chatList.add(new User("profile","user","aaa"));chatList.add(new User("profile","user","aaa"));chatList.add(new User("profile","user","aaa"));



    }
}
class User {
    private String profile;
    private String name;
    private String message;

    public User(){}
    public User(String profile, String name, String message) {
        this.profile = profile;
        this.name = name;
        this.message = message;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class ChattingAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<User> data;
    private ImageView userProfile;
    private TextView userName;
    private TextView chatContent;


    public ChattingAdapter() {}
    public ChattingAdapter(Context context, ArrayList<User> dataArray) {
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
    public User getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view;
        // usernam에 따라 위치조정 //this.name     //임시
        if(data.get(position).getName().equals("user")) {
            view = mLayoutInflater.inflate(R.layout.custom_chatting_mybox, null);
        }else {
            view = mLayoutInflater.inflate(R.layout.custom_chatting_otherbox, null);
        }


        userProfile = (ImageView) view.findViewById(R.id.iv_profile);
        Glide.with(view)
                .load(data.get(position).getProfile())
                .into(userProfile);
        userName = (TextView) view.findViewById(R.id.tv_name);
        userName.setText(data.get(position).getName());
        chatContent = (TextView) view.findViewById(R.id.tv_message);
        chatContent.setText(data.get(position).getMessage());

        return view;
    }

}
