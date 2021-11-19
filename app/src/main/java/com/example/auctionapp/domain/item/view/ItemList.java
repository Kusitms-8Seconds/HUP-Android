package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.DefaultResponse;
import com.example.auctionapp.domain.item.dto.ImageResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapCountResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.google.android.gms.common.util.IOUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemList extends Fragment {

    ViewGroup viewGroup;
    ItemDataAdapter adapter;
    ItemData data;
    List<ItemData> itemDataList = new ArrayList<>();
    int heartCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_itemlist, container, false);
        init();
        getData();
        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){
        RecyclerView recyclerView = viewGroup.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ItemDataAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ItemDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), ItemDetail.class);
                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ImageView searchView = (ImageView) viewGroup.findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Search.class);
                startActivity(intent);
            }
        });

    }

    private void getData(){
        //일단 레이아웃만
//        ItemData data = new ItemData(R.drawable.rectangle, "아이폰 11 프로 256GB", 530000, "14:46", 30, 4);
//        adapter.addItem(data);
//        data = new ItemData(R.drawable.rectangle, "아이폰 11 프로 64GB", 500000, "82:33", 32, 5);
//        adapter.addItem(data);
//        data = new ItemData(R.drawable.rectangle, "아이폰 11 미니 A급 256GB", 420000, "34:07", 23, 1);
//        adapter.addItem(data);
//        data = new ItemData(R.drawable.rectangle, "아이폰 11 미니 256GB 레드 미개봉 중고", 552000, "34:04", 27, 2);
//        adapter.addItem(data);

        if(Constants.token!=null) {
            RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllItemsInfo(ItemConstants.EItemSoldStatus.eOnGoing)
                    .enqueue(MainRetrofitTool.getCallback(new getAllItemsInfoCallback()));
        }


    }

    private class getAllItemsInfoCallback implements MainRetrofitCallback<PaginationDto<List<ItemDetailsResponse>>> {

        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                LocalDateTime startDateTime = LocalDateTime.now();
                LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));

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
                            "14:46", 0, null);
                }
                itemDataList.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getHeart(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getHeartCallback()));
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

            itemDataList.get(heartCount).setHeart(response.body().getHeart());
            adapter.addItem(itemDataList.get(heartCount));
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
    }
