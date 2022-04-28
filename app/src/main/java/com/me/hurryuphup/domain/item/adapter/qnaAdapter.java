package com.me.hurryuphup.domain.item.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.item.model.qnaData;

import java.util.ArrayList;

public class qnaAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<qnaData> data;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView idTextView;
    private TextView stateTextView;
    private ImageView icon;
    private ConstraintLayout ly_answer;


    public qnaAdapter() {}
    public qnaAdapter(Context context, ArrayList<qnaData> dataArray) {
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
    public qnaData getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.custom_qna_list, null);

        titleTextView = (TextView) view.findViewById(R.id.question_title);
        titleTextView.setText(data.get(position).getTitle());
        dateTextView = (TextView) view.findViewById(R.id.question_date);
        dateTextView.setText(data.get(position).getDate());
        idTextView = (TextView) view.findViewById(R.id.question_id);
        idTextView.setText(data.get(position).getId());
        stateTextView = (TextView) view.findViewById(R.id.qna_state);
        if(data.get(position).getState()) {
            stateTextView.setText("답변완료");
            stateTextView.setTextColor(Color.BLUE);
        }else {
            stateTextView.setText("답변예정");
        }
        icon = (ImageView) view.findViewById(R.id.foldIcon);
        ly_answer = (ConstraintLayout) view.findViewById(R.id.ly_answer);
        if(!data.get(position).getFolded()) {
            ly_answer.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.down);
        }else {
            ly_answer.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.fold_up);
        }

        return view;
    }

}