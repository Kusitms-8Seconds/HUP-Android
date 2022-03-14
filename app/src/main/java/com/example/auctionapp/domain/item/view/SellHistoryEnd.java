package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.adapter.SellHistoryEndAdapter;
import com.example.auctionapp.domain.item.dto.GetAllItemsByStatusRequest;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.SellHistoryEndData;
import com.example.auctionapp.domain.pricesuggestion.dto.BidderResponse;
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

public class SellHistoryEnd extends Fragment {
    ViewGroup viewGroup;

    SellHistoryEndAdapter adapter;
    SellHistoryEndData data;
    List<SellHistoryEndData> sellHistoryEndDataList;
    int bidderCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_sell_history_end, container, false);

        init();
        getData();

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init(){
        RecyclerView recyclerView = viewGroup.findViewById(R.id.sellHistoryEndRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SellHistoryEndAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SellHistoryEndAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), ItemDetail.class);
                intent.putExtra("itemId", adapter.getListData().get(position).getItemId());
                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
    private void getData(){
        bidderCount = 0;
        sellHistoryEndDataList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllItemsByUserIdAndStatus(GetAllItemsByStatusRequest.of(Constants.userId, ItemConstants.EItemSoldStatus.eSoldOut))
                .enqueue(MainRetrofitTool.getCallback(new getAllItemsByUserIdAndStatusCallback()));
    }

    private class getAllItemsByUserIdAndStatusCallback implements MainRetrofitCallback<PaginationDto<List<ItemDetailsResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                Long itemId = response.body().getData().get(i).getId();
                String itemName = response.body().getData().get(i).getItemName();
                int suggestionPrice = response.body().getData().get(i).getSoldPrice();

                if (response.body().getData().get(i).getFileNames().size() != 0) {
                    String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                    data = new SellHistoryEndData(itemId,
                            fileNameMajor,
                            itemName,
                            suggestionPrice,
                            null);
                } else {
                    data = new SellHistoryEndData(itemId,
                            null,
                            itemName,
                            suggestionPrice,
                            null);
                }
                sellHistoryEndDataList.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getBidder(itemId)
                        .enqueue(MainRetrofitTool.getCallback(new getBidderCallback()));
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

    private class getBidderCallback implements MainRetrofitCallback<BidderResponse> {

        @Override
        public void onSuccessResponse(Response<BidderResponse> response) throws IOException {

            sellHistoryEndDataList.get(bidderCount).setBidderName(response.body().getBidderName());
            adapter.addItem(sellHistoryEndDataList.get(bidderCount));
            adapter.notifyDataSetChanged();
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            bidderCount++;
        }
        @Override
        public void onFailResponse(Response<BidderResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }


}


