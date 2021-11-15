package com.example.auctionapp.domain.item.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.auctionapp.R;

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

