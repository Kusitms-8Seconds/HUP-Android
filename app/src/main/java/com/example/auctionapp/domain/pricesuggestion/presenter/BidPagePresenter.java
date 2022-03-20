package com.example.auctionapp.domain.pricesuggestion.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityBidPageBinding;
import com.example.auctionapp.domain.item.adapter.PTAdapter;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.BidParticipants;
import com.example.auctionapp.domain.item.view.AuctionHistory;
import com.example.auctionapp.domain.pricesuggestion.constant.PriceConstants;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionListResponse;
import com.example.auctionapp.domain.pricesuggestion.view.BidPage;
import com.example.auctionapp.domain.pricesuggestion.view.BidPageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.firebase.FCMRequest;
import com.example.auctionapp.global.firebase.FCMResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.stomp.PriceSuggestionStomp;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class BidPagePresenter implements Presenter{
    PTAdapter ptAdapter;
    private PriceSuggestionStomp priceSuggestionStomp;
    boolean onGoing;

    Long itemId;
    Long myId = Constants.userId;

    private int userCount;
    private ArrayList<BidParticipants> bidParticipants;
    Dialog dialog01;

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
        userCount = 0;
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllPriceSuggestionByItemId(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getAllPriceSuggestionCallback()));
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getItemDetailsCallback()));
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getMaximumPrice(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceCallback()));
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getParticipants(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));
    }

    @Override
    public void init(Long id) throws IOException, JSONException {
        itemId = id;

        bidParticipants = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        binding.participantsRecyclerView.setLayoutManager(linearLayoutManager);

        ptAdapter = new PTAdapter();
        binding.participantsRecyclerView.setAdapter(ptAdapter);

        priceSuggestionStomp = new PriceSuggestionStomp();
        priceSuggestionStomp.initStomp(itemId, ptAdapter, bidParticipants, binding.highPrice, binding.participants);

        if(!onGoing) {
            binding.auctionState.setVisibility(View.GONE);
        } else {
            binding.auctionState.setVisibility(View.VISIBLE);
        }

        dialog01 = new Dialog(context);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.custom_dialog01);

        binding.bidbutton.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View view) {
                String editPrice = binding.editPrice.getText().toString();
                if(!editPrice.equals("")){
                    priceSuggestionStomp.sendMessage(itemId, Constants.userId, binding.editPrice.getText().toString());
                    ptAdapter.notifyDataSetChanged();
                    binding.editPrice.setText("");
                }
            }
        });
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
            String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);

            if(Integer.parseInt(hours) >= 24) {
                hours = String.valueOf(Integer.parseInt(hours)%24);
                binding.itemLeftTime.setText(days + "일 " + hours + "시간 " + minutes + "분 전");
            } else
                binding.itemLeftTime.setText(hours + "시간 " + minutes + "분 전");

            if(response.body().getUserId().equals(myId)) {
                binding.editPrice.setVisibility(View.GONE);
                binding.bidbutton.setText("낙찰하기");
                binding.bidbutton.setOnClickListener(new View.OnClickListener() {
                    @SneakyThrows
                    @Override
                    public void onClick(View view) {
                        FCMRequest fcmRequest = FCMRequest.of(itemId);
                        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).pushMessage(fcmRequest)
                                .enqueue(MainRetrofitTool.getCallback(new pushMessageCallback()));
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }else {
                if(Integer.parseInt(hours) <= 0 && Integer.parseInt(minutes) <= 0) {
//                    binding.bidbutton.setEnabled(false);
                } else {
                    binding.editPrice.setVisibility(View.VISIBLE);
                    binding.bidbutton.setText("입찰하기");
                    binding.bidbutton.setOnClickListener(new View.OnClickListener() {
                        @SneakyThrows
                        @Override
                        public void onClick(View view) {
                            if (!binding.editPrice.getText().toString().equals("")) {
                                priceSuggestionStomp.sendMessage(itemId, Constants.userId, binding.editPrice.getText().toString());
                                ptAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }

            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            bidPageView.showToast(errorMessageParser.getParsedErrorMessage());
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
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            bidPageView.showToast(errorMessageParser.getParsedErrorMessage());
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
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            bidPageView.showToast(errorMessageParser.getParsedErrorMessage());
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
                BidParticipants data = new BidParticipants(response.body().getData().get(i).getUserId(), "", null,
                        response.body().getData().get(i).getSuggestionPrice(), "12:46");
                bidParticipants.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).userDetails(response.body().getData().get(i).getUserId())
                        .enqueue(MainRetrofitTool.getCallback(new getUserDetailsCallback()));
            }
            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            bidPageView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, PriceConstants.EPriceCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(PriceConstants.EPriceCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }

    private class getUserDetailsCallback implements MainRetrofitCallback<UserInfoResponse> {

        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            bidParticipants.get(userCount).setPtName(response.body().getUsername());
            if(response.body().getPicture()!=null){
                bidParticipants.get(userCount).setPtImage(response.body().getPicture());
            } else {
                String ptImage = "https://firebasestorage.googleapis.com/v0/b/auctionapp-f3805.appspot.com/o/profile.png?alt=media&token=655ed158-b464-4e5e-aa56-df3d7f12bdc8";
                bidParticipants.get(userCount).setPtImage(ptImage);
            }
            ptAdapter.addItem(bidParticipants.get(userCount));
            ptAdapter.notifyDataSetChanged();
            userCount++;
//            if(response.body().getUserId().equals(myId)) {
//                binding.lyEditPrice.setVisibility(View.GONE);
//            }else {
//                binding.lyEditPrice.setVisibility(View.VISIBLE);
//            }
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
//낙찰하기
    private class pushMessageCallback implements MainRetrofitCallback<FCMResponse> {

        @Override
        public void onSuccessResponse(Response<FCMResponse> response) {
            onGoing = false;
//            binding.auctionState.setVisibility(View.GONE);
            Log.d(TAG, PriceConstants.EPriceCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<FCMResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            bidPageView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, PriceConstants.EPriceCallback.rtFailResponse.getText() + "_pushMessage" +
                    response.errorBody().string());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(PriceConstants.EPriceCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    // dialog01을 디자인하는 함수
    public void showDialog01(){
        dialog01.show(); // 다이얼로그 띄우기

        // 홈으로 돌아가기 버튼
        dialog01.findViewById(R.id.goHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog01.dismiss();
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                dialog01.dismiss();
            }
        });
        // 참여내역 확인 버튼
        dialog01.findViewById(R.id.check_bidlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AuctionHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                dialog01.dismiss();
            }
        });
    }
}
