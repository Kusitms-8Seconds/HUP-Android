package com.example.auctionapp.global.stomp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.example.auctionapp.R;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static android.content.ContentValues.TAG;


public class PriceSuggestionStomp {

    private static final String url = "http://52.78.175.27:8080/websocket/websocket";

    private StompClient stompClient;
    private List<StompHeader> connectHeaderList;
    private List<StompHeader> topicHeaderList;
    private List<StompHeader> sendHeaderList;

    Long itemId;
    private PTAdapter ptAdapter;
    private ArrayList<BidParticipants> bidParticipants;
    TextView participants;
    TextView highPrice;
    BidParticipants data;

    String username;
    String suggestionPrice;
    String maximumPrice;
    String theNumberOfParticipants;
    String userId;

    private BidPageView bidPageView;

    @SuppressLint("CheckResult")
    public void initStomp(Long itemId, PTAdapter adapter, ArrayList<BidParticipants> bidParticipants, TextView highPrice, TextView participants) throws IOException, JSONException {
        this.itemId = itemId;
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

            Handler mainHandler = new Handler(Looper.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    highPrice.setText(String.valueOf(maximumPrice));
                    participants.setText(String.valueOf(theNumberOfParticipants));

                    RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).userDetails(Long.valueOf(userId))
                            .enqueue(MainRetrofitTool.getCallback(new getUserDetailsCallback()));
                } // This is your code
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
    private class getUserDetailsCallback implements MainRetrofitCallback<UserInfoResponse> {

        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            String ptImage = "";
            if(response.body().getPicture()!=null){
                ptImage = response.body().getPicture();
            } else {
                ptImage = "https://firebasestorage.googleapis.com/v0/b/auctionapp-f3805.appspot.com/o/profile.png?alt=media&token=655ed158-b464-4e5e-aa56-df3d7f12bdc8";
            }
            BidParticipants data = new BidParticipants(Long.valueOf(userId), ptImage, username, Integer.valueOf(suggestionPrice), "11");
            bidParticipants.add(data);
            ptAdapter.validationAndDeleteItem(data.getUserId());
            ptAdapter.addItem(data);
            ptAdapter.notifyDataSetChanged();
            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<UserInfoResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            bidPageView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, PriceConstants.EPriceCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(PriceConstants.EPriceCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }


}