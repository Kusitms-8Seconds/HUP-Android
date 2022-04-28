package com.me.hurryuphup.domain.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.notification.model.NotificationListData;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<NotificationListData> data;
    private TextView notiMessage;
    private TextView notiCategory;


    public NotificationAdapter() {}
    public NotificationAdapter(Context context, ArrayList<NotificationListData> dataArray) {
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
    public NotificationListData getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.custom_notification_listview, null);

        notiMessage = (TextView) view.findViewById(R.id.noti_message);
        notiMessage.setText(data.get(position).getNoticeTitle());
        notiCategory = (TextView) view.findViewById(R.id.noti_time);
        notiCategory.setText(data.get(position).getTime());

        return view;
    }
}
