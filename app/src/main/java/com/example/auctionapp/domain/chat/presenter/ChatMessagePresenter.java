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

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ChatMessagePresenter implements ChatMessagePresenterInterface {
    //uid
    private Long chatRoomUid; //채팅방 하나 id
    private Long myuid;       //나의 id
    private Long destUid;     //상대방 uid
    private Long itemId;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

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
    public void init(Long destuid, Long EndItemId) {
        myuid = Constants.userId;
        destUid = destuid;
        itemId = EndItemId;

        mBinding.chattingItemImage.setClipToOutline(true);

        //상품 정보 가져오기
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getItemDetailsCallback()));

        if (mBinding.editText.getText().toString() == null) mBinding.sendbutton.setEnabled(false);
        else mBinding.sendbutton.setEnabled(true);

        checkChatRoom();
    }

    @Override
    public void sendMsg() {
        mBinding.sendbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                sendMsgToDataBase();
            }
        });
    }

    @Override
    public void sendMsgToDataBase() {
        if (!mBinding.editText.getText().toString().equals("")) {
            // comment.uid 값이 myuid 여야 하는지, destuid여야 하는지 체크해야 함..
//            LocalDateTime currentDate = LocalDateTime.now();
//            ChatModel.Comment comment = new ChatModel.Comment();
//            comment.uid = myuid;
//            comment.message = mBinding.editText.getText().toString();
//            comment.timestamp = String.valueOf(currentDate);
//            databaseReference.child(ChatConstants.EChatFirebase.chatrooms.getText()).child(chatRoomUid).child(ChatConstants.EChatFirebase.comments.getText()).push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    mBinding.editText.setText("");
//                }
//            });
        }
    }

    @Override
    public void checkChatRoom() {
//        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getChatMessages(Constants.userId)
//                .enqueue(MainRetrofitTool.getCallback(new getChatMessagesCallback()));
        //동기화
        mBinding.chattingRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mBinding.chattingRecyclerView.setAdapter(new ChattingViewAdapter(chatMessageView, mBinding, context, chatRoomUid, myuid, destUid));
        mBinding.chattingRecyclerView.scrollToPosition(mBinding.chattingRecyclerView.getAdapter().getItemCount()-1);
    }

//    public class getChatMessagesCallback implements MainRetrofitCallback<ChatMessageResponse> {
//        @Override
//        public void onSuccessResponse(Response<ChatMessageResponse> response) {
//
//            Log.d(TAG, ChatConstants.EChatCallback.rtSuccessResponse.getText() + response.body().toString());
//        }
//        @Override
//        public void onFailResponse(Response<ChatMessageResponse> response) throws IOException, JSONException {
//            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
//            chatMessageView.showToast(errorMessageParser.getParsedErrorMessage());
//            Log.d(TAG, ChatConstants.EChatCallback.rtFailResponse.getText());
//        }
//        @Override
//        public void onConnectionFail(Throwable t) {
//            mBinding.chattingItemDetailPrice.setText(ChatConstants.EChatCallback.rtConnectionFail.getText());
//            Log.e(ChatConstants.EChatCallback.rtConnectionFail.getText(), t.getMessage());
//        }
//    }

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

