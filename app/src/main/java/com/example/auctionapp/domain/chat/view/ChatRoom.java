package com.example.auctionapp.domain.chat.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoom extends AppCompatActivity {

    private RecyclerView chat_recyclerView;
    private ImageView sendbutton;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> chatList;
    private EditText editText;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        editText = findViewById(R.id.editText);
        sendbutton = findViewById(R.id.sendbutton);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();
                Log.d("Test Message", message);
                databaseReference.child("User1/message").setValue(message);
            }
        });

        chat_recyclerView = findViewById(R.id.chattingRecyclerView); // 리싸이클러뷰 연결
        chat_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chat_recyclerView.setLayoutManager(layoutManager);
        chatList = new ArrayList<>();

        database = FirebaseDatabase.getInstance("https://auctionapp-f3805-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = database.getReference("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                // firebase 데이터베이스의 데이터를 받아 오는 곳
                chatList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ // 반복문으로 데이터 리스트 추출
                    User user = snapshot.getValue(User.class); // User객체에 데이터 담기
                    chatList.add(user); // 담은 데이터들을 배열 리스트에 넣고 RecyclerView에 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 배열 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                // DB 가져오던 중 에러 발생 시
                Log.e("FireBase에러", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new ChattingAdatper(chatList,this);
        chat_recyclerView.setAdapter(adapter);


//        adapter = new ChattingAdatper(ChatRoom.this, chatList);
//        chat_recyclerView.setAdapter(adapter);

//        chatList.add(new User("profile","name1","안녕하세요^^"));
//        chatList.add(new User("profile","user","3333-10-1234567 카카오뱅크로 낙찰가 100000원 보내주세요~"));
//        chatList.add(new User("profile","name1","네! 보냈습니다."));
//        chatList.add(new User("profile","user","확인했습니다. 경매상품 안전하게 보내드릴게요~"));
//        chatList.add(new User("profile","user","aaa"));
//        chatList.add(new User("profile","user","aaa"));
//        chatList.add(new User("profile","user","aaa"));




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

class ChattingAdatper extends RecyclerView.Adapter<ChattingAdatper.ChattingViewHolder>{

    private ArrayList<User> arrayList;
    private Context context;
    LayoutInflater mLayoutInflater = null;

    public ChattingAdatper(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ChattingAdatper.ChattingViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_line, parent, false);
//        if(arrayList.get(1).getName().equals("User1")) {
//            view = mLayoutInflater.inflate(R.layout.custom_chatting_mybox, null);
//        }else {
//            view = mLayoutInflater.inflate(R.layout.activity_chat_line, null);
//        }
        ChattingViewHolder holder = new ChattingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChattingAdatper.ChattingViewHolder holder, int position) {
//
//        if(arrayList.get(position).getName().equals("User1")) {
//            view = mLayoutInflater.inflate(R.layout.custom_chatting_mybox, null);
//        }else {
//            view = mLayoutInflater.inflate(R.layout.activity_chat_line, null);
//        }
//        Glide.with(holder.itemView)
//                .load(arrayList.get(position).getProfile())
//                .into(holder.iv_profile);
//        holder.tv_name.setText(arrayList.get(position).getName());
//        holder.tv_message.setText(arrayList.get(position).getMessage());

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_message.setText(arrayList.get(position).getMessage());

    }

    @Override
    public int getItemCount() { return (arrayList != null ? arrayList.size() : 0); }

    public class ChattingViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_profile;
        TextView tv_name;
        TextView tv_message;

        public ChattingViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_message = itemView.findViewById(R.id.tv_message);

        }
    }
//    public View getView(int position, View converView, ViewGroup parent) {
//        View view;
//        // usernam에 따라 위치조정 //this.name     //임시
//        if(arrayList.get(position).getName().equals("User1")) {
//            view = mLayoutInflater.inflate(R.layout.custom_chatting_mybox, null);
//        }else {
//            view = mLayoutInflater.inflate(R.layout.activity_chat_line, null);
//        }
//
//
//        userProfile = (ImageView) view.findViewById(R.id.iv_profile);
//        Glide.with(view)
//                .load(arrayList.get(position).getProfile())
//                .into(userProfile);
//        userName = (TextView) view.findViewById(R.id.tv_name);
//        userName.setText(data.get(position).getName());
//        chatContent = (TextView) view.findViewById(R.id.tv_message);
//        chatContent.setText(data.get(position).getMessage());
//
//        return view;
//    }
}
//
//class ChattingAdapter extends BaseAdapter {
//    Context mContext = null;
//    LayoutInflater mLayoutInflater = null;
//    private ArrayList<User> data;
//    private ImageView userProfile;
//    private TextView userName;
//    private TextView chatContent;
//
//
//    public ChattingAdapter() {}
//    public ChattingAdapter(Context context, ArrayList<User> dataArray) {
//        mContext = context;
//        data = dataArray;
//        mLayoutInflater = LayoutInflater.from(mContext);
//    }
//
//
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public User getItem(int position) {
//        return data.get(position);
//    }
//
//
//    public View getView(int position, View converView, ViewGroup parent) {
//        View view;
//        // usernam에 따라 위치조정 //this.name     //임시
//        if(data.get(position).getName().equals("user")) {
//            view = mLayoutInflater.inflate(R.layout.custom_chatting_mybox, null);
//        }else {
//            view = mLayoutInflater.inflate(R.layout.custom_chatting_otherbox, null);
//        }
//
//
//        userProfile = (ImageView) view.findViewById(R.id.iv_profile);
//        Glide.with(view)
//                .load(data.get(position).getProfile())
//                .into(userProfile);
//        userName = (TextView) view.findViewById(R.id.tv_name);
//        userName.setText(data.get(position).getName());
//        chatContent = (TextView) view.findViewById(R.id.tv_message);
//        chatContent.setText(data.get(position).getMessage());
//
//        return view;
//    }
//
//}
