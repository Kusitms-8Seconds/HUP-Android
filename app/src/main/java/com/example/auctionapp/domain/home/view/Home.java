package com.example.auctionapp.domain.home.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.BestItemResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.view.BestItem;
import com.example.auctionapp.domain.item.view.BestItemAdapter;
import com.example.auctionapp.domain.item.view.ItemData;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.item.view.ItemList;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.view.AuctionNow;
import com.example.auctionapp.domain.pricesuggestion.view.AuctionNowAdapter;
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
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Home extends Fragment {
    ViewGroup viewGroup;
    private ArrayList<BestItem> bestItemDataList = new ArrayList<>();

    AuctionNowAdapter adapter;
    AuctionNow data;
    BestItem bestItem;
    List<AuctionNow> auctionDataList = new ArrayList<>();
    int heartCount;
    int maximumPriceCount;
    int maximumPriceCount2;
    BestItemAdapter bestItemAdapter;
    ViewPager bestItemViewPager;

    RecyclerView AuctionNowRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);

        init();
        initializeAuctionNowData();
        initializeBestData();

        bestItemViewPager = viewGroup.findViewById(R.id.bestItemViewPager);
        bestItemAdapter = new BestItemAdapter(getContext(), bestItemDataList);
        bestItemViewPager.setAdapter(bestItemAdapter);

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){
        AuctionNowRecyclerView = viewGroup.findViewById(R.id.AuctionNowView);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),2);
        AuctionNowRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AuctionNowAdapter();
        AuctionNowRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AuctionNowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), ItemDetail.class);
                startActivity(intent); }
        });
    }


    public void initializeBestData()
    {
        bestItemDataList = new ArrayList();

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getBestItems(ItemConstants.EItemSoldStatus.eOnGoing)
                .enqueue(MainRetrofitTool.getCallback(new getBestItemsCallback()));

    }
    public void initializeAuctionNowData()
    {

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllItemsInfo(ItemConstants.EItemSoldStatus.eOnGoing)
                .enqueue(MainRetrofitTool.getCallback(new getAllItemsInfoCallback()));
    }

    private class getBestItemsCallback implements MainRetrofitCallback<List<BestItemResponse>> {

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
                RetrofitTool.getAPIWithNullConverter().getMaximumPrice(response.body().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceBestItemCallback()));
                setBestItemAnimation();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<List<BestItemResponse>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
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
                setAuctionItemAnimation();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

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
                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

//    private class getMaximumPriceCallback implements MainRetrofitCallback<MaximumPriceResponse> {
//
//        @Override
//        public void onSuccessResponse(Response<MaximumPriceResponse> response) throws IOException {
//
//            auctionDataList.get(maximumPriceCount).setItemPrice(response.body().getMaximumPrice());
//            adapter.addItem(auctionDataList.get(maximumPriceCount));
//            adapter.notifyDataSetChanged();
//            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
//            maximumPriceCount++;
//        }
//        @Override
//        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
//            System.out.println("errorBody"+response.errorBody().string());
//            try {
//                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
//            } catch (Exception e) { Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
//            Log.d(TAG, "onFailResponse");
//        }
//        @Override
//        public void onConnectionFail(Throwable t) {
//            Log.e("연결실패", t.getMessage());
//        }
//    }

    private class getMaximumPriceBestItemCallback implements MainRetrofitCallback<MaximumPriceResponse> {

        @Override
        public void onSuccessResponse(Response<MaximumPriceResponse> response) throws IOException {

            bestItemDataList.get(maximumPriceCount2).setBtTempMax(response.body().getMaximumPrice());
            bestItemAdapter = new BestItemAdapter(getContext(), bestItemDataList);
            //bestItemAdapter.notifyDataSetChanged();
            bestItemViewPager.setAdapter(bestItemAdapter);
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            maximumPriceCount2++;
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(getContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
    public void setAuctionItemAnimation() {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1500);
        AuctionNowRecyclerView.setAnimation(animation);
    }
    public void setBestItemAnimation() {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1500);
        bestItemViewPager.setAnimation(animation);
    }
}