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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {

    private RecyclerView chat_recyclerView;
    private ChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> chatList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        database = FirebaseDatabase.getInstance("https://fir-chat-7bf48-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = database.getReference("User");

        chat_recyclerView = findViewById(R.id.chattingRecyclerView); // id 연결
        chat_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chat_recyclerView.setLayoutManager(layoutManager);
        chatList = new ArrayList<>(); // 유저 객체를 담음

        adapter = new ChatAdapter(chatList, this);
        chat_recyclerView.setAdapter(adapter);

        chatList.add(new User("profile","name","message"));
        chatList.add(new User("profile","name2","message2"));
        chatList.add(new User("profile","name3","messag3"));
        chatList.add(new User("profile","name4","messag4"));

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
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_message.setText(arrayList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_profile;
        TextView tv_name;
        TextView tv_message;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_message = itemView.findViewById(R.id.tv_message);

        }
    }

}
