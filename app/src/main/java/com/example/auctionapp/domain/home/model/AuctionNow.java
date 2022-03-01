package com.example.auctionapp.domain.home.model;

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
    String imageURL;
    String itemName;
    int itemPrice;
    String date;
    String itemInfo;

    public AuctionNow(Long itemId, String imageURL, String itemName, int itemPrice, String date, String itemInfo) {
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.date = date;
        this.itemInfo = itemInfo;
    }
}

