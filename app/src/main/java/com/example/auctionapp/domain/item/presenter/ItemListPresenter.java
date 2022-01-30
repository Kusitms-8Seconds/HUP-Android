package com.example.auctionapp.domain.item.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.auctionapp.databinding.ActivityItemDetailBinding;
import com.example.auctionapp.databinding.ActivityItemlistBinding;
import com.example.auctionapp.domain.item.adapter.ItemDataAdapter;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.ItemData;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.item.view.ItemListView;
import com.example.auctionapp.domain.item.view.Search;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
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

public class ItemListPresenter implements ItemListPresenterInterface {
    ItemDataAdapter adapter = new ItemDataAdapter();
    ItemData data;
    List<ItemData> itemDataList;
    int heartCount;
    int participantCount;

    // Attributes
    private ItemListView itemListView;
    private ActivityItemlistBinding binding;
    private Activity activity;

    // Constructor
    public ItemListPresenter(ItemListView itemListView, ActivityItemlistBinding binding, Activity activity){
        this.itemListView = itemListView;
        this.binding = binding;
        this.activity = activity;
    }

    @Override
    public void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        itemDataList = new ArrayList<>();
        itemDataList.clear();
        heartCount = 0;
        participantCount = 0;

        adapter.setOnItemClickListener(new ItemDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(activity, ItemDetail.class);
                intent.putExtra("itemId", adapter.getListData().get(position).getItemId());
                activity.startActivity(intent);
            }
        });
        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), new LinearLayoutManager(activity).getOrientation());
        binding.recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void getData() {
        if (Constants.token != null) {
            RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllItemsInfo(ItemConstants.EItemSoldStatus.eOnGoing)
                    .enqueue(MainRetrofitTool.getCallback(new getAllItemsInfoCallback()));
        }
    }

    private class getAllItemsInfoCallback implements MainRetrofitCallback<PaginationDto<List<ItemDetailsResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) {
            itemDataList = new ArrayList<>();
            for(int i=0; i<response.body().getData().size(); i++){
                LocalDateTime startDateTime = LocalDateTime.now();
                LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
//                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
//                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
//                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)/60);

                long tmpMinute = ChronoUnit.MINUTES.between(startDateTime, endDateTime); // 시작시간 ~ 끝나는 시간을 minute으로 환산
//                long tmpMinute = 0;
                long tmpHour = 0;
                long tmpDay = 0;
//
//                tmpMinute = tmp / 60;
//                tmpMinute = tmp % 60;
//                tmp = tmp / 60;
//                tmpHour = tmp % 60

                tmpHour = tmpMinute % 60;
                tmpMinute = tmpMinute / 60;
                tmpDay = tmpMinute % 24;
                tmpMinute = tmpMinute / 24;

                String days = String.valueOf(tmpDay);
                String hours = String.valueOf(tmpHour);
                String minutes = String.valueOf(tmpMinute);

                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    data = new ItemData(response.body().getData().get(i).getId(),
                            response.body().getData().get(i).getFileNames().get(0),
                            response.body().getData().get(i).getItemName(),
                            response.body().getData().get(i).getInitPrice(),
                            days+"일 "+hours+"시간 "+minutes+"분", 0, null);
                } else{
                    data = new ItemData(response.body().getData().get(i).getId(),
                            null,
                            response.body().getData().get(i).getItemName(),
                            response.body().getData().get(i).getInitPrice(),
                            days+"일 "+hours+"시간 "+minutes+"분", 0, null);
                }
                itemDataList.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getHeart(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getHeartCallback()));
                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getParticipants(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));
                adapter.notifyDataSetChanged();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(activity, jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show(); }
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

            itemDataList.get(heartCount).setHeart(response.body().getHeart());
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            heartCount++;
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onFailResponse(Response<ScrapCountResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(activity, jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show(); }
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

            itemDataList.get(participantCount).setViews(response.body().getParticipantsCount());
            adapter.addItem(itemDataList.get(participantCount));
            adapter.notifyDataSetChanged();
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            participantCount++;
        }
        @Override
        public void onFailResponse(Response<ParticipantsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(activity, jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) { Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
