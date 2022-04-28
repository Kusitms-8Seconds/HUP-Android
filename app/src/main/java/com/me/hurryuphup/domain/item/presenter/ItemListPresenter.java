package com.me.hurryuphup.domain.item.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.RequiresApi;

import com.me.hurryuphup.R;
import com.me.hurryuphup.databinding.ActivityItemlistBinding;
import com.me.hurryuphup.domain.item.adapter.ItemDataAdapter;
import com.me.hurryuphup.domain.item.constant.ItemConstants;
import com.me.hurryuphup.domain.item.dto.ItemDetailsResponse;
import com.me.hurryuphup.domain.item.model.ItemData;
import com.me.hurryuphup.domain.item.view.ItemDetail;
import com.me.hurryuphup.domain.item.view.ItemListView;
import com.me.hurryuphup.domain.mypage.constant.MypageConstants;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.global.dto.PaginationDto;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;
import com.me.hurryuphup.global.util.ErrorMessageParser;
import com.me.hurryuphup.global.util.GetCategory;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
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
    DecimalFormat myFormatter = new DecimalFormat("###,###");
    ErrorMessageParser errorMessageParser;

    // itemData Attributes
    Long itemId;
    String imageURL;
    String itemName;
    int itemPrice;
    String endTime;
    int views;
    int heart;

    static final String[] LIST_MENU = {"전체", "디지털 기기", "생활가전", "가구/인테리어", "유아동", "생활/가공식품"
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

                        if(category.equals(LIST_MENU[0])) {
                            init();
                            getData();
                        }
                        else {
                            GetCategory getCategory = new GetCategory(category);
                            String eCategory = getCategory.getCategory();
                            RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllItemsByCategory(eCategory)
                                    .enqueue(MainRetrofitTool.getCallback(new getAllItemsByCategoryCallback()));
                        }
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
                String itemPriceStr = "";
                itemPrice = response.body().getData().get(i).getMaximumPrice();
                if(itemPrice != 0) {
                    itemPriceStr = myFormatter.format(itemPrice);
                } else {
                    itemPrice = response.body().getData().get(i).getInitPrice();
                    itemPriceStr = myFormatter.format(itemPrice);
                }
                views = response.body().getData().get(i).getParticipants();
                heart = response.body().getData().get(i).getScrapCount();
                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    endTime = days + "일 " + hours + "시간 " + minutes + "분";
                } else if(Integer.parseInt(hours) < 0 || Integer.parseInt(minutes) < 0) {
                    endTime = "";
                } else
                    endTime = hours + "시간 " + minutes + "분";
                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    imageURL = response.body().getData().get(i).getFileNames().get(0);
                } else {
                    imageURL = null;
                }
                data = new ItemData(itemId, imageURL, itemName, itemPriceStr, endTime, views, heart);
                itemDataList.add(data);
                adapter.notifyDataSetChanged();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), activity);
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
                itemPrice = response.body().getData().get(i).getMaximumPrice();
                String itemPriceStr = myFormatter.format(itemPrice);
                views = response.body().getData().get(i).getParticipants();
                heart = response.body().getData().get(i).getScrapCount();
                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    endTime = days + "일 " + hours + "시간 " + minutes + "분";
                } else
                    endTime = hours + "시간 " + minutes + "분";
                if(response.body().getData().get(i).getFileNames().size()!=0) {
                    imageURL = response.body().getData().get(i).getFileNames().get(0);
                } else{
                    imageURL = null;
                }
                data = new ItemData(itemId, imageURL, itemName, itemPriceStr, endTime, views, heart);
                itemDataList.add(data);
                adapter.notifyDataSetChanged();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), activity);
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
