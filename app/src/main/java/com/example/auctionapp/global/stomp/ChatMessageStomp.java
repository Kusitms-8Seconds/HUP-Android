package com.example.auctionapp.global.stomp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.chat.adapter.ChattingViewAdapter;
import com.example.auctionapp.domain.chat.model.ChatModel;
import com.example.auctionapp.domain.chat.view.ChatMessageView;
import com.example.auctionapp.domain.item.model.BidParticipants;
import com.example.auctionapp.domain.item.adapter.PTAdapter;
import com.example.auctionapp.domain.pricesuggestion.constant.PriceConstants;
import com.example.auctionapp.domain.pricesuggestion.presenter.BidPagePresenter;
import com.example.auctionapp.domain.pricesuggestion.view.BidPageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static android.content.ContentValues.TAG;


public class ChatMessageStomp {

    private static final String url = "http://10.0.2.2:8080/websocket/websocket";

    private StompClient stompClient;
    private List<StompHeader> connectHeaderList;
    private List<StompHeader> subHeaderList;
    private List<StompHeader> pubEnterHeaderList;
    private List<StompHeader> pubSendHeaderList;
    private ChattingViewAdapter adapter;

    List<ChatModel.Comment> comments;
    Long chatRoomId;
    Long userId;
    String userName;
    String message;
    String createdDate;

    private ChatMessageView chatMessageView;

    int count;

    @SuppressLint("CheckResult")
    public void initStomp(ChattingViewAdapter adapter, Long chatRoomId, ChatMessageView chatMessageView) throws IOException, JSONException {
        this.adapter = adapter;
        this.chatRoomId = chatRoomId;
        this.chatMessageView = chatMessageView;

        comments = new ArrayList<>();
        connectSTOMP();
        subSTOMP();
        //pubEnterMessage();
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
            System.out.println("element: " + element);
            chatRoomId = Long.valueOf(element.getAsJsonObject().get("id").getAsString());
            userId = Long.valueOf(element.getAsJsonObject().get("userId").getAsString());
            userName = element.getAsJsonObject().get("userName").getAsString();
            message = element.getAsJsonObject().get("message").getAsString();
            createdDate = element.getAsJsonObject().get("createdDate").getAsString();

            Handler mainHandler = new Handler(Looper.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    if(userId == Constants.userId)
                        comments.add(new ChatModel.Comment(userId, message, createdDate, 1));
                    else
                        comments.add(new ChatModel.Comment(userId, message, createdDate, 0));

                    adapter.notifyDataSetChanged();
                } // This is your code
            };
            mainHandler.post(myRunnable);

        });
    }

    public void pubEnterMessage() throws JSONException{
        pubEnterHeaderList = new ArrayList<>();
        pubEnterHeaderList.add(new StompHeader("Authorization", "Bearer " + Constants.accessToken));
        pubEnterHeaderList.add(new StompHeader(StompHeader.DESTINATION, "/pub/chatRoom/enter"));
        JSONObject json = new JSONObject();
        json.put("chatRoomId", chatRoomId);
        json.put("userId", Constants.userId);
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, pubEnterHeaderList, json.toString());
        stompClient.send(stompMessage).subscribe();
    }

    public void pubSendMessage(String message) throws JSONException{
        pubSendHeaderList = new ArrayList<>();
        pubSendHeaderList.add(new StompHeader("Authorization", "Bearer " + Constants.accessToken));
        pubSendHeaderList.add(new StompHeader(StompHeader.DESTINATION, "/pub/chatRoom/send"));
        JSONObject json = new JSONObject();
        json.put("chatRoomId", chatRoomId);
        json.put("userId", Constants.userId);
        json.put("message", message);
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, pubSendHeaderList, json.toString());
        stompClient.send(stompMessage).subscribe();
    }

}