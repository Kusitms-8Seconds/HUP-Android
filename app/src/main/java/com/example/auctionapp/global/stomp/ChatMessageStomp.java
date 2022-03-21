package com.example.auctionapp.global.stomp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.auctionapp.domain.chat.adapter.ChattingViewAdapter;
import com.example.auctionapp.domain.chat.dto.DeleteChatRoomRequest;
import com.example.auctionapp.domain.chat.model.ChatModel;
import com.example.auctionapp.domain.chat.view.ChatMessageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.util.GetTime;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;


public class ChatMessageStomp {

    private static final String url = "http://52.78.175.27:8080/websocket/websocket";

    private StompClient stompClient;
    private List<StompHeader> connectHeaderList;
    private List<StompHeader> subHeaderList;
    private List<StompHeader> pubEnterHeaderList;
    private List<StompHeader> pubSendHeaderList;
    private List<StompHeader> outChatHeaderList;
    private ChattingViewAdapter adapter;

    List<ChatModel.Comment> comments;
    Long chatRoomId;
    Long userId;
    String userName;
    String message;
    String createdDate;

    private ChatMessageView chatMessageView;

    @SuppressLint("CheckResult")
    public void initStomp(ChattingViewAdapter adapter, Long chatRoomId, ChatMessageView chatMessageView) throws IOException, JSONException {
        this.adapter = adapter;
        this.chatRoomId = chatRoomId;
        this.chatMessageView = chatMessageView;

        comments = new ArrayList<>();
        connectSTOMP();
        subSTOMP();
    }
    public void initStomp(DeleteChatRoomRequest deleteChatRoomRequest) throws IOException, JSONException {
//        this.adapter = adapter;
//        this.chatRoomId = deleteChatRoomRequest.getChatRoomId();
//        this.chatMessageView = chatMessageView;

//        comments = new ArrayList<>();
        connectSTOMP();
        subSTOMP();
    }

    public void connectSTOMP(){
        stompClient= Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("tag", "Stomp connection opened");
                    break;
                case ERROR:
                    Log.e("tag", "Error", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.d("tag", "Stomp connection closed");
                    break;
            }
        });
        connectHeaderList=new ArrayList<>();
        connectHeaderList.add(new StompHeader("Authorization", "Bearer "+ Constants.accessToken));
        stompClient.connect(connectHeaderList);
    }

    public void subSTOMP() throws IOException, JSONException {
        subHeaderList=new ArrayList<>();
        subHeaderList.add(new StompHeader("Authorization", "Bearer "+ Constants.accessToken));
        stompClient.topic("/sub/chatRoom/"+chatRoomId, subHeaderList).subscribe(topicMessage -> {
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(topicMessage.getPayload());
            chatRoomId = Long.valueOf(element.getAsJsonObject().get("id").getAsString());
            userId = Long.valueOf(element.getAsJsonObject().get("userId").getAsString());
            userName = element.getAsJsonObject().get("userName").getAsString();
            message = element.getAsJsonObject().get("message").getAsString();
            createdDate = element.getAsJsonObject().get("createdDate").getAsString();
            GetTime getChatTime = new GetTime(createdDate);
            String time = getChatTime.getLatestTime();

            Handler mainHandler = new Handler(Looper.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    if(message.contains("님이 채팅방에 참여하였습니다") || message.contains("님이 채팅방을 퇴장했습니다")) {
                        ChatModel.Comment data = new ChatModel.Comment(userId, message, time, 2);
                        comments.add(data);
                        adapter.addItem(data);
                    }
                    else if(userId.equals(Constants.userId)) {
                        ChatModel.Comment data = new ChatModel.Comment(userId, message, time, 1);
                        comments.add(data);
                        adapter.addItem(data);
                    }
                    else {
                        ChatModel.Comment data = new ChatModel.Comment(userId, message, time, 0);
                        comments.add(data);
                        adapter.addItem(data);
                    }
                    adapter.notifyDataSetChanged();
                } // This is your code
            };
            mainHandler.post(myRunnable);

        });
    }

    public void pubEnterMessage(Long chatRoomId, Long userId) throws JSONException {
        pubEnterHeaderList = new ArrayList<>();
        pubEnterHeaderList.add(new StompHeader("Authorization", "Bearer " + Constants.accessToken));
        pubEnterHeaderList.add(new StompHeader(StompHeader.DESTINATION, "/pub/chatRoom/enter"));
        JSONObject json = new JSONObject();
        json.put("chatRoomId", chatRoomId);
        json.put("userId", userId);
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, pubEnterHeaderList, json.toString());
        stompClient.send(stompMessage).subscribe();
    }
    public void pubSendMessage(Long chatRoomId, Long userId, String message) throws JSONException{
        pubSendHeaderList = new ArrayList<>();
        pubSendHeaderList.add(new StompHeader("Authorization", "Bearer " + Constants.accessToken));
        pubSendHeaderList.add(new StompHeader(StompHeader.DESTINATION, "/pub/chatRoom/send"));
        JSONObject json = new JSONObject();
        json.put("chatRoomId", chatRoomId);
        json.put("userId", userId);
        json.put("message", message);
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, pubSendHeaderList, json.toString());
        stompClient.send(stompMessage).subscribe();
    }
    public void outChatRoom(DeleteChatRoomRequest deleteChatRoomRequest) throws JSONException{
        outChatHeaderList = new ArrayList<>();
        outChatHeaderList.add(new StompHeader("Authorization", "Bearer " + Constants.accessToken));
        outChatHeaderList.add(new StompHeader(StompHeader.DESTINATION, "/pub/chatRoom/out"));
        JSONObject json = new JSONObject();
        json.put("chatRoomId", deleteChatRoomRequest.getChatRoomId());
        json.put("userId", deleteChatRoomRequest.getUserId());
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, outChatHeaderList, json.toString());
        stompClient.send(stompMessage).subscribe();
    }
}