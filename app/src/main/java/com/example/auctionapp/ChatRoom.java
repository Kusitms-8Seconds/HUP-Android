package com.example.auctionapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {

    private RecyclerView chat_recyclerView;
    private ChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chat_recyclerView = findViewById(R.id.chattingRecyclerView); // id 연결
        chat_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chat_recyclerView.setLayoutManager(layoutManager);
        chatList = new ArrayList<>(); // 유저 객체를 담음

        adapter = new ChatAdapter(chatList, this);
        chat_recyclerView.setAdapter(adapter);

        chatList.add(new User("aa","aa", 123,"aaa"));

    }
}
class User {
    private String profile;
    private String id;
    private int pw;
    private String userName;

    public User(){}
    public User(String profile, String id, int pw, String userName) {
        this.profile = profile;
        this.id = id;
        this.pw = pw;
        this.userName = userName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPw() {
        return pw;
    }

    public void setPw(int pw) {
        this.pw = pw;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;

    public ChatAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chatuser_recyclerview, parent, false);
        ChatViewHolder holder = new ChatViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);
        holder.tv_name.setText(arrayList.get(position).getId());
        holder.tv_message.setText(arrayList.get(position).getPw()+"");
        holder.tv_userName.setText(arrayList.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_profile;
        TextView tv_name;
        TextView tv_message;
        TextView tv_userName;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_message = itemView.findViewById(R.id.tv_message);

        }
    }

}
