package com.example.auctionapp.domain.item.vc;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.adapter.SellHistoryOngoingAdapter;
import com.example.auctionapp.domain.item.dto.GetAllItemsByStatusRequest;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.SellHistoryOngoingData;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class SellHistoryOngoing extends Fragment {
    ViewGroup viewGroup;
    SellHistoryOngoingAdapter adapter;
    SellHistoryOngoingData data;
    List<SellHistoryOngoingData> sellHistoryOngoingDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_sell_history_ongoing, container, false);

        init();
        getData();

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init(){
        GridView gridView = (GridView) viewGroup.findViewById(R.id.sellHistoryOngoingGridView);
        adapter = new SellHistoryOngoingAdapter();
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AuctionHistoryOngoingData item = (AuctionHistoryOngoingData) adapter.getItem(position);
            }
        });
    }
    private void getData(){
        //일단 레이아웃만
//        SellHistoryOngoingData data = new SellHistoryOngoingData(R.drawable.rectangle, "나이키 데이브레이크", 200000, "12:21");
//        adapter.addItem(data);
//        data = new SellHistoryOngoingData(R.drawable.rectangle, "맥북 에어 M1 실버", 270000, "12:21");
//        adapter.addItem(data);
        sellHistoryOngoingDataList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllItemsByUserIdAndStatus(GetAllItemsByStatusRequest.of(Constants.userId,
                ItemConstants.EItemSoldStatus.eOnGoing))
                .enqueue(MainRetrofitTool.getCallback(new getAllItemsByUserIdAndStatusCallback()));
    }

    private class getAllItemsByUserIdAndStatusCallback implements MainRetrofitCallback<PaginationDto<List<ItemDetailsResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                    LocalDateTime startDateTime = LocalDateTime.now();
                    LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                    String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                    String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                    String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));
                    Long itemId = response.body().getData().get(i).getId();
                    String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                    String itemName = response.body().getData().get(i).getItemName();
                    int suggestionPrice = response.body().getData().get(i).getSoldPrice();
                    if (response.body().getData().get(i).getFileNames().size() != 0) {
                        data = new SellHistoryOngoingData(itemId,
                                fileNameMajor,
                                itemName,
                                suggestionPrice,
                                minutes + "분");
                    } else {
                        data = new SellHistoryOngoingData(itemId,
                                null,
                                itemName,
                                suggestionPrice,
                                minutes + "분");
                    }
                adapter.addItem(data);adapter.notifyDataSetChanged();
                sellHistoryOngoingDataList.add(data);
                }
//                setAnimation();
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            }

        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}

