package com.example.auctionapp.domain.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.example.auctionapp.domain.scrap.dto.ScrapCountResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitConstants;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

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

    private ArrayList<BestItem> bestItemDataList;
    private BestItem bestItem;
    BestItemAdapter bestItemAdapter;

    private AuctionNow data;
    List<AuctionNow> auctionDataList = new ArrayList<>();
    int heartCount;
    AuctionNowAdapter auctionNowAdapter;

    //AuctionNow Attributes
    Long[] itemId;
    String[] imageURL;
    String[] itemName;
    int[] itemPrice;
    String[] date;
    String[] itemInfo;
    Long[] heart;
    //BestItem Attributes
    String[] btImage;
    String[] btName;
    String[] btTime;
    int[] btTempMax;

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
        auctionNowAdapter = new AuctionNowAdapter();
        binding.AuctionNowView.setAdapter(auctionNowAdapter);

        auctionNowAdapter.setOnItemClickListener(new AuctionNowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(context, ItemDetail.class);
                intent.putExtra("itemId", auctionNowAdapter.getAuctionNowData().get(position).getItemId());
                context.startActivity(intent);
            }
        });

        bestItemDataList = new ArrayList<>();
        bestItemAdapter = new BestItemAdapter(context, bestItemDataList);
        binding.bestItemViewPager.setAdapter(bestItemAdapter);
    }

    @Override
    public void initializeBestData() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getBestItems(ItemConstants.EItemSoldStatus.eOnGoing)
                .enqueue(MainRetrofitTool.getCallback(new getBestItemsCallback()));
    }

    @Override
    public void initializeAuctionNowData() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllItemsInfo(ItemConstants.EItemSoldStatus.eOnGoing)
                .enqueue(MainRetrofitTool.getCallback(new getAllItemsInfoCallback()));
    }

    // best items callback
    public class getBestItemsCallback implements MainRetrofitCallback<List<BestItemResponse>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<List<BestItemResponse>> response) {
            btImage = new String[response.body().size()];
            btName = new String[response.body().size()];
            btTime = new String[response.body().size()];
            btTempMax = new int[response.body().size()];
            for(int i=0; i<response.body().size(); i++){
                btName[i] = response.body().get(i).getItemName();
                btTime[i] = response.body().get(i).getBuyDate().getYear()+ HomeConstants.EDate.dpYear.getText() + " " +
                        response.body().get(i).getBuyDate().getMonth().getValue()+HomeConstants.EDate.dpMonth.getText();
                btTempMax[i] = response.body().get(i).getInitPrice();

                if(response.body().get(i).getFileNames().size()!=0) {
                    btImage[i] = response.body().get(i).getFileNames().get(0);
                } else{
                    btImage[i] = null;
                }
                RetrofitTool.getAPIWithNullConverter().getMaximumPrice(response.body().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceBestItemCallback()));
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<List<BestItemResponse>> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            mainView.showToast(errorMessageParser.getParsedErrorMessage());
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
            itemId = new Long[response.body().getData().size()];
            itemName = new String[response.body().getData().size()];
            itemPrice = new int[response.body().getData().size()];
            date = new String[response.body().getData().size()];
            itemInfo = new String[response.body().getData().size()];
            heart = new Long[response.body().getData().size()];
            imageURL = new String[response.body().getData().size()];
            for(int i=0; i<response.body().getData().size(); i++){
                LocalDateTime startDateTime = LocalDateTime.now();
                LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);

                itemId[i] = response.body().getData().get(i).getId();
                itemName[i] = response.body().getData().get(i).getItemName();
                itemPrice[i] = response.body().getData().get(i).getInitPrice();
                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    date[i] = days + "일 " + hours + "시간 " + minutes + "분 전";
                } else
                    date[i] = hours + "시간 " + minutes + "분 전";
                itemInfo[i] = response.body().getData().get(i).getDescription();
                heart[i] = null;
                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    imageURL[i] = response.body().getData().get(i).getFileNames().get(0);
                } else{
                    imageURL[i] = null;
                }
                RetrofitTool.getAPIWithNullConverter().getHeart(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getHeartCallback()));
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            mainView.showToast(errorMessageParser.getParsedErrorMessage());
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
            heart[heartCount] = response.body().getHeart();
            data = new AuctionNow(itemId[heartCount], imageURL[heartCount], itemName[heartCount], itemPrice[heartCount],
                    date[heartCount], itemInfo[heartCount], heart[heartCount]);
            auctionDataList.add(data);
            auctionNowAdapter.addItem(auctionDataList.get(heartCount));
            auctionNowAdapter.notifyDataSetChanged();
            setAuctionItemAnimation();
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());
            heartCount++;
        }
        @Override
        public void onFailResponse(Response<ScrapCountResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            mainView.showToast(errorMessageParser.getParsedErrorMessage());
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
            btTempMax[maximumPriceCount2] = response.body().getMaximumPrice();
            bestItem = new BestItem(btImage[maximumPriceCount2], btName[maximumPriceCount2], btTime[maximumPriceCount2], btTempMax[maximumPriceCount2]);
            bestItemDataList.add(bestItem);
            bestItemAdapter.notifyDataSetChanged();
            binding.bestItemViewPager.setAdapter(bestItemAdapter);
            setBestItemAnimation();
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());
            maximumPriceCount2++;
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            mainView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, HomeConstants.EHomeCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(HomeConstants.EHomeCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }

    public void setAuctionItemAnimation() {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1500);
        binding.AuctionNowView.setAnimation(animation);
    }
    public void setBestItemAnimation() {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1500);
        binding.bestItemViewPager.setAnimation(animation);
    }
}

