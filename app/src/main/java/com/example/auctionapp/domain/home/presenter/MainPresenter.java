package com.example.auctionapp.domain.home.presenter;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.auctionapp.domain.home.view.Home;
import com.example.auctionapp.domain.home.view.MainView;
import com.example.auctionapp.domain.item.adapter.AuctionNowAdapter;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.BestItemResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.AuctionNow;
import com.example.auctionapp.domain.item.model.BestItem;
import com.example.auctionapp.domain.scrap.dto.ScrapCountResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
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

    private ArrayList<BestItem> bestItemDataList = new ArrayList<>();
    private BestItem bestItem;
    private AuctionNow data;
    List<AuctionNow> auctionDataList = new ArrayList<>();
    int heartCount;
    AuctionNowAdapter adapter;

    public MainPresenter(MainView view){
        this.view = view;
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
                            response.body().get(i).getBuyDate().getYear()+"년"+
                                    response.body().get(i).getBuyDate().getMonth().getValue()+"월",
                            0
                    );
                } else{
                    bestItem = new BestItem(null,
                            response.body().get(i).getItemName(),
                            response.body().get(i).getBuyDate().getYear()+"년"+
                                    response.body().get(i).getBuyDate().getMonth().getValue()+"월",
                            0
                    );
                }
                bestItemDataList.add(bestItem);
//                RetrofitTool.getAPIWithNullConverter().getMaximumPrice(response.body().get(i).getId())
//                        .enqueue(MainRetrofitTool.getCallback(new Home.getMaximumPriceBestItemCallback()));
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<List<BestItemResponse>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { 
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show(); 
            }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
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
                            minutes+"분",
                            response.body().getData().get(i).getDescription(), null);
                } else{
                    data = new AuctionNow(response.body().getData().get(i).getId(),
                            null,
                            response.body().getData().get(i).getItemName(),
                            response.body().getData().get(i).getInitPrice(),
                            minutes+"분",
                            response.body().getData().get(i).getDescription(), null);
                }
                auctionDataList.add(data);
                RetrofitTool.getAPIWithNullConverter().getHeart(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getHeartCallback()));
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
    // heart callback
    private class getHeartCallback implements MainRetrofitCallback<ScrapCountResponse> {

        @Override
        public void onSuccessResponse(Response<ScrapCountResponse> response) {

            auctionDataList.get(heartCount).setHeart(response.body().getHeart());
            adapter.addItem(auctionDataList.get(heartCount));
            adapter.notifyDataSetChanged();
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            heartCount++;
        }
        @Override
        public void onFailResponse(Response<ScrapCountResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}

