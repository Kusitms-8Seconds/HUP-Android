package com.me.hurryuphup.domain.item.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.item.model.AuctionHistoryOngoingData;
import com.me.hurryuphup.domain.user.constant.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AuctionHistoryOngoingAdapter extends BaseAdapter {
    ArrayList<AuctionHistoryOngoingData> items =
            new ArrayList<AuctionHistoryOngoingData>();
    Context context;

    public void addItem(AuctionHistoryOngoingData item) {
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
        AuctionHistoryOngoingData category = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_auction_history_ongoing_recyclerview, parent, false);
        }

        ImageView auc_history_ongoing_img = (ImageView) convertView.findViewById(R.id.auc_history_ongoing_img);
        TextView auc_history_ongoing_edt_name = (TextView) convertView.findViewById(R.id.auc_history_ongoing_edt_name);
        TextView auc_history_ongoing_myPrice = (TextView) convertView.findViewById(R.id.auc_history_ongoing_myPrice);
        TextView auc_history_ongoing_max = (TextView) convertView.findViewById(R.id.auc_history_ongoing_max);
        TextView itemLeftTime = (TextView) convertView.findViewById(R.id.itemLeftTime);
        TextView tv1 = (TextView) convertView.findViewById(R.id.auc_history_ongoing_edt_end1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.auc_history_ongoing_edt_end2);
        DecimalFormat myFormatter = new DecimalFormat("###,###");

        if(items.get(position).getImageURL() == null) {
            Glide.with(context).load(context.getString(R.string.hup_icon_url)).override(auc_history_ongoing_img.getWidth()
                    ,auc_history_ongoing_img.getHeight()).into(auc_history_ongoing_img);
        } else {
            Glide.with(context).load(Constants.imageBaseUrl + items.get(position).getImageURL()).override(auc_history_ongoing_img.getWidth()
                    , auc_history_ongoing_img.getHeight()).into(auc_history_ongoing_img);
        }
        auc_history_ongoing_img.setClipToOutline(true);  //item 테두리
        auc_history_ongoing_edt_name.setText(items.get(position).getItemName());
        auc_history_ongoing_myPrice.setText(myFormatter.format(items.get(position).getMyPrice()));
        auc_history_ongoing_max.setText(myFormatter.format(items.get(position).getMaxPrice()));
        itemLeftTime.setText(items.get(position).getLeftTime());
        if(items.get(position).getLeftTime().equals("")) {
            itemLeftTime.setText("경매 시간 종료");
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
        }


        return convertView;
    }
}
