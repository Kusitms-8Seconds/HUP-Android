package com.example.auctionapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends  RecyclerView.ViewHolder {

    ImageView item_image;
    TextView item_name;
    TextView item_price;
    TextView end_time;
    TextView item_views;
    TextView item_hearts;



    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        item_image = itemView.findViewById(R.id.item_image);
        item_name = itemView.findViewById(R.id.item_name);
        item_price = itemView.findViewById(R.id.item_price);
        end_time = itemView.findViewById(R.id.end_time);
        item_views = itemView.findViewById(R.id.item_views);
        item_hearts = itemView.findViewById(R.id.item_hearts);

    }

    public void onBind(ItemData data){
        item_image.setImageResource(data.getImage());
        item_image.setClipToOutline(true);  //item 테두리
        item_name.setText(data.getItemName());
        item_price.setText(data.getItemPrice()+"");
        end_time.setText(data.getEndTime());
        item_views.setText(data.getViews()+"");
        item_hearts.setText(data.getHeart()+"");
    }
}