package com.me.hurryuphup.domain.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;

import com.me.hurryuphup.databinding.ActivityHomeBinding;
import com.me.hurryuphup.domain.home.adapter.BestItemAdapter;
import com.me.hurryuphup.domain.home.constant.HomeConstants;
import com.me.hurryuphup.domain.home.view.MainView;
import com.me.hurryuphup.domain.home.adapter.AuctionNowAdapter;
import com.me.hurryuphup.domain.item.constant.ItemConstants;
import com.me.hurryuphup.domain.item.dto.BestItemResponse;
import com.me.hurryuphup.domain.item.dto.ItemDetailsResponse;
import com.me.hurryuphup.domain.home.model.AuctionNow;
import com.me.hurryuphup.domain.home.model.BestItem;
import com.me.hurryuphup.domain.item.view.ItemDetail;
import com.me.hurryuphup.domain.mypage.constant.MypageConstants;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.global.dto.PaginationDto;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;
import com.me.hurryuphup.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainPresenter implements Presenter{

    private ArrayList<BestItem> bestItemDataList;
    private BestItem bestItem;
    BestItemAdapter bestItemAdapter;

    private AuctionNow data;
    List<AuctionNow> auctionDataList = new ArrayList<>();
    AuctionNowAdapter auctionNowAdapter;
    DecimalFormat myFormatter = new DecimalFormat("###,###");

    //AuctionNow Attributes
    Long itemId;
    String imageURL;
    String itemName;
    int initPrice;
    int maxPrice;
    String date;
    String itemInfo;
    //BestItem Attributes
    String btImage;
    String btName;
    String btTime;
    int btTempMax;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 200;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3500; // time in milliseconds between successive task executions.

    // Attributes
    private MainView mainView;
    private ActivityHomeBinding binding;
    private Context context;
    private Activity activity;
    ErrorMessageParser errorMessageParser;

    // Constructor
    public MainPresenter(MainView mainView, ActivityHomeBinding binding, Context context, Activity activity){
        this.mainView = mainView;
        this.binding = binding;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void init() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 2);
        binding.AuctionNowView.setLayoutManager(linearLayoutManager);
        auctionNowAdapter = new AuctionNowAdapter();
        binding.AuctionNowView.setAdapter(auctionNowAdapter);

        bestItemDataList = new ArrayList<>();
        bestItemAdapter = new BestItemAdapter(context, bestItemDataList);
        binding.bestItemViewPager.setAdapter(bestItemAdapter);

        auctionNowAdapter.setOnItemClickListener(new AuctionNowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (Constants.userId != null) {
                    Intent intent = new Intent(context, ItemDetail.class);
                    intent.putExtra("itemId", auctionNowAdapter.getAuctionNowData().get(position).getItemId());
                    context.startActivity(intent);
                } else {
                    mainView.showToast(MypageConstants.ELogin.afterLogin.getText());
                }
            }
        });
        setBestItemAnimation();
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
            // best item animation
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                @Override
                public void run() {
                    if(currentPage == response.body().size()) {
                        currentPage = 0;
                    }
                    binding.bestItemViewPager.setCurrentItem(currentPage++, true);
                    setBestItemAnimation();
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() { handler.post(Update); }
                }, DELAY_MS, PERIOD_MS);

            for(int i=0; i<response.body().size(); i++){
                btName = response.body().get(i).getItemName();
                btTime = response.body().get(i).getBuyDate().getYear()+ HomeConstants.EDate.dpYear.getText() + " " +
                        response.body().get(i).getBuyDate().getMonth().getValue()+HomeConstants.EDate.dpMonth.getText();
                btTempMax = response.body().get(i).getMaximumPrice();
                String itemPriceStr = myFormatter.format(btTempMax);

                if(response.body().get(i).getFileNames().size()!=0) {
                    btImage = response.body().get(i).getFileNames().get(0);
                } else{
                    btImage = null;
                }
                bestItem = new BestItem(btImage, btName, btTime, itemPriceStr);
                bestItemDataList.add(bestItem);
                bestItemAdapter.notifyDataSetChanged();
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<List<BestItemResponse>> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), activity);
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
                initPrice = response.body().getData().get(i).getInitPrice();
                maxPrice = response.body().getData().get(i).getMaximumPrice();
                int dif = maxPrice - initPrice;

                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    date = days + "일 " + hours + "시간 " + minutes + "분 전";
                } else if(Integer.parseInt(hours) < 0 || Integer.parseInt(minutes) < 0) {
                    date = "경매 시간 종료";
                } else
                    date = hours + "시간 " + minutes + "분 전";
                itemInfo = response.body().getData().get(i).getDescription();
                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    imageURL = response.body().getData().get(i).getFileNames().get(0);
                } else{
                    imageURL = null;
                }
                data = new AuctionNow(itemId, imageURL, itemName, dif, date, itemInfo);
                auctionDataList.add(data);
                auctionNowAdapter.addItem(data);
                auctionNowAdapter.notifyDataSetChanged();
                setAuctionItemAnimation();
            }
            Log.d(TAG, HomeConstants.EHomeCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), activity);
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

