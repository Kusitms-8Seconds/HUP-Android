package com.example.auctionapp;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class HupStomp {

    String url = "ws://192.168.2.2/websocket/websocket"; // 소켓에 연결하는 엔드포인트가 /socket일때 다음과 같음

    private StompClient stompClient;
    private List<StompHeader> connectHeaderList;
    private List<StompHeader> topicHeaderList;
    private List<StompHeader> sendHeaderList;

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJranc1OTgwMDAiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjM2NzI5NTQxfQ.sQYhLm4SkNBaosDB2-CbBH_dSRXRlYB7-2fZFwse_t4JbWv3dw-CCxHVkirocpqQEzLQL8T5LggEdWSi3QvI8w";

    @SuppressLint("CheckResult")
    public void initStomp(){
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
        connectHeaderList.add(new StompHeader("Authorization", "Bearer "+ token));
        stompClient.connect(connectHeaderList);
    }



    public void topicSTOMP(){
        topicHeaderList=new ArrayList<>();
        topicHeaderList.add(new StompHeader("Authorization", "Bearer "+ token));
        stompClient.topic("/topic/greetings", topicHeaderList).subscribe(topicMessage -> {
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(topicMessage.getPayload());
            System.out.println("토픽" + obj.toString());

        });
    }

    public void sendMessage() throws JSONException{
        sendHeaderList = new ArrayList<>();
        sendHeaderList.add(new StompHeader("Authorization", "Bearer " + token));
        sendHeaderList.add(new StompHeader(StompHeader.DESTINATION, "/app/hello"));
        JSONObject json = new JSONObject();
        json.put("name", "kimjungwoo");
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, sendHeaderList, json.toString());
        stompClient.send(stompMessage).subscribe();

    }

}