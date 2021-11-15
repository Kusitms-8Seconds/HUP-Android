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

public class AuctionNow {
    int imageURL; //url 수정하기
    String itemName;
    int itemPrice;
    String date;
    String itemInfo;

    public AuctionNow(int imageURL, String itemName, int itemPrice, String date, String itemInfo) {
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.date = date;
        this.itemInfo = itemInfo;
    }

    public int getImage() {
        return imageURL;
    }
    public void setImage(int imageURL) {
        this.imageURL = imageURL;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String endTime) {
        this.date = endTime;
    }

    public String getItemInfo() { return itemInfo; }
    public void setItemInfo(int views) {
        this.itemInfo = itemInfo;
    }
}

