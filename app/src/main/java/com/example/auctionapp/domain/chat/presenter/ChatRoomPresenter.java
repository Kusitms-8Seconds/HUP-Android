package com.example.auctionapp.domain.chat.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.auctionapp.databinding.ActivityChatBinding;
import com.example.auctionapp.domain.chat.adapter.chatListAdapter;
import com.example.auctionapp.domain.chat.dto.ChatRoomResponse;
import com.example.auctionapp.domain.chat.dto.DeleteChatRoomRequest;
import com.example.auctionapp.domain.chat.model.chatListData;
import com.example.auctionapp.domain.chat.view.ChatRoomView;
import com.example.auctionapp.domain.home.constant.HomeConstants;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.stomp.ChatMessageStomp;
import com.example.auctionapp.global.util.ErrorMessageParser;
import com.example.auctionapp.global.util.GetTime;

import org.json.JSONException;

import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import retrofit2.Response;

public class ChatRoomPresenter implements ChatRoomPresenterInterface {
    //uid
    String myuid;
    String chatRoomUid;
    Long itemId;
    //chatting room list
    ArrayList<chatListData> chatroomList = new ArrayList<chatListData>();
    com.example.auctionapp.domain.chat.adapter.chatListAdapter chatListAdapter;
    //stomp
    ChatMessageStomp chatMessageStomp;

    // Attributes
    private ChatRoomView chatRoomView;
    private ActivityChatBinding mBinding;
    private Context context;

    // Constructor
    public ChatRoomPresenter(ChatRoomView chatRoomView, ActivityChatBinding mBinding, Context getApplicationContext){
        this.chatRoomView = chatRoomView;
        this.mBinding = mBinding;
        this.context = getApplicationContext;
    }

    @Override
    public void init() {
        chatListAdapter = new chatListAdapter(context, chatroomList);
        mBinding.chattingRoomListView.setAdapter(chatListAdapter);

        myuid = String.valueOf(Constants.userId);
        getChatList();

        chatMessageStomp = new ChatMessageStomp();
        mBinding.chattingRoomListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("채팅방 퇴장").setMessage("퇴장하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                    @SneakyThrows
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO : 채팅방 퇴장
                        chatListData data = (chatListData) parent.getItemAtPosition(position);
                        DeleteChatRoomRequest deleteChatRoomRequest = new DeleteChatRoomRequest(data.getChatroomId(), Constants.userId);
                        chatMessageStomp.initStomp(deleteChatRoomRequest);
                        chatMessageStomp.outChatRoom(deleteChatRoomRequest);
//                        init();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public void getChatList() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getChatRooms(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getChatRoomsCallback()));
    }

    private class getChatRoomsCallback implements MainRetrofitCallback<List<ChatRoomResponse>> {

        @Override
        public void onSuccessResponse(Response<List<ChatRoomResponse>>response) throws IOException {
            for(int i=0; i<response.body().size(); i++) {
                Long chatroomId = response.body().get(i).getId();
                Long destId = response.body().get(i).getUserId();
                String userName = response.body().get(i).getUserName();
                Long itemId = response.body().get(i).getItemId();
                String itemUrl = "";
                if (!response.body().get(i).getFileNames().isEmpty())
                    itemUrl = response.body().get(i).getFileNames().get(0);
                String latestMessage = response.body().get(i).getLatestMessage();

                Month month = response.body().get(i).getLatestTime().getMonth();
                String day = String.valueOf(response.body().get(i).getLatestTime().getDayOfMonth());
                String hour = String.valueOf(response.body().get(i).getLatestTime().getHour());
                String minute = String.valueOf(response.body().get(i).getLatestTime().getMinute());
                GetTime getChatTime = new GetTime(month, day, hour, minute);
                String latestTime = getChatTime.getLatestTime();

                chatroomList.add(new chatListData(chatroomId, destId, userName, itemId, itemUrl, latestMessage, latestTime));
                chatListAdapter.notifyDataSetChanged();
            }

            Log.d("getChatRooms", "onSuccess");
        }
        @Override
        public void onFailResponse(Response<List<ChatRoomResponse>> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            chatRoomView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d("getChatRooms", HomeConstants.EHomeCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(HomeConstants.EHomeCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
