package com.example.auctionapp.domain.pricesuggestion.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityBidPageBinding;
import com.example.auctionapp.domain.item.adapter.PTAdapter;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.BidParticipants;
import com.example.auctionapp.domain.item.presenter.ItemDetailPresenter;
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
import com.example.auctionapp.global.util.CustomTextWatcher;
import com.example.auctionapp.global.util.ErrorMessageParser;
import com.example.auctionapp.global.util.GetTime;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lombok.SneakyThrows;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class BidPagePresenter implements Presenter{
    PTAdapter ptAdapter;
    private PriceSuggestionStomp priceSuggestionStomp;
    boolean onGoing;

    Long itemId;
    Long myId = Constants.userId;

    private int userCount;
    private ArrayList<BidParticipants> bidParticipants;
    Dialog dialog01;
    DecimalFormat myFormatter = new DecimalFormat("###,###");

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
        priceSuggestionStomp.initStomp(itemId, ptAdapter, bidParticipants, binding);

        dialog01 = new Dialog(context);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.custom_dialog01);

        binding.editPrice.addTextChangedListener(new CustomTextWatcher(binding.editPrice));
        binding.editPrice.setTypeface(binding.editPrice.getTypeface(), Typeface.BOLD);
    }
    // update time with handler and timertask
    Handler mHandler;
    Runnable mUpdateTimeTask;
    class MainTimerTask extends TimerTask {
        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }
    private class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            // item image
            if(response.body().getFileNames().size()!=0){
                Glide.with(context).load(Constants.imageBaseUrl+response.body().getFileNames().get(0))
                        .into(binding.bidImage); }
            // item init price
            binding.initPrice.setText(myFormatter.format(response.body().getInitPrice()));
            // item status
            String state = response.body().getSoldStatus().getName();
            String stateStr = "";
            if(state.equals(ItemConstants.EItemSoldStatus.eNew.getName())) stateStr = "NEW!";
            else if(state.equals(ItemConstants.EItemSoldStatus.eOnGoing.getName())) stateStr = "경매 중";
            else if(state.equals(ItemConstants.EItemSoldStatus.eSoldOut.getName())) {
                stateStr = "판매 완료";
                binding.auctionState.setBackgroundResource(R.drawable.circle_button_gray);
            }
            binding.auctionState.setText(stateStr);

            // timer
            MainTimerTask timerTask = new MainTimerTask();
            Timer mTimer = new Timer();
            mTimer.schedule(timerTask, 500, 1000);
            //handler
            mHandler = new Handler();
            mUpdateTimeTask = new Runnable() {
                public void run() {
                    LocalDateTime startDateTime = LocalDateTime.now();
                    LocalDateTime endDateTime = response.body().getAuctionClosingDate();
                    String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                    String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                    String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);
                    String second = String.valueOf(ChronoUnit.SECONDS.between(startDateTime, endDateTime)%60);

                    GetTime getTime = new GetTime(binding.itemLeftTime, days, hours, minutes, second, mTimer);
                }
            };

            // 만약 판매자일 때
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
            } else {
                binding.bidbutton.setOnClickListener(new View.OnClickListener() {
                    @SneakyThrows
                    @Override
                    public void onClick(View view) {
                        String editPrice = binding.editPrice.getText().toString().replace(",","");
                        String maxPrice = binding.highPrice.getText().toString().replace(",","");
                        if(editPrice.equals("")){
                            bidPageView.showToast(PriceConstants.EPriceSuggestionServiceImpl.eEditPriceMessage.getValue());
                        } else if(Integer.parseInt(editPrice) <= Integer.parseInt(maxPrice) ||
                                Integer.parseInt(editPrice) <= response.body().getInitPrice()) {
                            bidPageView.showToast(PriceConstants.EPriceSuggestionServiceImpl.ePriorPriceSuggestionExceptionMessage.getValue());
                        } else if(binding.itemLeftTime.getText().toString().equals("경매 시간 종료")) {
                            bidPageView.showToast(PriceConstants.EPriceSuggestionServiceImpl.eTimeOutExceptionMessage.getValue());
                        } else {
                            priceSuggestionStomp.sendMessage(itemId, Constants.userId, editPrice);
                            ptAdapter.notifyDataSetChanged();
                            binding.editPrice.setText("");
                        }
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binding.editPrice.getWindowToken(), 0);
                    }
                });
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
            binding.highPrice.setText(myFormatter.format(response.body().getMaximumPrice()));
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
                BidParticipants data = new BidParticipants(response.body().getData().get(i).getUserId(), response.body().getData().get(i).getPicture(), response.body().getData().get(i).getUserName(),
                        myFormatter.format(response.body().getData().get(i).getSuggestionPrice()), null);
                bidParticipants.add(data);
                ptAdapter.addItem(data);
                ptAdapter.notifyDataSetChanged();
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
    //낙찰하기
    private class pushMessageCallback implements MainRetrofitCallback<FCMResponse> {

        @Override
        public void onSuccessResponse(Response<FCMResponse> response) {
            onGoing = false;
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
