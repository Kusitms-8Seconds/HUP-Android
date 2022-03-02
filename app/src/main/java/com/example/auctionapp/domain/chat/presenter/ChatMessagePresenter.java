package com.example.auctionapp.domain.chat.presenter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.auctionapp.databinding.ActivityChatRoomBinding;
import com.example.auctionapp.domain.chat.adapter.ChattingViewAdapter;
import com.example.auctionapp.domain.chat.constant.ChatConstants;
import com.example.auctionapp.domain.chat.dto.ChatMessageResponse;
import com.example.auctionapp.domain.chat.model.ChatModel;
import com.example.auctionapp.domain.chat.model.User;
import com.example.auctionapp.domain.chat.view.ChatMessageView;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.stomp.ChatMessageStomp;
import com.example.auctionapp.global.util.ErrorMessageParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;

import lombok.SneakyThrows;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ChatMessagePresenter implements ChatMessagePresenterInterface {
    //uid
    private Long chatRoomUid; //채팅방 하나 id
    private Long myuid;       //나의 id
    private Long destUid;     //상대방 uid
    private Long itemId;

    ChattingViewAdapter adapter;
    //stomp
    ChatMessageStomp chatMessageStomp;

    // Attributes
    private ChatMessageView chatMessageView;
    private ActivityChatRoomBinding mBinding;
    private Context context;

    // Constructor
    public ChatMessagePresenter(ChatMessageView chatMessageView, ActivityChatRoomBinding mBinding, Context getApplicationContext){
        this.chatMessageView = chatMessageView;
        this.mBinding = mBinding;
        this.context = getApplicationContext;
    }

    @Override
    public void init(Long chatRoomId, Long destuid, Long EndItemId) throws IOException, JSONException {
        chatRoomUid = chatRoomId;
        myuid = Constants.userId;
        destUid = destuid;
        itemId = EndItemId;

        mBinding.chattingItemImage.setClipToOutline(true);
        adapter = new ChattingViewAdapter(chatMessageView, mBinding, context, chatRoomUid, myuid, destUid);
        adapter.notifyDataSetChanged();

        //상품 정보 가져오기
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getItemDetailsCallback()));

        if (mBinding.editText.getText().toString() == null) mBinding.sendbutton.setEnabled(false);
        else mBinding.sendbutton.setEnabled(true);

        checkChatRoom();
        chatMessageStomp = new ChatMessageStomp();
        chatMessageStomp.initStomp(adapter, chatRoomUid, chatMessageView);
        mBinding.chattingRecyclerView.scrollToPosition(adapter.getItemCount()-1);

        mBinding.sendbutton.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String message = mBinding.editText.getText().toString();
                chatMessageStomp.pubSendMessage(message);
                //refresh
                init(chatRoomUid, destUid, itemId);
//                adapter = new ChattingViewAdapter(chatMessageView, mBinding, context, chatRoomUid, myuid, destUid);
//                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void checkChatRoom() {
        //동기화
        mBinding.chattingRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mBinding.chattingRecyclerView.setAdapter(adapter);
        mBinding.chattingRecyclerView.scrollToPosition(adapter.getItemCount()-1);
    }

    public class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            mBinding.chattingItemDetailName.setText(response.body().getItemName());
            if(response.body().getFileNames().size()!=0){
                String fileThumbNail = response.body().getFileNames().get(0);
                Glide.with(context).load(Constants.imageBaseUrl+fileThumbNail).into(mBinding.chattingItemImage);
            }
            mBinding.chattingItemDetailCategory.setText(response.body().getCategory().getName());
            mBinding.chattingItemDetailPrice.setText(String.valueOf(response.body().getSoldPrice()));    //낙찰가 출력
            Log.d(TAG, ChatConstants.EChatCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            chatMessageView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, ChatConstants.EChatCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            mBinding.chattingItemDetailPrice.setText(ChatConstants.EChatCallback.rtConnectionFail.getText());
            Log.e(ChatConstants.EChatCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}

