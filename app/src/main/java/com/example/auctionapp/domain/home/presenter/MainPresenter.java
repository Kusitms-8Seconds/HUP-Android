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
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
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
    Long itemId;
    String imageURL;
    String itemName;
    int itemPrice;
    String date;
    String itemInfo;
    //BestItem Attributes
    String btImage;
    String btName;
    String btTime;
    int btTempMax;

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
                if(Constants.userId != null) {
                    Intent intent = new Intent(context, ItemDetail.class);
                    intent.putExtra("itemId", auctionNowAdapter.getAuctionNowData().get(position).getItemId());
                    context.startActivity(intent);
                } else {
                    mainView.showToast(MypageConstants.ELogin.afterLogin.getText());
                }
            }
        });

//        bestItemAdapter = new BestItemAdapter(context, bestItemDataList);
//        binding.bestItemViewPager.setAdapter(bestItemAdapter);
        setBestItemAnimation();
        maximumPriceCount2 = 0;
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
            bestItemDataList = new ArrayList<>();
            for(int i=0; i<response.body().size(); i++){
                btName = response.body().get(i).getItemName();
                btTime = response.body().get(i).getBuyDate().getYear()+ HomeConstants.EDate.dpYear.getText() + " " +
                        response.body().get(i).getBuyDate().getMonth().getValue()+HomeConstants.EDate.dpMonth.getText();
                btTempMax = response.body().get(i).getInitPrice();

                if(response.body().get(i).getFileNames().size()!=0) {
                    btImage = response.body().get(i).getFileNames().get(0);
                } else{
                    btImage = null;
                }
                bestItem = new BestItem(btImage, btName, btTime, btTempMax);
                bestItemDataList.add(bestItem);

                RetrofitTool.getAPIWithNullConverter().getMaximumPrice(response.body().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceBestItemCallback()));

//                bestItemAdapter.notifyDataSetChanged();
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
            for(int i=0; i<response.body().getData().size(); i++){
                LocalDateTime startDateTime = LocalDateTime.now();
                LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);

                itemId = response.body().getData().get(i).getId();
                itemName = response.body().getData().get(i).getItemName();
                itemPrice = response.body().getData().get(i).getInitPrice();
                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    date = days + "일 " + hours + "시간 " + minutes + "분 전";
                } else
                    date = hours + "시간 " + minutes + "분 전";
                itemInfo = response.body().getData().get(i).getDescription();
                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    imageURL = response.body().getData().get(i).getFileNames().get(0);
                } else{
                    imageURL = null;
                }
                data = new AuctionNow(itemId, imageURL, itemName, itemPrice, date, itemInfo);
                auctionDataList.add(data);
                auctionNowAdapter.addItem(data);
                auctionNowAdapter.notifyDataSetChanged();
                setAuctionItemAnimation();
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
    private class getMaximumPriceBestItemCallback implements MainRetrofitCallback<MaximumPriceResponse> {

        @Override
        public void onSuccessResponse(Response<MaximumPriceResponse> response) throws IOException {
            bestItemDataList.get(maximumPriceCount2).setBtTempMax(response.body().getMaximumPrice());
            bestItemAdapter = new BestItemAdapter(context, bestItemDataList);
            binding.bestItemViewPager.setAdapter(bestItemAdapter);
            bestItemAdapter.notifyDataSetChanged();

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

