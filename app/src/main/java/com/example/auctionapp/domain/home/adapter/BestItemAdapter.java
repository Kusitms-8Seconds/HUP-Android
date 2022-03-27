package com.example.auctionapp.domain.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityHomeBinding;
import com.example.auctionapp.domain.home.model.BestItem;
import com.example.auctionapp.domain.user.constant.Constants;

import java.util.ArrayList;

public class BestItemAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<BestItem> listData;
    Context context;

    public BestItemAdapter(Context context, ArrayList<BestItem> listData) {
        this.mContext = context;
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_layout_home_bestitem, null);
        ImageView viewPagerImageView = view.findViewById(R.id.bt_image);
        TextView bt_item_name = view.findViewById(R.id.bt_item_name);
        TextView bt_purchase_date = view.findViewById(R.id.bt_purchase_date);
        TextView bt_temp_max = view.findViewById(R.id.bt_temp_max);
        TextView won = view.findViewById(R.id.won);

        String url = listData.get(position).getBtImage();
        if(url == null) {
            Glide.with(context).load(context.getString(R.string.hup_icon_url)).override(viewPagerImageView.getWidth()
                    ,viewPagerImageView.getHeight()).into(viewPagerImageView);
        } else {
            Glide.with(context).load(Constants.imageBaseUrl+url).override(viewPagerImageView.getWidth()
                    ,viewPagerImageView.getHeight()).into(viewPagerImageView);
        }
        bt_item_name.setText(listData.get(position).getBtName());
        bt_purchase_date.setText(listData.get(position).getBtTime());
        if(listData.get(position).getBtTempMax().equals("0")) {
            bt_temp_max.setText("ㅡ");
            won.setText("");
        } else bt_temp_max.setText(listData.get(position).getBtTempMax() + "");

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return listData.size();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View) o);
    }

}
