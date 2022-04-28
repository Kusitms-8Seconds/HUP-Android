package com.me.hurryuphup.global.stomp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.me.hurryuphup.databinding.ActivityBidPageBinding;
import com.me.hurryuphup.domain.item.model.BidParticipants;
import com.me.hurryuphup.domain.item.adapter.PTAdapter;
import com.me.hurryuphup.domain.pricesuggestion.view.BidPageView;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;


public class PriceSuggestionStomp {

    private static final String url = "http://www.hurryuphup.me/websocket/websocket";

    private StompClient stompClient;
    private List<StompHeader> connectHeaderList;
    private List<StompHeader> topicHeaderList;
    private List<StompHeader> sendHeaderList;

    Long itemId;
    private PTAdapter ptAdapter;
    private ArrayList<BidParticipants> bidParticipants;

    ActivityBidPageBinding binding;
    BidParticipants data;
    DecimalFormat myFormatter = new DecimalFormat("###,###");

    String username;
    String suggestionPrice;
    String maximumPrice;
    String theNumberOfParticipants;
    String userId;
    String picture;

    private BidPageView bidPageView;

    @SuppressLint("CheckResult")
    public void initStomp(Long itemId, PTAdapter adapter, ArrayList<BidParticipants> bidParticipants, ActivityBidPageBinding binding) throws IOException, JSONException {
        this.itemId = itemId;
        this.ptAdapter = adapter;
        this.bidParticipants = bidParticipants;
        this.binding = binding;
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
        connectHeaderList.add(new StompHeader("Authorization", "Bearer "+ Constants.accessToken));
        stompClient.connect(connectHeaderList);
    }

    public void topicSTOMP() throws IOException, JSONException {
        topicHeaderList=new ArrayList<>();
        topicHeaderList.add(new StompHeader("Authorization", "Bearer "+ Constants.accessToken));
        stompClient.topic("/sub/priceSuggestions/"+itemId, topicHeaderList).subscribe(topicMessage -> {
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(topicMessage.getPayload());
            username = element.getAsJsonObject().get("username").getAsString();
            suggestionPrice = element.getAsJsonObject().get("suggestionPrice").getAsString();
            maximumPrice = element.getAsJsonObject().get("maximumPrice").getAsString();
            theNumberOfParticipants = element.getAsJsonObject().get("theNumberOfParticipants").getAsString();
            userId = element.getAsJsonObject().get("userId").getAsString();
            picture = element.getAsJsonObject().get("picture").getAsString();

            Handler mainHandler = new Handler(Looper.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    binding.highPrice.setText(myFormatter.format(Integer.valueOf(maximumPrice)));
                    binding.participants.setText(String.valueOf(theNumberOfParticipants));

                    BidParticipants data = new BidParticipants(Long.valueOf(userId), picture, username, myFormatter.format(Integer.valueOf(suggestionPrice)), null);
                    bidParticipants.add(data);
                    ptAdapter.validationAndDeleteItem(data.getUserId());
                    ptAdapter.addItem(data);
                    ptAdapter.notifyDataSetChanged();
                }
            };
            mainHandler.post(myRunnable);

        });
    }

    public void sendMessage(Long itemId, Long userId, String suggestionPrice) throws JSONException{
        sendHeaderList = new ArrayList<>();
        sendHeaderList.add(new StompHeader("Authorization", "Bearer " + Constants.accessToken));
        sendHeaderList.add(new StompHeader(StompHeader.DESTINATION, "/pub/priceSuggestions"));
        JSONObject json = new JSONObject();
        json.put("itemId", itemId);
        json.put("userId", userId);
        json.put("suggestionPrice", suggestionPrice);
        StompMessage stompMessage = new StompMessage(StompCommand.SEND, sendHeaderList, json.toString());
        stompClient.send(stompMessage).subscribe();
    }
}