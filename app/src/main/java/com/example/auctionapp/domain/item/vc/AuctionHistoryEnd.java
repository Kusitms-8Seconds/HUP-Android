package com.example.auctionapp.domain.item.vc;

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
import com.example.auctionapp.domain.item.adapter.AuctionHistoryEndAdapter;
import com.example.auctionapp.domain.item.model.AuctionHistoryEndData;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionListResponse;
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

public class AuctionHistoryEnd extends Fragment {

    ViewGroup viewGroup;
    AuctionHistoryEndAdapter adapter;
    AuctionHistoryEndData data;
    List<AuctionHistoryEndData> auctionHistoryEndDataList = new ArrayList<>();
    int maximumPriceCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_auction_history_end, container, false);

        init();
        getData();

        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init(){
        RecyclerView recyclerView = viewGroup.findViewById(R.id.auctionHistoryEndRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AuctionHistoryEndAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AuctionHistoryEndAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                Intent intent = new Intent(getContext(), ItemDetail.class);
//                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
    private void getData(){
        //일단 레이아웃만
//        AuctionHistoryEndData data = new AuctionHistoryEndData(R.drawable.rectangle, "스타벅스 리유저블 컵", 70000, "뽀로로");
//        adapter.addItem(data);
//        data = new AuctionHistoryEndData(R.drawable.rectangle, "스타벅스 리유저블 컵", 70000, "뽀로로");
//        adapter.addItem(data);
        maximumPriceCount = 0;
        auctionHistoryEndDataList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllPriceSuggestionByUserId(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getAllPriceSuggestionByUserIdCallback()));
    }

    private class getAllPriceSuggestionByUserIdCallback implements MainRetrofitCallback<PaginationDto<List<PriceSuggestionListResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                if(response.body().getData().get(i).isAcceptState()==true) {
                    LocalDateTime startDateTime = LocalDateTime.now();
                    LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                    String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                    String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                    String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));
                    Long itemId = response.body().getData().get(i).getItemId();
                    String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                    String itemName = response.body().getData().get(i).getItemName();
                    int suggestionPrice = response.body().getData().get(i).getSuggestionPrice();
                    String userName = response.body().getData().get(i).getUserName();
                    if (response.body().getData().get(i).getFileNames().size() != 0) {
                        data = new AuctionHistoryEndData(itemId,
                                fileNameMajor,
                                itemName,
                                suggestionPrice,
                                userName);
                    } else {
                        data = new AuctionHistoryEndData(itemId,
                                fileNameMajor,
                                itemName,
                                suggestionPrice,
                                userName);
                    }
                    auctionHistoryEndDataList.add(data);
                }
//                setAnimation();
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

}

