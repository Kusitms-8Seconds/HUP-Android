package com.me.hurryuphup.domain.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.home.model.AuctionNow;
import com.me.hurryuphup.domain.item.view.ItemDetail;
import com.me.hurryuphup.domain.mypage.constant.MypageConstants;
import com.me.hurryuphup.domain.user.constant.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lombok.Getter;

public @Getter
class AuctionNowAdapter extends BaseAdapter {
    ArrayList<AuctionNow> items = new ArrayList<AuctionNow>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(AuctionNow item) {
        items.add(item);
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final AuctionNow auctionNowItem = items.get(position);

        ImageView item_image;
        TextView item_name;
        TextView item_upPrice;
        TextView item_date;
        TextView item_info;
        TextView upArrow;
        TextView won;
        LinearLayout ly_price;
        DecimalFormat myFormatter = new DecimalFormat("###,###");

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_home_auction_list, viewGroup, false);

            item_image = convertView.findViewById(R.id.sell_history_ongoing_img);
            item_name = convertView.findViewById(R.id.sell_history_ongoing_edt_name);
            item_upPrice = convertView.findViewById(R.id.auc_list_price);
            item_date = convertView.findViewById(R.id.sell_history_ongoing_max);
            item_info = convertView.findViewById(R.id.sell_history_ongoing_myPrice);
            upArrow = convertView.findViewById(R.id.upArrow);
            won = convertView.findViewById(R.id.won);
            ly_price = convertView.findViewById(R.id.ly_price);

            if(auctionNowItem.getImageURL() == null) {
                Glide.with(context).load(context.getString(R.string.hup_icon_url)).override(item_image.getWidth()
                        ,item_image.getHeight()).into(item_image);
            } else {
                Glide.with(context).load(Constants.imageBaseUrl+auctionNowItem.getImageURL()).override(item_image.getWidth()
                        ,item_image.getHeight()).into(item_image);
            }
            item_image.setClipToOutline(true);  //item 테두리
            item_name.setText(auctionNowItem.getItemName());
            int dif = auctionNowItem.getItemPrice();
            if(dif <= 0) {
                upArrow.setText("");
                won.setVisibility(View.GONE);
                item_upPrice.setText("ㅡ");
                item_upPrice.setTextColor(Color.BLUE);
                item_upPrice.setTypeface(item_upPrice.getTypeface(), Typeface.BOLD);
                ly_price.setBackgroundResource(R.drawable.dialog_edge);
            } else
                item_upPrice.setText(myFormatter.format(auctionNowItem.getItemPrice()));
            item_date.setText(auctionNowItem.getDate());
            if(auctionNowItem.getDate().equals("경매 시간 종료")) item_date.setTypeface(item_date.getTypeface(), Typeface.BOLD);
            item_info.setText(auctionNowItem.getItemInfo() + "");

        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        //각 아이템 선택 event
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.userId != null) {
                    Intent intent = new Intent(context, ItemDetail.class);
                    intent.putExtra("itemId", auctionNowItem.getItemId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, MypageConstants.ELogin.afterLogin.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;  //뷰 객체 반환
    }
}