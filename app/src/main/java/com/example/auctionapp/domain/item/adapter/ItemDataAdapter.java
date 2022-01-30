package com.example.auctionapp.domain.item.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.model.ItemData;
import com.example.auctionapp.domain.user.constant.Constants;

import java.util.ArrayList;

import lombok.Getter;

public @Getter
class ItemDataAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<ItemData> data;

    public ItemDataAdapter(Context context, ArrayList<ItemData> dataArray){
        mContext = context;
        data = dataArray;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ItemData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.custom_itemlist, null);

        ImageView item_image;
        TextView item_name;
        TextView item_price;
        TextView end_time;
        TextView item_views;
        TextView item_hearts;

        item_image = view.findViewById(R.id.sell_history_ongoing_img);
        item_name = view.findViewById(R.id.bt_item_name);
        item_price = view.findViewById(R.id.item_price);
        end_time = view.findViewById(R.id.end_time);
        item_views = view.findViewById(R.id.item_views);
        item_hearts = view.findViewById(R.id.item_hearts);

        Glide.with(mContext).load(Constants.imageBaseUrl+data.get(position).getImageURL()).override(item_image.getWidth()
                    ,item_image.getHeight()).into(item_image);
            item_image.setClipToOutline(true);  //item 테두리
            item_name.setText(data.get(position).getItemName());
            item_price.setText(data.get(position).getItemPrice()+"");
            end_time.setText(data.get(position).getEndTime());
            item_views.setText(data.get(position).getViews()+"");
            item_hearts.setText(data.get(position).getHeart()+"");

        return view;
    }
}