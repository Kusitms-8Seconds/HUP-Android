package com.example.auctionapp.domain.chat.view;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.user.constant.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoom extends AppCompatActivity {
    private String chatRoomUid; //채팅방 하나 id
    private String myuid;       //나의 id
    private String destUid;     //상대방 uid

    private RecyclerView recyclerView;
    private ImageView button;
    private EditText editText;


    private User destUser;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy.MM.dd HH:mm");

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        init();
        sendMsg();
    }

    private void init()
    {
        database = FirebaseDatabase.getInstance("https://auctionapp-f3805-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = database.getReference();

        myuid = "판매자";
//        destUid = "구매자1";        //채팅 상대 //임시
        Intent intent = getIntent();
        destUid = intent.getStringExtra("destUid");

        recyclerView = (RecyclerView)findViewById(R.id.chattingRecyclerView);
        button=(ImageView)findViewById(R.id.sendbutton);
        editText = (EditText)findViewById(R.id.editText);

        if(editText.getText().toString() == null) button.setEnabled(false);
        else button.setEnabled(true);

        checkChatRoom();
    }

    private void sendMsg()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(myuid,true);
                chatModel.users.put(destUid,true);

                //push() 데이터가 쌓이기 위해 채팅방 key가 생성
                if(chatRoomUid == null){
                    Toast.makeText(ChatRoom.this, "채팅방 생성", Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                    databaseReference.child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    //작성한 메시지를 데이터베이스에 보낸다.
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMsgToDataBase()
    {
        if(!editText.getText().toString().equals(""))
        {
            LocalDateTime currentDate = LocalDateTime.now();
            ChatModel.Comment comment = new ChatModel.Comment();
            comment.uid = myuid;
            comment.message = editText.getText().toString();
            comment.timestamp = String.valueOf(currentDate);
            databaseReference.child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editText.setText("");
                }
            });
        }
    }

    private void checkChatRoom()
    {
        //자신 key == true 일때 chatModel 가져온다.
        /* chatModel
        public Map<String,Boolean> users = new HashMap<>(); //채팅방 유저
        public Map<String, ChatModel.Comment> comments = new HashMap<>(); //채팅 메시지
        */
        databaseReference.child("chatrooms").orderByChild("users/"+myuid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) //나, 상대방 id 가져온다.
                {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destUid)){           //상대방 id 포함돼 있을때 채팅방 key 가져옴
                        chatRoomUid = dataSnapshot.getKey();
                        button.setEnabled(true);

                        //동기화
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatRoom.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());

                        //메시지 보내기
                        sendMsgToDataBase();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //===============채팅 창===============//
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    {
        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter(){
            comments = new ArrayList<>();

            getDestUid();
        }

        //상대방 uid 하나(single) 읽기
        private void getDestUid()
        {
            databaseReference.child("users").child(destUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    destUser = snapshot.getValue(User.class);

                    //채팅 내용 읽어들임
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        //채팅 내용 읽어들임
        private void getMessageList()
        {
            databaseReference.child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        comments.add(dataSnapshot.getValue(ChatModel.Comment.class));
                    }
                    notifyDataSetChanged();

                    recyclerView.scrollToPosition(comments.size()-1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chatting_mybox,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            ViewHolder viewHolder = ((ViewHolder)holder);

            if(comments.get(position).uid.equals(myuid)) //나의 uid 이면
            {
                //나의 말풍선 오른쪽으로
                viewHolder.textViewMsg.setText(comments.get(position).message);
                viewHolder.textViewMsg.setBackgroundResource(R.drawable.chatbox_user);
//                viewHolder.linearLayoutDest.setVisibility(View.INVISIBLE);        //상대방 레이아웃
//                viewHolder.linearLayoutRoot.setGravity(Gravity.RIGHT);
//                viewHolder.linearLayoutTime.setGravity(Gravity.RIGHT);
            }else{
                //상대방 말풍선 왼쪽
//                Glide.with(holder.itemView.getContext())
//                        .load(destUser.profileImgUrl)
//                        .apply(new RequestOptions().circleCrop())
//                        .into(holder.imageViewProfile);
//                viewHolder.textViewName.setText(destUser.name);
//                viewHolder.linearLayoutDest.setVisibility(View.VISIBLE);
                viewHolder.textViewMsg.setBackgroundResource(R.drawable.chatbox_others);
                viewHolder.textViewMsg.setText(comments.get(position).message);
                viewHolder.chat_line.setGravity(Gravity.LEFT);
//                viewHolder.linearLayoutTime.setGravity(Gravity.LEFT);
            }
            viewHolder.textViewTimeStamp.setText(comments.get(position).timestamp+"");

        }

//        public String getDateTime(int position)
//        {
//            long unixTime=(long) comments.get(position).timestamp;
//            Date date = new Date(unixTime);
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//            String time = simpleDateFormat.format(date);
//            return time;
//        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView textViewMsg;   //메시지 내용
            public TextView textViewName;
            public TextView textViewTimeStamp;
            public ImageView imageViewProfile;
            public LinearLayout chat_line;
//            public LinearLayout linearLayoutRoot;
//            public LinearLayout linearLayoutTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewMsg = (TextView)itemView.findViewById(R.id.tv_message);
                textViewName = (TextView)itemView.findViewById(R.id.tv_name);
                textViewTimeStamp = (TextView)itemView.findViewById(R.id.tv_time);
                imageViewProfile = (ImageView)itemView.findViewById(R.id.iv_profile);
                chat_line = (LinearLayout)itemView.findViewById(R.id.chat_line);
//                linearLayoutRoot = (LinearLayout)itemView.findViewById(R.id.item_messagebox_root)
//                ;
//                linearLayoutTime = (LinearLayout)itemView.findViewById(R.id.item_messagebox_layout_timestamp);
            }
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