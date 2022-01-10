package com.example.auctionapp.domain.item.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.model.SellHistoryOngoingData;
import com.example.auctionapp.domain.user.constant.Constants;

import java.util.ArrayList;

public class SellHistoryOngoingAdapter extends BaseAdapter {
    ArrayList<SellHistoryOngoingData> items =
            new ArrayList<SellHistoryOngoingData>();
    Context context;


    public void addItem(SellHistoryOngoingData item) {
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
        SellHistoryOngoingData category = items.get(position);

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