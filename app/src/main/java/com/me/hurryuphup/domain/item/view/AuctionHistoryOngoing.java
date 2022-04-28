package com.me.hurryuphup.domain.item.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.item.adapter.AuctionHistoryOngoingAdapter;
import com.me.hurryuphup.domain.item.constant.ItemConstants;
import com.me.hurryuphup.domain.item.model.AuctionHistoryOngoingData;
import com.me.hurryuphup.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.me.hurryuphup.domain.pricesuggestion.dto.PriceSuggestionListResponse;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.global.dto.PaginationDto;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AuctionHistoryOngoing extends Fragment {

    ViewGroup viewGroup;
    AuctionHistoryOngoingAdapter adapter;
    AuctionHistoryOngoingData data;
    List<AuctionHistoryOngoingData> auctionHistoryOngoingDataList;
    int maximumPriceCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_auction_history_ongoing, container, false);
        auctionHistoryOngoingDataList = new ArrayList<>();
        init();
        getData();

        return viewGroup;
    }
    
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init(){
        GridView gridView = (GridView) viewGroup.findViewById(R.id.auctionHistoryOngoingRecyclerView);
        adapter = new AuctionHistoryOngoingAdapter();
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AuctionHistoryOngoingData item = (AuctionHistoryOngoingData) adapter.getItem(position);
                Intent intent = new Intent(view.getContext(), ItemDetail.class);
                intent.putExtra("itemId", item.getItemId());
                startActivity(intent);
            }
        });
    }
    private void getData(){
        maximumPriceCount = 0;
        auctionHistoryOngoingDataList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllPriceSuggestionByUserId(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getAllPriceSuggestionByUserIdCallback()));
    }

    private class getAllPriceSuggestionByUserIdCallback implements MainRetrofitCallback<PaginationDto<List<PriceSuggestionListResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) {
            if(response.body().getData().size() == 0)
                Toast.makeText(getActivity(), ItemConstants.EItemServiceImpl.eNotPriceSuggestionContentExceptionMessage.getValue(), Toast.LENGTH_SHORT).show();
            for(int i=0; i<response.body().getData().size(); i++){
                if(response.body().getData().get(i).isAcceptState()==false) {
                    LocalDateTime startDateTime = LocalDateTime.now();
                    LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                    String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                    String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                    String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);
                    Long itemId = response.body().getData().get(i).getItemId();
                    String itemName = response.body().getData().get(i).getItemName();
                    int suggestionPrice = response.body().getData().get(i).getSuggestionPrice();

                    String date = "";
                    if(Integer.parseInt(hours) >= 24) {
                        hours = String.valueOf(Integer.parseInt(hours)%24);
                        date = days + "일 " + hours + "시간 " + minutes + "분";
                    } else if(Integer.parseInt(hours) < 0 || Integer.parseInt(minutes) < 0) {
                        date = "";
                    }
                    else
                        date = hours + "시간 " + minutes + "분";

                    if (response.body().getData().get(i).getFileNames().size() != 0) {
                        String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                        data = new AuctionHistoryOngoingData(itemId,
                                fileNameMajor,
                                itemName,
                                suggestionPrice,
                                0,
                                date);
                    } else {
                        data = new AuctionHistoryOngoingData(itemId,
                                null,
                                itemName,
                                suggestionPrice,
                                0,
                                date);
                    }
                    auctionHistoryOngoingDataList.add(data);
                    adapter.addItem(data);
                    adapter.notifyDataSetChanged();
                }
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
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

            auctionHistoryOngoingDataList.get(maximumPriceCount).setMaxPrice(response.body().getMaximumPrice());
            adapter.addItem(auctionHistoryOngoingDataList.get(maximumPriceCount));
            adapter.notifyDataSetChanged();
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            maximumPriceCount++;
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}

