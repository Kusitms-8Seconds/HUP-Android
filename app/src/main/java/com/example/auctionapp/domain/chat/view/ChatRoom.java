package com.example.auctionapp.domain.chat.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.home.view.Mypage;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.item.view.ItemDetailViewPagerAdapter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ChatRoom extends AppCompatActivity {
    //uid
    private String chatRoomUid; //채팅방 하나 id
    private String myuid;       //나의 id
    private String destUid;     //상대방 uid
    private User destUser;
    String profileUrlStr;
    private Long EndItemId;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private ImageView button;   //보내기 버튼
    private EditText editText;  //메세지 작성
    private ImageView chattingItemImage;
    private TextView chattingItemDetailName;
    private TextView chattingItemDetailCategory;
    private TextView chattingItemDetailPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        init();
        sendMsg();
    }

    private void init() {
        database = FirebaseDatabase.getInstance("https://auctionapp-f3805-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = database.getReference();

        myuid = String.valueOf(Constants.userId);
        destUid = "상대방";
//        Intent intent = getIntent();
//        destUid = intent.getStringExtra("destUid");
//        EndItemId = intent.getLongExtra("itemId", 0);

        recyclerView = (RecyclerView) findViewById(R.id.chattingRecyclerView);
        button = (ImageView) findViewById(R.id.sendbutton);
        editText = (EditText) findViewById(R.id.editText);
        chattingItemImage = (ImageView) findViewById(R.id.chattingItemImage);
        chattingItemDetailName = (TextView) findViewById(R.id.chattingItemDetailName);
        chattingItemDetailCategory = (TextView) findViewById(R.id.chattingItemDetailCategory);
        chattingItemDetailPrice = (TextView) findViewById(R.id.chattingItemDetailPrice);
        chattingItemImage.setClipToOutline(true);

        //상품 정보 가져오기
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(Long.valueOf(7))
                .enqueue(MainRetrofitTool.getCallback(new ChatRoom.getItemDetailsCallback()));

        if (editText.getText().toString() == null) button.setEnabled(false);
        else button.setEnabled(true);

        checkChatRoom();
    }

    private void sendMsg() {
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(myuid, true);
                chatModel.users.put(destUid, true);
                chatModel.itemId.put("itemId", Long.valueOf(7));    //상품 id ?

                //push() 데이터가 쌓이기 위해 채팅방 생성_미완
                if (chatRoomUid == null) {
                    button.setEnabled(false);
                    databaseReference.child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
//                    insertUserInfo(Long.valueOf(myuid));
//                    insertUserInfo(Long.valueOf(destUid));
//                    databaseReference.child("User").push().child("uid").setValue(myuid);
//                    databaseReference.child("User").push().child("uid").setValue(destUid);
                } else {
                    sendMsgToDataBase();
                }
            }
        });
    }

    public void insertUserInfo(Long chatUserId) {

        UserDetailsInfoRequest userDetailsInfoRequest = UserDetailsInfoRequest.of(chatUserId);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(userDetailsInfoRequest)
                .enqueue(MainRetrofitTool.getCallback(new ChatRoom.UserDetailsInfoCallback()));

    }

    //작성한 메시지를 데이터베이스에 보낸다.
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMsgToDataBase() {
        if (!editText.getText().toString().equals("")) { // 이 안에서 한 번 더 나인지 상대방인지 체크해 주는게 필요할 듯
            // comment.uid 값이 myuid 여야 하는지, destuid여야 하는지 체크해야 함..
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

    private void checkChatRoom() {
        //자신 key == true 일때 chatModel 가져온다.
        /* chatModel
        public Map<String,Boolean> users = new HashMap<>(); //채팅방 유저
        public Map<String, ChatModel.Comment> comments = new HashMap<>(); //채팅 메시지
        */
        databaseReference.child("chatrooms").orderByChild("users/" + myuid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) //나, 상대방 id 가져온다.
                {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    if (chatModel.users.containsKey(destUid)) {           //상대방 id 포함돼 있을때 채팅방 key 가져옴
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
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            getDestUid();
        }

        //상대방 uid 하나(single) 읽기
        private void getDestUid() {
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
        private void getMessageList() {
            databaseReference.child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ChatModel.Comment commentUser = dataSnapshot.getValue(ChatModel.Comment.class);
                        String messageStr = commentUser.getMessage();
                        String timestampStr = commentUser.getTimestamp();
                        String UidStr = commentUser.getUid();
                        if (UidStr.equals(myuid)) {
                            comments.add(new ChatModel.Comment(UidStr, messageStr, timestampStr, 1));
                        } else {
                            comments.add(new ChatModel.Comment(UidStr, messageStr, timestampStr, 0));
                        }
                    }
                    notifyDataSetChanged();
                    recyclerView.scrollToPosition(comments.size() - 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (viewType == Code.ViewType.CENTER_CONTENT) {
//                view = inflater.inflate(R.layout.center_content, parent, false);
//                return new CenterViewHolder(view);
            } else if (viewType == Code.ViewType.LEFT_CONTENT) {
                view = inflater.inflate(R.layout.custom_chatting_otherbox, parent, false);
                return new LeftViewHolder(view);
            } else {
                view = inflater.inflate(R.layout.custom_chatting_mybox, parent, false);
                return new RightViewHolder(view);
            }
            return new RightViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof CenterViewHolder) {
//                ((CenterViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
            } else if (viewHolder instanceof LeftViewHolder) {
                ((LeftViewHolder) viewHolder).name.setText(comments.get(position).getUid());
                ((LeftViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
                String chatTimeStr = comments.get(position).getTimestamp();
                String [] array = chatTimeStr.split("-");
                String month = array[1];
                String [] array2 = array[2].split("T");
                String [] array3 = array2[1].split(":");
                String chatTime = month + "월 " + array2[0] + "일 " + array3[0] + ":" + array3[1];
                ((LeftViewHolder) viewHolder).chat_time.setText(chatTime);
                ((LeftViewHolder) viewHolder).chat_time.setText(chatTime);
                databaseReference.child("User").orderByChild("name").equalTo(destUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            profileUrlStr = dataSnapshot.child("profile").getValue(String.class);
                        }
                        Glide.with(getApplicationContext()).load(profileUrlStr).into(((LeftViewHolder) viewHolder).image);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            } else {
//                ((RightViewHolder) viewHolder).name.setText(comments.get(position).getUid());
                ((RightViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
                String chatTimeStr = comments.get(position).getTimestamp();
                String [] array = chatTimeStr.split("-");
                String month = array[1];
                String [] array2 = array[2].split("T");
                String [] array3 = array2[1].split(":");
                String chatTime = month + "월 " + array2[0] + "일 " + array3[0] + ":" + array3[1];
                ((RightViewHolder) viewHolder).chat_time.setText(chatTime);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return comments.get(position).getViewType();
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        public class CenterViewHolder extends RecyclerView.ViewHolder {
            TextView content;

            CenterViewHolder(View itemView) {
                super(itemView);
                content = itemView.findViewById(R.id.content);
            }
        }

        public class LeftViewHolder extends RecyclerView.ViewHolder {
            TextView content;
            TextView name;
            TextView chat_time;
            ImageView image;

            LeftViewHolder(View itemView) {
                super(itemView);

                content = itemView.findViewById(R.id.tv_message);
                name = itemView.findViewById(R.id.tv_name);
                chat_time = itemView.findViewById(R.id.tv_time);
                image = itemView.findViewById(R.id.iv_profile);
            }
        }

        public class RightViewHolder extends RecyclerView.ViewHolder {
            TextView content;
            TextView chat_time;

            RightViewHolder(View itemView) {
                super(itemView);

                content = itemView.findViewById(R.id.tv_message);
                chat_time = itemView.findViewById(R.id.tv_time);
            }
        }

        class Code {
            public class ViewType {
                public static final int LEFT_CONTENT = 0;
                public static final int RIGHT_CONTENT = 1;
                public static final int CENTER_CONTENT = 2;
            }
        }
    }
    public class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            chattingItemDetailName.setText(response.body().getItemName());
            if(response.body().getFileNames().size()!=0){
                String fileThumbNail = response.body().getFileNames().get(0);
                Glide.with(getApplicationContext()).load(Constants.imageBaseUrl+fileThumbNail).into(chattingItemImage);
            }
            chattingItemDetailCategory.setText(response.body().getCategory().getName());
            chattingItemDetailPrice.setText("500000");    //낙찰가 출력(임시)
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            chattingItemDetailPrice.setText("?연결실패?");
            Log.e("연결실패", t.getMessage());
        }
    }
    private class UserDetailsInfoCallback implements MainRetrofitCallback<UserDetailsInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserDetailsInfoResponse> response) {
            String userProfile = response.body().getPicture();
            Long chatUserId = response.body().getUserId();
            String userName = response.body().getUsername();
            User userInfo = new User(userName, userProfile, chatUserId);
            databaseReference.child("User").push().setValue(userInfo);

            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<UserDetailsInfoResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(getApplicationContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
class User {
    public String name;
    public String profileImgUrl;
    public Long uid;

    public User() { }

    public User(String name, String profileImgUrl, Long uid) {
        this.name = name;
        this.profileImgUrl = profileImgUrl;
        this.uid = uid;
    }
}
class ItemId {
    public Long itemId;
//    public Long getItemId() {return itemId;}
}

class ChatModel {
    public Map<String, Boolean> users = new HashMap<>(); //채팅방 유저
    public Map<String, Long> itemId = new HashMap<>(); //채팅 아이템 id

    public static class Comment {
        public String uid;
        public String message;
        public String timestamp;
        public int viewType;

        public Comment() {

        }

        public Comment(String uid, String message, String timestamp, int viewType) {
            this.uid = uid;
            this.message = message;
            this.timestamp = timestamp;
            this.viewType = viewType;
        }

        public String getUid() {
            return uid;
        }
        public String getMessage() {
            return message;
        }
        public String getTimestamp() {
            return timestamp;
        }
        public int getViewType() {
            return viewType;
        }
    }
}