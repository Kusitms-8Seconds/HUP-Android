package com.example.auctionapp.domain.pricesuggestion.view;

import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;

import java.util.ArrayList;

public class AuctionHistoryOngoing extends Fragment {

    ViewGroup viewGroup;
    AuctionHistoryOngoingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_auction_history_ongoing, container, false);

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
        AuctionHistoryOngoingData data = new AuctionHistoryOngoingData(R.drawable.rectangle, "맥북 에어 M1 실버", 200000, 270000, "12:21");
        adapter.addItem(data);
        data = new AuctionHistoryOngoingData(R.drawable.rectangle, "맥북 에어 M1 실버", 200000, 270000, "12:21");
        adapter.addItem(data);
    }
}
class AuctionHistoryOngoingData {
    int imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int myPrice;
    int maxPrice;
    String leftTime;

    public AuctionHistoryOngoingData(int imageURL, String itemName, int myPrice, int maxPrice, String leftTime) {
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.myPrice = myPrice;
        this.maxPrice = maxPrice;
        this.leftTime = leftTime;

    }

    public int getImage() {return imageURL;}
    public String getItemName() {return itemName;}
    public int getMyPrice() {return myPrice;}
    public int getMaxPrice() {return maxPrice;}
    public String getLeftTime() {return leftTime;}

    public void setImage(int imageURL) {this.imageURL = imageURL;}
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setMyPrice(int myPrice) {this.myPrice = myPrice;}
    public void setMaxPrice(int maxPrice) {this.maxPrice = maxPrice;}
    public void setLeftTime(String leftTime) {this.leftTime = leftTime;}
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

        auc_history_ongoing_img.setImageResource(items.get(position).getImage());
        auc_history_ongoing_img.setClipToOutline(true);  //item 테두리
        auc_history_ongoing_edt_name.setText(items.get(position).getItemName());
        auc_history_ongoing_myPrice.setText(items.get(position).getMyPrice()+"");
        auc_history_ongoing_max.setText(items.get(position).getMaxPrice()+"");
        itemLeftTime.setText(items.get(position).getLeftTime());


        return convertView;
    }
}

