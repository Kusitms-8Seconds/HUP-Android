package com.example.auctionapp.global.stomp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.pricesuggestion.view.BidParticipants;
import com.example.auctionapp.domain.pricesuggestion.view.PTAdapter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.functions.Consumer;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static android.content.ContentValues.TAG;


public class HupStomp {

//    private static final String url = "ws://192.168.1.2:8080/websocket/websocket"; // 내부IP(안드로이드휴대폰사용, cmd -> ipconfig IPv4주소)
    private static final String url = "http://10.0.2.2:8080/websocket/websocket"; // 안드로이드에뮬레이터IP

    private StompClient stompClient;
    private List<StompHeader> connectHeaderList;
    private List<StompHeader> topicHeaderList;
    private List<StompHeader> sendHeaderList;
    private PTAdapter ptAdapter;
    private ArrayList<BidParticipants> bidParticipants;
    TextView participants;
    TextView highPrice;
    BidParticipants data;

    int count;

    @SuppressLint("CheckResult")
    public void initStomp(PTAdapter adapter, ArrayList<BidParticipants> bidParticipants, TextView highPrice, TextView participants){
        this.ptAdapter = adapter;
        this.bidParticipants = bidParticipants;
        this.participants = participants;
        this.highPrice = highPrice;
        connectSTOMP();
        topicSTOMP();
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
        connectHeaderList.add(new StompHeader("Authorization", "Bearer "+ Constants.token));
        stompClient.connect(connectHeaderList);
    }

    public void topicSTOMP(){
        topicHeaderList=new ArrayList<>();
        topicHeaderList.add(new StompHeader("Authorization", "Bearer "+ Constants.token));
        stompClient.topic("/topic/priceSuggestion", topicHeaderList).subscribe(topicMessage -> {
              JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(topicMessage.getPayload());
            String username = element.getAsJsonObject().get("username").getAsString();
            String suggestionPrice = element.getAsJsonObject().get("suggestionPrice").getAsString();
            String maximumPrice = element.getAsJsonObject().get("maximumPrice").getAsString();
            String theNumberOfParticipants = element.getAsJsonObject().get("theNumberOfParticipants").getAsString();
            String userId = element.getAsJsonObject().get("userId").getAsString();
            System.out.println("userId"+userId );
            System.out.println("username"+username );
            System.out.println("suggestionPrice"+suggestionPrice );
            System.out.println("maximumPrice"+maximumPrice );
            System.out.println("theNumberOfParticipants"+theNumberOfParticipants );
            System.out.println("들어오는지?");

            Handler mainHandler = new Handler(Looper.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    highPrice.setText(String.valueOf(maximumPrice));
                    participants.setText(String.valueOf(theNumberOfParticipants));
                    BidParticipants data = new BidParticipants(Long.valueOf(userId), R.drawable.profile, username, Integer.valueOf(suggestionPrice), "11");
                    bidParticipants.add(data);
                    ptAdapter.validationAndDeleteItem(data.getUserId());
                    ptAdapter.addItem(data);
                    ptAdapter.notifyDataSetChanged();} // This is your code
            };
            mainHandler.post(myRunnable);

        });
    }

    public void sendMessage(Long itemId, Long userId, String suggestionPrice) throws JSONException{
        sendHeaderList = new ArrayList<>();
        sendHeaderList.add(new StompHeader("Authorization", "Bearer " + Constants.token));
        sendHeaderList.add(new StompHeader(StompHeader.DESTINATION, "/app/priceSuggestion"));
        JSONObject json = new JSONObject();
        json.put("itemId", itemId);
        json.put("userId", userId);
        json.put("suggestionPrice", suggestionPrice);
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, sendHeaderList, json.toString());
        stompClient.send(stompMessage).subscribe();
    }



}