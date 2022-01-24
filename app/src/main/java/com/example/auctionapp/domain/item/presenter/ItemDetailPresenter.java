package com.example.auctionapp.domain.item.presenter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityItemDetailBinding;
import com.example.auctionapp.domain.item.adapter.ItemDetailViewPagerAdapter;
import com.example.auctionapp.domain.item.adapter.qnaAdapter;
import com.example.auctionapp.global.dto.DefaultResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.qnaData;
import com.example.auctionapp.domain.item.view.ItemDetailView;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitConstants;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemDetailPresenter implements ItemDetailPresenterInterface{
    private ArrayList<String> itemImageList;
    private static final int DP = 24;
    // id
    Long myId = Constants.userId;
    private Long itemId;
    private Long scrapId;
    public Boolean isHeart = false;
    ItemDetailViewPagerAdapter itemDetailViewPagerAdapter;
    ArrayList<qnaData> qnaList = new ArrayList<qnaData>();
    qnaAdapter adapter;

    // Attributes
    private ItemDetailView itemDetailView;
    private ActivityItemDetailBinding binding;
    private Context context;

    // Constructor
    public ItemDetailPresenter(ItemDetailView itemDetailView, ActivityItemDetailBinding binding, Context context){
        this.itemDetailView = itemDetailView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void initializeImageData() {
        itemImageList = new ArrayList();
        itemDetailViewPagerAdapter = new ItemDetailViewPagerAdapter(context, itemImageList);
        binding.itemDetailViewPager.setAdapter(itemDetailViewPagerAdapter);
    }

    @Override
    public void qnaInit() {
        adapter = new qnaAdapter(context, qnaList);
        binding.itemDetailQnaList.setAdapter(adapter);

        qnaList.add(new qnaData("자전거 많이 무겁나요?", "2021.11.27", "hoa9***", false, true));
        qnaList.add(new qnaData("기능 어떤 것들이 있나요?", "2021.11.26", "둠***", true, true));
        qnaList.add(new qnaData("자전거 브랜드 궁금합니다.", "2021.11.26", "우왕***", true, true));

        binding.qaCount.setText("(" + String.valueOf(qnaList.size()-1) + ")");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void heartCheckCallback(Long userId, Long itemId) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                .isCheckedHeart(ScrapCheckedRequest.of(userId, itemId))
                .enqueue(MainRetrofitTool.getCallback(new isCheckedHeartCallback()));
    }

    @Override
    public void deleteHeartCallback(Long scrapId) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                .deleteHeart(scrapId)
                .enqueue(MainRetrofitTool.getCallback(new deleteScrapCallback()));
    }

    @Override
    public void createHeartCallback(Long userId, Long itemId) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                .createScrap(ScrapRegisterRequest.of(userId, itemId))
                .enqueue(MainRetrofitTool.getCallback(new createScrapCallback()));
    }

    @Override
    public void getItemInfoCallback(Long itemId) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getMaximumPrice(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceCallback()));

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getParticipants(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getItemDetailsCallback()));
    }

    @Override
    public void getUserInfoCallback(Long userId) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(userId)
                .enqueue(MainRetrofitTool.getCallback(new getUserDetailsCallback()));
    }

    @Override
    public void deleteItem(Long itemId) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).deleteItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new DeleteItemCallback()));
    }

    @Override
    public void exceptionToast(int statusCode) {
        String errorMsg = "";
        if(statusCode==401) errorMsg = RetrofitConstants.ERetrofitCallback.eUnauthorized.getText();
        else if(statusCode==403) errorMsg = RetrofitConstants.ERetrofitCallback.eForbidden.getText();
        else if(statusCode==404) errorMsg = RetrofitConstants.ERetrofitCallback.eNotFound.getText();
        else errorMsg = String.valueOf(statusCode);
        Toast.makeText(context, "Item: " +
                statusCode + "_" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    private class DeleteItemCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<DefaultResponse> response) {
            exceptionToast(response.code());
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class deleteScrapCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            binding.isheart.setImageResource(R.drawable.heartx);
            isHeart = false;
            scrapId = null;
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<DefaultResponse> response) {
            exceptionToast(response.code());
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class createScrapCallback implements MainRetrofitCallback<ScrapRegisterResponse> {
        @Override
        public void onSuccessResponse(Response<ScrapRegisterResponse> response) {
            binding.isheart.setImageResource(R.drawable.hearto);
            isHeart = true;
            scrapId = response.body().getScrapId();
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<ScrapRegisterResponse> response) {
            exceptionToast(response.code());
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class isCheckedHeartCallback implements MainRetrofitCallback<ScrapCheckedResponse> {
        @Override
        public void onSuccessResponse(Response<ScrapCheckedResponse> response) {

            if(response.body().getScrapId()!=null){
                binding.isheart.setImageResource(R.drawable.hearto);
                isHeart = true;
                scrapId = response.body().getScrapId();
            } else{
                binding.isheart.setImageResource(R.drawable.heartx);
                isHeart = false;
                scrapId = null;
            }
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<ScrapCheckedResponse> response) {
            exceptionToast(response.code());
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getMaximumPriceCallback implements MainRetrofitCallback<MaximumPriceResponse> {

        @Override
        public void onSuccessResponse(Response<MaximumPriceResponse> response) throws IOException {

            binding.highPrice.setText(String.valueOf(response.body().getMaximumPrice()));
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
            exceptionToast(response.code());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getParticipantsCallback implements MainRetrofitCallback<ParticipantsResponse> {

        @Override
        public void onSuccessResponse(Response<ParticipantsResponse> response) {
            binding.participants.setText(String.valueOf(response.body().getParticipantsCount()));
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ParticipantsResponse> response) throws IOException, JSONException {
            exceptionToast(response.code());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getUserDetailsCallback implements MainRetrofitCallback<UserInfoResponse> {

        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            binding.sellerName.setText(response.body().getUsername());
            if(!response.body().getPicture().isEmpty()){
                Glide.with(context).load(response.body().getPicture()).into(binding.sellerImage);
            }
            //delete item
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<UserInfoResponse> response) throws IOException, JSONException {
            exceptionToast(response.code());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    public class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            binding.itemName.setText(response.body().getItemName());
            binding.itemContent.setText(response.body().getDescription());
            if(response.body().getFileNames().size()!=0){
                for (int i=0; i<response.body().getFileNames().size(); i++) {
                    itemImageList.add(response.body().getFileNames().get(i));
                }
                itemDetailViewPagerAdapter = new ItemDetailViewPagerAdapter(context, itemImageList);
                binding.itemDetailViewPager.setAdapter(itemDetailViewPagerAdapter);
            }
            binding.category.setText(response.body().getCategory().getName());
            LocalDateTime startDateTime = LocalDateTime.now();
            LocalDateTime endDateTime = response.body().getAuctionClosingDate();
            String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
            String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
            String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)/60);
            binding.itemLeftTime.setText(days+"일 "+hours+"시간 "+minutes+"분 전");

            if(response.body().getUserId().equals(myId)) {

                binding.deleteButton.setVisibility(View.VISIBLE);
                //button inactivated
                binding.participateButton.setEnabled(false);
                binding.participateButton.setBackgroundColor(Color.GRAY);
            }else {
                binding.deleteButton.setVisibility(View.GONE);
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            exceptionToast(response.code());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
