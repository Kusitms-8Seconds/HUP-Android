package com.example.auctionapp.domain.chat.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.item.view.ItemDetailViewPagerAdapter;
import com.example.auctionapp.domain.user.constant.Constants;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Chat extends Fragment {

    ViewGroup viewGroup;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //uid
    String myuid = String.valueOf(Constants.userId);
    String chatRoomUid;
    Long itemId;
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
        getChatList();

        chattingRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                chatListData destUser = (chatListData) adapterView.getItemAtPosition(position);
                String destUid = destUser.getProfileName();
                Long destItemId = destUser.getItemId();
                Intent intent = new Intent(getContext(), ChatRoom.class);
                intent.putExtra("destUid", destUid);
                intent.putExtra("itemId", destItemId);
                startActivity(intent);
            }
        });

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void getChatList() {
        databaseReference.child("chatrooms").orderByChild("users/"+myuid).equalTo(true).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatroomList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    chatRoomUid = dataSnapshot.getKey();
                    itemId = dataSnapshot.child("itemId/itemId").getValue(Long.class);
                    String temp = chatModel.users.toString();
                    String [] array = temp.split("=true");
                    for(int i=0; i<array.length; i++) {
                        // 내가 들어가 있는 채팅방의 상대방유저 id 가져오기
                        String usersIdStr = array[i];
                        if(usersIdStr.contains(myuid)) continue;
                        usersIdStr = usersIdStr.replace("{","");
                        usersIdStr = usersIdStr.replace("}","");
                        usersIdStr = usersIdStr.replace(",","");
                        usersIdStr = usersIdStr.replace(" ","");
                        if(!usersIdStr.equals(myuid) && !usersIdStr.equals("")) {
                            setChatList(chatRoomUid, usersIdStr, itemId);
                        }
                    }

                }
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void setChatList(String chatRoomUid, String oppId, Long itemIdL) {
        databaseReference.child("chatrooms/" + chatRoomUid + "/comments").addValueEventListener(new ValueEventListener() {
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
                chatroomList.add(new chatListData(itemIdL, oppId, lastChatTime, lastChat));
                chatListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
class chatListData {
    private Long itemId;
    private String profileName;
    private String chatTime;
    private String lastChat;

    public chatListData(){ }

    public chatListData(Long itemId, String profileName, String chatTime, String lastChat){
        this.itemId = itemId;
        this.profileName = profileName;
        this.chatTime = chatTime;
        this.lastChat = lastChat;
    }
    public Long getItemId() {
        return this.itemId;
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
        itemImageImageView.setClipToOutline(true);
//        Long itemIdL = data.get(position).getItemId();
        // 상품 이미지 load  //임시
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(Long.valueOf(7))
                .enqueue(MainRetrofitTool.getCallback(new chatListAdapter.getItemDetailsCallback()));
        profileNameTextView = (TextView) view.findViewById(R.id.tv_chatlist_profileName);
        profileNameTextView.setText(data.get(position).getProfileName());
        chatTimeTextView = (TextView) view.findViewById(R.id.tv_chatlist_lastChatTime);
        chatTimeTextView.setText(data.get(position).getChatTime());
        lastChatTextView = (TextView) view.findViewById(R.id.tv_chatlist_lastChat);
        lastChatTextView.setText(data.get(position).getLastChat());

        return view;
    }
    class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            if(response.body().getFileNames().size()!=0){
                String fileThumbNail = response.body().getFileNames().get(0);
                Glide.with(itemImageImageView.getContext()).load(Constants.imageBaseUrl+fileThumbNail).into(itemImageImageView);
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            itemImageImageView.setImageResource(R.drawable.baby);
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            itemImageImageView.setImageResource(R.drawable.baby);
            Log.e("연결실패", t.getMessage());
        }
    }
}