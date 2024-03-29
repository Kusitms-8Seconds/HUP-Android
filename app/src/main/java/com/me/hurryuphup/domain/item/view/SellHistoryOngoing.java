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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.item.constant.ItemConstants;
import com.me.hurryuphup.domain.item.adapter.SellHistoryOngoingAdapter;
import com.me.hurryuphup.domain.item.dto.GetAllItemsByStatusRequest;
import com.me.hurryuphup.domain.item.dto.ItemDetailsResponse;
import com.me.hurryuphup.domain.item.model.SellHistoryOngoingData;
import com.me.hurryuphup.domain.pricesuggestion.view.BidPage;
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
                SellHistoryOngoingData item = (SellHistoryOngoingData) adapter.getItem(position);
                Intent intent = new Intent(view.getContext(), BidPage.class);
                intent.putExtra("itemId", item.getItemId());
                startActivity(intent);
            }
        });
    }
    private void getData(){
        //일단 레이아웃만
        sellHistoryOngoingDataList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllItemsByUserIdAndStatus(GetAllItemsByStatusRequest.of(Constants.userId,
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
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);
                Long itemId = response.body().getData().get(i).getId();
                String itemName = response.body().getData().get(i).getItemName();
                int suggestionPrice = response.body().getData().get(i).getInitPrice();

                String date = "";
                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    date = days + "일 " + hours + "시간 " + minutes + "분";
                } else if(Integer.parseInt(hours) < 0 || Integer.parseInt(minutes) < 0) {
                    date = "";
                } else
                    date = hours + "시간 " + minutes + "분";

                if (response.body().getData().get(i).getFileNames().size() != 0) {
                    String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                    data = new SellHistoryOngoingData(itemId,
                            fileNameMajor,
                            itemName,
                            suggestionPrice,
                            date);
                } else {
                    data = new SellHistoryOngoingData(itemId,
                            null,
                            itemName,
                            suggestionPrice,
                            date);
                }
                adapter.addItem(data);adapter.notifyDataSetChanged();
                sellHistoryOngoingDataList.add(data);
                }
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

