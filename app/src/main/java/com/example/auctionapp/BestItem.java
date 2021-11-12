package com.example.auctionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BestItem {
    int btImage;    //나중에 수정
    String btName;
    String btTime;
    int btTempMax;

    public BestItem(int btImage, String btName, String btTime, int btPrice) {
        this.btImage = btImage;
        this.btName = btName;
        this.btTime = btTime;
        this.btTempMax = btPrice;
    }

    public String getBtName() {
        return btName;
    }
    public void setBtName(String btName) {
        this.btName = btName;
    }
    public String getBtTime() {
        return btTime;
    }
    public void setBtTime(String btTime) {
        this.btTime = btTime;
    }
    public int getBtTempMax() {
        return btTempMax;
    }
    public void setBtTempMax(int btPrice) {
        this.btTempMax = btPrice;
    }
    public int getBtImage() {
        return btImage;
    }
    public void setBtImage(int btImage) {
        this.btImage = btImage;
    }

}

class BestItemAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<BestItem> listData;

//    public BestItemAdapter() {}

    public BestItemAdapter(Context context, ArrayList<BestItem> listData)
    {
        this.mContext = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_layout_home_bestitem, null);

        ImageView ViewPagerImageView = view.findViewById(R.id.bt_image);
        ViewPagerImageView.setImageResource(listData.get(position).getBtImage());
        TextView bt_item_name = view.findViewById(R.id.bt_item_name);
        bt_item_name.setText(listData.get(position).getBtName());
        TextView bt_purchase_date = view.findViewById(R.id.bt_purchase_date);
        bt_purchase_date.setText(listData.get(position).getBtTime());
        TextView bt_temp_max = view.findViewById(R.id.bt_temp_max);
        bt_temp_max.setText(listData.get(position).getBtTempMax()+"");

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return listData.size();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }

}

