package com.example.auctionapp.domain.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.example.auctionapp.databinding.ActivityBidPageBinding;
import com.example.auctionapp.databinding.ActivityHomeBinding;
import com.example.auctionapp.domain.home.adapter.BestItemAdapter;
import com.example.auctionapp.domain.home.constant.HomeConstants;
import com.example.auctionapp.domain.home.view.MainView;
import com.example.auctionapp.domain.home.adapter.AuctionNowAdapter;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.BestItemResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.home.model.AuctionNow;
import com.example.auctionapp.domain.home.model.BestItem;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.view.BidPageView;
import com.example.auctionapp.domain.scrap.dto.ScrapCountResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitConstants;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainPresenter implements Presenter{
    private MainView view;

    private ArrayList<BestItem> bestItemDataList;
    private BestItem bestItem;
    BestItemAdapter bestItemAdapter;
    ViewPager bestItemViewPager;

    private AuctionNow data;
    List<AuctionNow> auctionDataList = new ArrayList<>();
    int heartCount;
    AuctionNowAdapter adapter;

    int maximumPriceCount;
    int maximumPriceCount2;

    // Attributes
    private MainView mainView;
    private ActivityHomeBinding binding;
    private Context context;
    private Activity activity;

    // Constructor
    public MainPresenter(MainView mainView, ActivityHomeBinding binding, Context context, Activity activity){
        this.mainView = mainView;
        this.binding = binding;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void init() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context,2);
        binding.AuctionNowView.setLayoutManager(linearLayoutManager);
        adapter = new AuctionNowAdapter();
        binding.AuctionNowView.setAdapter(adapter);

        bestItemDataList = new ArrayList();

        adapter.setOnItemClickListener(new AuctionNowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(context, ItemDetail.class);
                context.startActivity(intent);
            }
        });

        bestItemDataList = new ArrayList<>();
        bestItemAdapter = new BestItemAdapter(context, bestItemDataList);
        binding.bestItemViewPager.setAdapter(bestItemAdapter);
    }

    @Override
    public void initializeBestData() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getBestItems(ItemConstants.EItemSoldStatus.eOnGoing)
                .enqueue(MainRetrofitTool.getCallback(new getBestItemsCallback()));
    }

    @Override
    public void initializeAuctionNowData() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllItemsInfo(ItemConstants.EItemSoldStatus.eOnGoing)
                .enqueue(MainRetrofitTool.getCallback(new getAllItemsInfoCallback()));
    }

    @Override
    public void exceptionToast(String Tag, int statusCode) {
        String errorMsg = "";
        if(statusCode==401) errorMsg = RetrofitConstants.ERetrofitCallback.eUnauthorized.getText();
        else if(statusCode==403) errorMsg = RetrofitConstants.ERetrofitCallback.eForbidden.getText();
        else if(statusCode==404) errorMsg = RetrofitConstants.ERetrofitCallback.eNotFound.getText();
        else errorMsg = String.valueOf(statusCode);
        Toast.makeText(context, Tag +
                statusCode + "_" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    // best items callback
    public class getBestItemsCallback implements MainRetrofitCallback<List<BestItemResponse>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<List<BestItemResponse>> response) {
            bestItemDataList = new ArrayList<>();
            for(int i=0; i<response.body().size(); i++){
                LocalDateTime startDateTime = LocalDateTime.now();
                LocalDateTime endDateTime = response.body().get(i).getAuctionClosingDate();
                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));

                if(response.body().get(i).getFileNames().size()!=0) {
                    bestItem = new BestItem(response.body().get(i).getFileNames().get(0),
                            response.body().get(i).getItemName(),
                            response.body().get(i).getBuyDate().getYear()+ HomeConstants.EDate.dpYear.getText() +
                                    response.body().get(i).getBuyDate().getMonth().getValue()+HomeConstants.EDate.dpMonth.getText(),
                            0
                    );
                } else{
                    bestItem = new BestItem(null,
                            response.body().get(i).getItemName(),
                            response.body().get(i).getBuyDate().getYear()+HomeConstants.EDate.dpYear.getText()+
                                    response.body().get(i).getBuyDate().getMonth().getValue()+HomeConstants.EDate.dpMonth.getText(),
                            0
                    );
                }
                bestItemDataList.add(bestItem);
//                RetrofitTool.getAPIWithNullConverter().getMaximumPrice(response.body().get(i).getId())
//                        .enqueue(MainRetrofitTool.getCallback(new Home.getMaximumPriceBestItemCallback()));
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<List<BestItemResponse>> response) throws IOException, JSONException {
            exceptionToast("best item callback", response.code());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { 
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show(); 
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(HomeConstants.EHomeCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    // item info callback
    private class getAllItemsInfoCallback implements MainRetrofitCallback<PaginationDto<List<ItemDetailsResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                LocalDateTime startDateTime = LocalDateTime.now();
                LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));

                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    data = new AuctionNow(response.body().getData().get(i).getId(),
                            response.body().getData().get(i).getFileNames().get(0),
                            response.body().getData().get(i).getItemName(),
                            response.body().getData().get(i).getInitPrice(),
                            minutes+HomeConstants.EDate.dpMinute.getText(),
                            response.body().getData().get(i).getDescription(), null);
                } else{
                    data = new AuctionNow(response.body().getData().get(i).getId(),
                            null,
                            response.body().getData().get(i).getItemName(),
                            response.body().getData().get(i).getInitPrice(),
                            minutes+HomeConstants.EDate.dpMinute.getText(),
                            response.body().getData().get(i).getDescription(), null);
                }
                auctionDataList.add(data);
                RetrofitTool.getAPIWithNullConverter().getHeart(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getHeartCallback()));
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            exceptionToast("get all item callback",response.code());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(HomeConstants.EHomeCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    // heart callback
    private class getHeartCallback implements MainRetrofitCallback<ScrapCountResponse> {

        @Override
        public void onSuccessResponse(Response<ScrapCountResponse> response) {

            auctionDataList.get(heartCount).setHeart(response.body().getHeart());
            adapter.addItem(auctionDataList.get(heartCount));
            adapter.notifyDataSetChanged();
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());
            heartCount++;
        }
        @Override
        public void onFailResponse(Response<ScrapCountResponse> response) throws IOException, JSONException {
            exceptionToast("get heart callback", response.code());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(HomeConstants.EHomeCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    private class getMaximumPriceBestItemCallback implements MainRetrofitCallback<MaximumPriceResponse> {

        @Override
        public void onSuccessResponse(Response<MaximumPriceResponse> response) throws IOException {

            bestItemDataList.get(maximumPriceCount2).setBtTempMax(response.body().getMaximumPrice());
            bestItemAdapter = new BestItemAdapter(context, bestItemDataList);
            //bestItemAdapter.notifyDataSetChanged();
            bestItemViewPager.setAdapter(bestItemAdapter);
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());
            maximumPriceCount2++;
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
            exceptionToast("get maximum price callback", response.code());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(context, jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, HomeConstants.EHomeCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(HomeConstants.EHomeCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}

