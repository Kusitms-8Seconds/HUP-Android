package com.example.auctionapp.domain.item.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityItemlistBinding;
import com.example.auctionapp.domain.item.adapter.ItemDataAdapter;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.ItemData;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.item.view.ItemList;
import com.example.auctionapp.domain.item.view.ItemListView;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapCountResponse;
import com.example.auctionapp.domain.upload.view.UploadPage;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;
import com.example.auctionapp.global.util.GetCategory;

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
    ItemDataAdapter adapter;
    ItemData data;
    ArrayList<ItemData> itemDataList;
    int heartCount;
    int participantCount;

    // itemData Attributes
    Long itemId;
    String imageURL;
    String itemName;
    int itemPrice;
    String endTime;
    int views;
    Long heart;

    static final String[] LIST_MENU = {"디지털 기기", "생활가전", "가구/인테리어", "유아동", "생활/가공식품"
            ,"유아도서", "스포츠/레저", "여성잡화", "여성의류", "남성패션/잡화", "게임/취미", "뷰티/미용",
            "반려동물용품", "도서/티켓/음반", "식물"} ;

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
        itemDataList = new ArrayList<>();
        adapter = new ItemDataAdapter(activity, itemDataList);
        binding.itemListLv.setAdapter(adapter);

        itemDataList.clear();
        heartCount = 0;
        participantCount = 0;

        binding.itemListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Constants.userId != null) {
                    Intent intent = new Intent(activity, ItemDetail.class);
                    intent.putExtra("itemId", itemDataList.get(position).getItemId());
                    activity.startActivity(intent);
                } else { itemListView.showToast(MypageConstants.ELogin.afterLogin.getText()); }
            }
        });
        binding.searchfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
                dlg.setTitle("카테고리 선택"); //제목
                dlg.setIcon(R.drawable.filterp); // 아이콘 설정

                dlg.setItems(LIST_MENU, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = LIST_MENU[which];
                        binding.tvFilter.setText(category);

                        itemDataList = new ArrayList<>();
                        adapter = new ItemDataAdapter(activity, itemDataList);
                        binding.itemListLv.setAdapter(adapter);
                        itemDataList.clear();
                        heartCount = 0;
                        participantCount = 0;

                        GetCategory getCategory = new GetCategory(category);
                        String eCategory = getCategory.getCategory();
                        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllItemsByCategory(eCategory)
                                .enqueue(MainRetrofitTool.getCallback(new getAllItemsByCategoryCallback()));
                    }
                });
                dlg.show();
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getData() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllItemsInfo(ItemConstants.EItemSoldStatus.eOnGoing)
                .enqueue(MainRetrofitTool.getCallback(new getAllItemsInfoCallback()));
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
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);

                itemId = response.body().getData().get(i).getId();
                itemName = response.body().getData().get(i).getItemName();
                itemPrice = response.body().getData().get(i).getInitPrice();
                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    endTime = days + "일 " + hours + "시간 " + minutes + "분";
                } else if(Integer.parseInt(hours) < 0) {
                    endTime = "";
                } else
                    endTime = hours + "시간 " + minutes + "분";
                views = 0;
                heart = null;
                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    imageURL = response.body().getData().get(i).getFileNames().get(0);
                } else {
                    imageURL = null;
                }
                data = new ItemData(itemId, imageURL, itemName, itemPrice, endTime, views, heart);
                itemDataList.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getHeart(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getHeartCallback()));
                RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getParticipants(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));
                adapter.notifyDataSetChanged();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            itemListView.showToast(errorMessageParser.getParsedErrorMessage());
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
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            itemListView.showToast(errorMessageParser.getParsedErrorMessage());
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
//            adapter.addItem(itemDataList.get(participantCount));
            adapter.notifyDataSetChanged();
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
            participantCount++;
        }
        @Override
        public void onFailResponse(Response<ParticipantsResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            itemListView.showToast(errorMessageParser.getParsedErrorMessage());
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
    private class getAllItemsByCategoryCallback implements MainRetrofitCallback<PaginationDto<List<ItemDetailsResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                LocalDateTime startDateTime = LocalDateTime.now();
                LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);

                itemId = response.body().getData().get(i).getId();
                itemName = response.body().getData().get(i).getItemName();
                itemPrice = response.body().getData().get(i).getInitPrice();
                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    endTime = days + "일 " + hours + "시간 " + minutes + "분";
                } else
                    endTime = hours + "시간 " + minutes + "분";
                views = 0;
                heart = null;
                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    imageURL = response.body().getData().get(i).getFileNames().get(0);
                } else{
                    imageURL = null;
                }
                data = new ItemData(itemId, imageURL, itemName, itemPrice, endTime, views, heart);
                itemDataList.add(data);
                RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getHeart(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getHeartCallback()));
                RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getParticipants(response.body().getData().get(i).getId())
                        .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));
                adapter.notifyDataSetChanged();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            itemListView.showToast(errorMessageParser.getParsedErrorMessage());
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
