package com.example.auctionapp.domain.chat.view;

import android.content.Context;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.user.constant.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoom extends AppCompatActivity {

    private ListView chat_listView;
    private ImageView sendbutton;
    private ChattingAdapter adapter;
    private ArrayList<User> chatList;
    private EditText editText;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //uid
    String myuid = String.valueOf(Constants.userId);
    String destUid = "상대방";
    String chatRoomUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.bringToFront();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        chatList = new ArrayList<>();
        chat_listView = findViewById(R.id.chattingListView);
//        adapter = new ChattingAdapter(this, chatList);
//        chat_listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance("https://auctionapp-f3805-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = database.getReference();

        editText = findViewById(R.id.editText);
        sendbutton = findViewById(R.id.sendbutton);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
//                chatList.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    User user = snapshot.getValue(User.class);
//                    System.out.println(user.toString());
//                    chatList.add(user);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
//                Log.e("FireBase에러", String.valueOf(databaseError.toException()));
//            }
//        });

    }
    private void sendMsg() {
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(myuid,true);
                chatModel.users.put(destUid,true);
                //push() 데이터가 쌓이기 위해 채팅방 key가 생성
                if(chatRoomUid == null){
                    Toast.makeText(getApplicationContext(), "채팅방 생성", Toast.LENGTH_SHORT).show();
                    sendbutton.setEnabled(false);
                    databaseReference.child("chatrooms").push().setValue(chatModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    checkChatRoom();
                                }
                            });
                }else{
                    sendMsgToDataBase();
                }
            }
        });
    }
    private void checkChatRoom() {
        //자신 key == true 일때 chatModel 가져온다.
        // /* chatModel public Map<String,Boolean> users = new HashMap<>();
        // 채팅방 유저 public Map<String, ChatModel.Comment> comments = new HashMap<>();
        // 채팅 메시지 */
        databaseReference.child("chatrooms").orderByChild("users/"+myuid).
                equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                //나, 상대방 id 가져온다.
                {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destUid)){
                        //상대방 id 포함돼 있을때 채팅방 key 가져옴
                        chatRoomUid = dataSnapshot.getKey();
                        sendbutton.setEnabled(true);
                        //동기화
                        adapter = new ChattingAdapter(getApplicationContext(), chatList);
                        chat_listView.setAdapter(adapter);
                        //메시지 보내기
                        sendMsgToDataBase();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    //작성한 메시지를 데이터베이스에 보낸다.
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMsgToDataBase() {
        if(!editText.getText().toString().equals("")) {
            LocalDateTime time = LocalDateTime.now();
            //Current date and time: 2019-03-23T00:05:54.608
            ChatModel.Comment comment = new ChatModel.Comment();
            comment.uid = myuid;
            comment.message = editText.getText().toString();
            comment.timestamp = String.valueOf(time);
            databaseReference.child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override public void onSuccess(Void aVoid) {
                            editText.setText("");
                        }
                    });
        }
    }
}

class User
{
    public String name;
    public String profileImgUrl;
    public String uid;
    public String pushToken;
}
    class ChatModel
{
    public Map<String,Boolean> users = new HashMap<>(); //채팅방 유저
    public Map<String,Comment> comments = new HashMap<>(); //채팅 메시지

    public static class Comment
    {
        public String uid;
        public String message;
        public Object timestamp;
    }
}

//class User {
//    private String profile;
//    private String name;
//    private String message;
//
//    public User(){}
//    public User(String profile, String name, String message) {
//        this.profile = profile;
//        this.name = name;
//        this.message = message;
//    }
//
//    public String getProfile() {
//        return profile;
//    }
//
//    public void setProfile(String profile) {
//        this.profile = profile;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//}


class ChattingAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<User> data;
    private ImageView userProfile;
    private TextView userName;
    private TextView chatContent;

    List<ChatModel.Comment> comments;
    //user
    String myuid = String.valueOf(Constants.userId);
    String destUid = "상대방";
    String chatRoomUid;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public ChattingAdapter() {
        comments = new ArrayList<>();
        getDestUid();
    }
    public ChattingAdapter(Context context, ArrayList<User> dataArray) {
        mContext = context;
        data = dataArray;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    //상대방 uid 하나(single) 읽기
    private void getDestUid() {
        databaseReference.child("users").child(destUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User destUser = snapshot.getValue(User.class);
                        //채팅 내용 읽어들임
                        getMessageList();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
//채팅 내용 읽어들임
    private void getMessageList() {
        FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        comments.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            comments.add(dataSnapshot.getValue(ChatModel.Comment.class));
                        }
                        notifyDataSetChanged();
//                        recyclerView.scrollToPosition(comments.size()-1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    public View getView(int position, View converView, ViewGroup parent) {
        View view;
        // usernam에 따라 위치조정 //this.name     //임시
        if(comments.get(position).uid.equals(myuid)) {
            view = mLayoutInflater.inflate(R.layout.custom_chatting_mybox, null);
        }else {
            view = mLayoutInflater.inflate(R.layout.custom_chatting_otherbox, null);
        }

        userProfile = (ImageView) view.findViewById(R.id.iv_profile);
        Glide.with(view)
                .load(data.get(position).profileImgUrl)
                .into(userProfile);
        userName = (TextView) view.findViewById(R.id.tv_name);
        userName.setText(data.get(position).name);
        chatContent = (TextView) view.findViewById(R.id.tv_message);
        chatContent.setText(comments.get(position).message);

        return view;
    }
}

