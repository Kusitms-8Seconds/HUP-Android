package com.example.auctionapp.domain.item.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.GetAllItemsByStatusRequest;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
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

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
class SellHistoryOngoingData {
    Long itemId;
    String imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int maxPrice;
    String leftTime;

    public SellHistoryOngoingData(Long itemId, String imageURL, String itemName, int maxPrice, String leftTime) {
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.maxPrice = maxPrice;
        this.leftTime = leftTime;

    }

}
class SellHistoryOngoingAdapter extends BaseAdapter {
    ArrayList<com.example.auctionapp.domain.item.view.SellHistoryOngoingData> items =
            new ArrayList<com.example.auctionapp.domain.item.view.SellHistoryOngoingData>();
    Context context;


    public void addItem(com.example.auctionapp.domain.item.view.SellHistoryOngoingData item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        com.example.auctionapp.domain.item.view.SellHistoryOngoingData category = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_sell_history_ongoing_recyclerview, parent, false);
        }

        ImageView auc_history_ongoing_img = (ImageView) convertView.findViewById(R.id.sell_history_ongoing_img);
        TextView auc_history_ongoing_edt_name = (TextView) convertView.findViewById(R.id.sell_history_ongoing_edt_name);
        TextView auc_history_ongoing_max = (TextView) convertView.findViewById(R.id.sell_history_ongoing_max);
        TextView itemLeftTime = (TextView) convertView.findViewById(R.id.itemLeftTime);

        Glide.with(context).load(Constants.imageBaseUrl+items.get(position).getImageURL()).override(auc_history_ongoing_img.getWidth()
                ,auc_history_ongoing_img.getHeight()).into(auc_history_ongoing_img);
        auc_history_ongoing_img.setClipToOutline(true);  //item 테두리
        auc_history_ongoing_edt_name.setText(items.get(position).getItemName());
        auc_history_ongoing_max.setText(items.get(position).getMaxPrice()+"");
        itemLeftTime.setText(items.get(position).getLeftTime());


        return convertView;
    }
}