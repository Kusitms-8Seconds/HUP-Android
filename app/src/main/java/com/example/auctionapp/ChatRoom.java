package com.example.auctionapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

//        database = FirebaseDatabase.getInstance("https://fir-chat-7bf48-default-rtdb.asia-southeast1.firebasedatabase.app");
//        databaseReference = database.getReference("User");

        chat_recyclerView = findViewById(R.id.chattingRecyclerView); // id 연결
        chat_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chat_recyclerView.setLayoutManager(layoutManager);
        chatList = new ArrayList<>(); // 유저 객체를 담음

        adapter = new ChatAdapter(chatList, this);
        chat_recyclerView.setAdapter(adapter);

        chatList.add(new User("profile","name1","안녕하세요^^"));
        chatList.add(new User("profile","user","3333-10-1234567 카카오뱅크로 낙찰가 100000원 보내주세요~"));
        chatList.add(new User("profile","name1","네! 보냈습니다."));
        chatList.add(new User("profile","user","확인했습니다. 경매상품 안전하게 보내드릴게요~"));
        chatList.add(new User("profile","user","aaa"));

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
    String myName;

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
        holder.tv_message.setText(arrayList.get(position).getMessage());

        // 사용자에 따라 메세지 detail 조정_나중에 수정     //this.name
        if(arrayList.get(position).getName().equals("user")) {
            holder.tv_message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.tv_message.setGravity(Gravity.RIGHT);
            holder.tv_name.setVisibility(View.GONE);
            holder.iv_profile.setVisibility(View.GONE);

        }else {
            holder.tv_message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tv_message.setGravity(Gravity.LEFT);
            holder.tv_message.setBackgroundResource(R.drawable.chatbox_others);
            holder.tv_message.setTextColor(Color.BLACK);
            Glide.with(holder.itemView)
                    .load(arrayList.get(position).getProfile())
                    .into(holder.iv_profile);
//            holder.tv_name.setText(arrayList.get(position).getName());    //getName method
            holder.tv_name.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_profile;
        TextView tv_name;
        TextView tv_message;
        LinearLayout chat_layout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_message = itemView.findViewById(R.id.tv_message);
            this.chat_layout = itemView.findViewById(R.id.chat_layout);

        }
    }

}
