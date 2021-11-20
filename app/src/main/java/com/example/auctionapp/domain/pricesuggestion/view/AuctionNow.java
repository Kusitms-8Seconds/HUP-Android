package com.example.auctionapp.domain.pricesuggestion.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionNow {

    Long itemId;
    String imageURL;  //url 수정하기
    String itemName;
    int itemPrice;
    String date;
    String itemInfo;
    Long heart;

    public AuctionNow(Long itemId, String imageURL, String itemName, int itemPrice, String date, String itemInfo, Long heart) {
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.date = date;
        this.itemInfo = itemInfo;
        this.heart = heart;
    }
}

