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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestItem {
    String btImage;    //나중에 수정
    String btName;
    String btTime;
    int btTempMax;

    public BestItem(String btImage, String btName, String btTime, int btPrice) {
        this.btImage = btImage;
        this.btName = btName;
        this.btTime = btTime;
        this.btTempMax = btPrice;
    }


}

