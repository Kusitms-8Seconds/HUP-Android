package com.me.hurryuphup.domain.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.chat.model.chatListData;
import com.me.hurryuphup.domain.user.constant.Constants;

import java.util.ArrayList;

public class chatListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<chatListData> data;
    private ImageView itemImageImageView;
    private TextView profileNameTextView;
    private TextView chatTimeTextView;
    private TextView lastChatTextView;


    public chatListAdapter() {}
    public chatListAdapter(Context context, ArrayList<chatListData> dataArray) {
        mContext = context;
        data = dataArray;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public chatListData getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.custom_chatlist_listview, null);

        itemImageImageView = (ImageView) view.findViewById(R.id.iv_chatlist_itemImage);
        itemImageImageView.setClipToOutline(true);
        Long itemIdL = data.get(position).getItemId();
        // 상품 이미지 load
        Glide.with(itemImageImageView.getContext()).load(Constants.imageBaseUrl+data.get(position).getItemUrl()).into(itemImageImageView);
        profileNameTextView = (TextView) view.findViewById(R.id.tv_chatlist_profileName);
        profileNameTextView.setText(data.get(position).getUserName());
        chatTimeTextView = (TextView) view.findViewById(R.id.tv_chatlist_lastChatTime);
        chatTimeTextView.setText(data.get(position).getLatestTime());
        lastChatTextView = (TextView) view.findViewById(R.id.tv_chatlist_lastChat);
        lastChatTextView.setText(data.get(position).getLatestMessage());

        return view;
    }
}