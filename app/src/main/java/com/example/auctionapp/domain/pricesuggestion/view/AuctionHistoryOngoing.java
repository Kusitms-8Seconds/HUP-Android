package com.example.auctionapp.domain.pricesuggestion.view;

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
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionListResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapDetailsResponse;
import com.example.auctionapp.domain.scrap.view.Scrap;
import com.example.auctionapp.domain.scrap.view.ScrapItem;
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

import lombok.Getter;
import lombok.Setter;
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
//                AuctionHistoryOngoingData item = (AuctionHistoryOngoingData) adapter.getItem(position);
            }
        });
    }
    private void getData(){
        //일단 레이아웃만
//        AuctionHistoryOngoingData data = new AuctionHistoryOngoingData(R.drawable.rectangle, "맥북 에어 M1 실버", 200000, 270000, "12:21");
//        adapter.addItem(data);
//        data = new AuctionHistoryOngoingData(R.drawable.rectangle, "맥북 에어 M1 실버", 200000, 270000, "12:21");
//        adapter.addItem(data);
        maximumPriceCount = 0;
        auctionHistoryOngoingDataList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllPriceSuggestionByUserId(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getAllPriceSuggestionByUserIdCallback()));
    }

    private class getAllPriceSuggestionByUserIdCallback implements MainRetrofitCallback<PaginationDto<List<PriceSuggestionListResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                if(response.body().getData().get(i).isAcceptState()==false) {
                    LocalDateTime startDateTime = LocalDateTime.now();
                    LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                    String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                    String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                    String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));
                    Long itemId = response.body().getData().get(i).getItemId();
                    String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                    String itemName = response.body().getData().get(i).getItemName();
                    int suggestionPrice = response.body().getData().get(i).getSuggestionPrice();
                    if (response.body().getData().get(i).getFileNames().size() != 0) {
                        data = new AuctionHistoryOngoingData(itemId,
                                fileNameMajor,
                                itemName,
                                suggestionPrice,
                                0,
                                minutes + "분");
                    } else {
                        data = new AuctionHistoryOngoingData(itemId,
                                null,
                                itemName,
                                suggestionPrice,
                                0,
                                minutes + "분");
                    }
                    auctionHistoryOngoingDataList.add(data);
                    RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getMaximumPrice(itemId)
                            .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceCallback()));
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
@Getter
@Setter
class AuctionHistoryOngoingData {
    Long itemId;
    String imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int myPrice;
    int maxPrice;
    String leftTime;

    public AuctionHistoryOngoingData(Long itemId, String imageURL, String itemName, int myPrice, int maxPrice, String leftTime) {
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.myPrice = myPrice;
        this.maxPrice = maxPrice;
        this.leftTime = leftTime;

    }

}
class AuctionHistoryOngoingAdapter extends BaseAdapter {
    ArrayList<com.example.auctionapp.domain.pricesuggestion.view.AuctionHistoryOngoingData> items =
            new ArrayList<com.example.auctionapp.domain.pricesuggestion.view.AuctionHistoryOngoingData>();
    Context context;

    public void addItem(com.example.auctionapp.domain.pricesuggestion.view.AuctionHistoryOngoingData item) {
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
        com.example.auctionapp.domain.pricesuggestion.view.AuctionHistoryOngoingData category = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_auction_history_ongoing_recyclerview, parent, false);
        }

        ImageView auc_history_ongoing_img = (ImageView) convertView.findViewById(R.id.auc_history_ongoing_img);
        TextView auc_history_ongoing_edt_name = (TextView) convertView.findViewById(R.id.auc_history_ongoing_edt_name);
        TextView auc_history_ongoing_myPrice = (TextView) convertView.findViewById(R.id.auc_history_ongoing_myPrice);
        TextView auc_history_ongoing_max = (TextView) convertView.findViewById(R.id.auc_history_ongoing_max);
        TextView itemLeftTime = (TextView) convertView.findViewById(R.id.itemLeftTime);

        //auc_history_ongoing_img.setImageResource(items.get(position).getImage());

        Glide.with(context).load(Constants.imageBaseUrl+items.get(position).getImageURL()).override(auc_history_ongoing_img.getWidth()
                ,auc_history_ongoing_img.getHeight()).into(auc_history_ongoing_img);
        auc_history_ongoing_img.setClipToOutline(true);  //item 테두리
        auc_history_ongoing_edt_name.setText(items.get(position).getItemName());
        auc_history_ongoing_myPrice.setText(items.get(position).getMyPrice()+"");
        auc_history_ongoing_max.setText(items.get(position).getMaxPrice()+"");
        itemLeftTime.setText(items.get(position).getLeftTime());


        return convertView;
    }
}

