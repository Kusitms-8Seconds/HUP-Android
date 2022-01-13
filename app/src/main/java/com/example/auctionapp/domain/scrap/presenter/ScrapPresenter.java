package com.example.auctionapp.domain.scrap.presenter;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.scrap.adapter.ScrapAdapter;
import com.example.auctionapp.domain.scrap.dto.ScrapDetailsResponse;
import com.example.auctionapp.domain.scrap.model.ScrapItem;
import com.example.auctionapp.domain.scrap.view.Scrap;
import com.example.auctionapp.domain.scrap.view.ScrapView;
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

public class ScrapPresenter implements Presenter{
    private final ScrapView view;
    private final ScrapItem model;

    ScrapAdapter adapter;
    ScrapItem data;
    List<ScrapItem> scrapItemsList = new ArrayList<>();
    int maximumPriceCount;

    public ScrapPresenter(ScrapView view){
        this.view = view;
        this.model = new ScrapItem();
    }

    @Override
    public void getData() {
        scrapItemsList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllScrapsByUserId(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getAllScrapsCallback()));
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
