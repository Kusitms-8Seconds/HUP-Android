package com.me.hurryuphup.domain.chat.presenter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.me.hurryuphup.databinding.ActivityChatRoomBinding;
import com.me.hurryuphup.domain.chat.adapter.ChattingViewAdapter;
import com.me.hurryuphup.domain.chat.constant.ChatConstants;
import com.me.hurryuphup.domain.chat.dto.IsEnterChatRoomRequest;
import com.me.hurryuphup.domain.chat.dto.IsEnterChatRoomResponse;
import com.me.hurryuphup.domain.chat.view.ChatMessageView;
import com.me.hurryuphup.domain.item.dto.ItemDetailsResponse;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;
import com.me.hurryuphup.global.stomp.ChatMessageStomp;
import com.me.hurryuphup.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;

import lombok.SneakyThrows;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.INPUT_METHOD_SERVICE;

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
    ErrorMessageParser errorMessageParser;

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

        checkChatRoom();

        chatMessageStomp = new ChatMessageStomp();
        chatMessageStomp.initStomp(adapter, chatRoomUid, chatMessageView);
        //채팅방 입장여부 확인
        IsEnterChatRoomRequest isEnterChatRoomRequest = new IsEnterChatRoomRequest(chatRoomUid, Constants.userId);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).isChatRoomEntered(isEnterChatRoomRequest)
                .enqueue(MainRetrofitTool.getCallback(new getIfChatRoomEnteredCallback()));
        mBinding.chattingRecyclerView.scrollToPosition(adapter.getItemCount()-1);

        mBinding.sendbutton.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String message = mBinding.editText.getText().toString();
                if(!message.equals("")) {
                    chatMessageStomp.pubSendMessage(chatRoomUid, Constants.userId, message);
                    adapter.notifyDataSetChanged();
                    mBinding.editText.setText("");
                }
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mBinding.editText.getWindowToken(), 0);
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
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            Log.d(TAG, ChatConstants.EChatCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            mBinding.chattingItemDetailPrice.setText(ChatConstants.EChatCallback.rtConnectionFail.getText());
            Log.e(ChatConstants.EChatCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    public class getIfChatRoomEnteredCallback implements MainRetrofitCallback<IsEnterChatRoomResponse> {
        @Override
        public void onSuccessResponse(Response<IsEnterChatRoomResponse> response) throws JSONException {
            Boolean isEnter = response.body().isEnter();
            if(!isEnter) chatMessageStomp.pubEnterMessage(chatRoomUid, Constants.userId);
            Log.d(TAG, ChatConstants.EChatCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<IsEnterChatRoomResponse> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            Log.d(TAG, ChatConstants.EChatCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            mBinding.chattingItemDetailPrice.setText(ChatConstants.EChatCallback.rtConnectionFail.getText());
            Log.e(ChatConstants.EChatCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}

