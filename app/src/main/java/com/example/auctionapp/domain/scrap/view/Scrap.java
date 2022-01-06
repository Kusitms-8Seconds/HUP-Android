package com.example.auctionapp.domain.scrap.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.home.view.Home;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.view.BestItemAdapter;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.view.AuctionNow;
import com.example.auctionapp.domain.scrap.dto.ScrapDetailsResponse;
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

public class Scrap extends AppCompatActivity {

    ScrapAdapter adapter;
    RecyclerView recyclerView;
    ScrapItem data;
    List<ScrapItem> scrapItemsList = new ArrayList<>();
    int maximumPriceCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);

        init();
        getData();

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.bringToFront();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void init(){
        recyclerView = findViewById(R.id.scrapRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ScrapAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ScrapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ItemDetail.class);
                intent.putExtra("itemId", scrapItemsList.get(position).getItemId());
                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getApplicationContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    private void getData(){
        scrapItemsList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllScrapsByUserId(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getAllScrapsCallback()));

//        setAnimation();
    }
    public void setAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(200, 0, 200, 0);
        Animation alphaAnimation = new AlphaAnimation(0, 1);
        translateAnimation.setDuration(500);
        alphaAnimation.setDuration(1300);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(translateAnimation);
        animation.addAnimation(alphaAnimation);
        recyclerView.setAnimation(animation);


//        Animation animation = new AlphaAnimation(0, 1);
//        animation.setDuration(1500);
//        recyclerView.setAnimation(animation);
    }

    private class getAllScrapsCallback implements MainRetrofitCallback<PaginationDto<List<ScrapDetailsResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ScrapDetailsResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                LocalDateTime startDateTime = LocalDateTime.now();
                LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));
                Long itemId = response.body().getData().get(i).getItemId();
                String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                String itemName = response.body().getData().get(i).getItemName();
                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    data = new ScrapItem(itemId,
                            fileNameMajor,
                            itemName,
                            0,
                            minutes+"분");
                } else{
                    data = new ScrapItem(itemId,
                            null,
                            itemName,
                            0,
                            minutes+"분");
                }
                scrapItemsList.add(data);
                System.out.println("itemId"+itemId);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getMaximumPrice(itemId)
                        .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceCallback()));
//                setAnimation();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ScrapDetailsResponse>>> response) throws IOException, JSONException {
//            System.out.println("errorBody"+response.errorBody().string());
//            try {
//                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getApplicationContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
//            } catch (Exception e) { Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
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

            scrapItemsList.get(maximumPriceCount).setItemPrice(response.body().getMaximumPrice());
            adapter.addItem(scrapItemsList.get(maximumPriceCount));
            adapter.notifyDataSetChanged();
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            maximumPriceCount++;
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
//            System.out.println("errorBody"+response.errorBody().string());
//            try {
//                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(getApplicationContext(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
//            } catch (Exception e) { Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
