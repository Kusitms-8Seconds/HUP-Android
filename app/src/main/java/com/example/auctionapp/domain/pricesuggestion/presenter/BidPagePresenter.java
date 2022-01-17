package com.example.auctionapp.domain.pricesuggestion.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityBidPageBinding;
import com.example.auctionapp.databinding.ActivityMypageBinding;
import com.example.auctionapp.domain.item.adapter.PTAdapter;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.BidParticipants;
import com.example.auctionapp.domain.mypage.view.MypageView;
import com.example.auctionapp.domain.pricesuggestion.constant.PriceConstants;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionListResponse;
import com.example.auctionapp.domain.pricesuggestion.view.BidPage;
import com.example.auctionapp.domain.pricesuggestion.view.BidPageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.stomp.HupStomp;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class BidPagePresenter implements Presenter{
    Dialog dialog01;
    Dialog dialog02;
    Dialog dialog03;
    PTAdapter ptAdapter;
    private HupStomp hupstomp;

    Long itemId;
    Long participantId;
    int finalPrice;
    Long myId = Constants.userId;


    private int userCount;
    PTAdapter adapter;
    private ArrayList<BidParticipants> bidParticipants;
    RecyclerView ptRecyclerView;

    // Attributes
    private BidPageView bidPageView;
    private ActivityBidPageBinding binding;
    private Context context;

    // Constructor
    public BidPagePresenter(BidPageView bidPageView, ActivityBidPageBinding binding, Context context){
        this.bidPageView = bidPageView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void initializeData(Long itemId) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllPriceSuggestionByItemId(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getAllPriceSuggestionCallback()));
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getItemDetailsCallback()));
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getMaximumPrice(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceCallback()));
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getParticipants(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));
    }

    @Override
    public void init() {
        bidParticipants = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.participantsRecyclerView.setLayoutManager(linearLayoutManager);

        ptAdapter = new PTAdapter();
        ptRecyclerView.setAdapter(ptAdapter);
    }


    private class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {

        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            if(response.body().getFileNames().size()!=0){
                Glide.with(context).load(Constants.imageBaseUrl+response.body().getFileNames().get(0))
                        .into(binding.bidImage); }
            LocalDateTime startDateTime = LocalDateTime.now();
            LocalDateTime endDateTime = response.body().getAuctionClosingDate();
            String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
            String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
            String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)/60);
            binding.itemLeftTime.setText(days + PriceConstants.EPriceCallback.dpDay.getText() + hours +
                    PriceConstants.EPriceCallback.dpHour.getText() + minutes + PriceConstants.EPriceCallback.dpMinute.getText());
            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println(PriceConstants.EPriceCallback.errorBody.getText()+response.errorBody().string());
            Log.d(TAG, PriceConstants.EPriceCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(PriceConstants.EPriceCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }

    private class getMaximumPriceCallback implements MainRetrofitCallback<MaximumPriceResponse> {

        @Override
        public void onSuccessResponse(Response<MaximumPriceResponse> response) throws IOException {

            binding.highPrice.setText(String.valueOf(response.body().getMaximumPrice()));
            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
            Log.d(TAG, PriceConstants.EPriceCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(PriceConstants.EPriceCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }

    private class getParticipantsCallback implements MainRetrofitCallback<ParticipantsResponse> {

        @Override
        public void onSuccessResponse(Response<ParticipantsResponse> response) {
            binding.participants.setText(String.valueOf(response.body().getParticipantsCount()));
            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ParticipantsResponse> response) throws IOException, JSONException {
            System.out.println(PriceConstants.EPriceCallback.errorBody.getText()+response.errorBody().string());
            Log.d(TAG, PriceConstants.EPriceCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(PriceConstants.EPriceCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }

    private class getAllPriceSuggestionCallback implements MainRetrofitCallback<PaginationDto<List<PriceSuggestionListResponse>>> {

        @Override
        public void onSuccessResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                BidParticipants data = new BidParticipants(response.body().getData().get(i).getUserId(), R.drawable.hearto, null,
                        response.body().getData().get(i).getSuggestionPrice(), "12:46");
                bidParticipants.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(UserDetailsInfoRequest.of(response.body().getData().get(i).getUserId()))
                        .enqueue(MainRetrofitTool.getCallback(new getUserDetailsCallback()));
//                setAnimation();
            }
            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) throws IOException, JSONException {
            System.out.println(PriceConstants.EPriceCallback.errorBody.getText()+response.errorBody().string());
            Log.d(TAG, PriceConstants.EPriceCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(PriceConstants.EPriceCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }

    private class getUserDetailsCallback implements MainRetrofitCallback<UserDetailsInfoResponse> {

        @Override
        public void onSuccessResponse(Response<UserDetailsInfoResponse> response) {

            bidParticipants.get(userCount).setPtName(response.body().getUsername());
            ptAdapter.addItem(bidParticipants.get(userCount));
            ptAdapter.notifyDataSetChanged();
            userCount++;
            if(response.body().getUserId().equals(myId)) {
                binding.lyEditPrice.setVisibility(View.GONE);
            }else {
                binding.lyEditPrice.setVisibility(View.VISIBLE);
            }
            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<UserDetailsInfoResponse> response) throws IOException, JSONException {
            System.out.println(PriceConstants.EPriceCallback.errorBody.getText()+response.errorBody().string());
            Log.d(TAG, PriceConstants.EPriceCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(PriceConstants.EPriceCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
