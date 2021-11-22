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
public class BidParticipants {
    Long userId;
    int ptImage;    //나중에 url로 수정
    String ptName;
    int ptPrice;
    String ptTime;

    public BidParticipants(Long userId, int ptImage, String ptName, int ptPrice, String ptTime) {
        this.userId = userId;
        this.ptImage = ptImage;
        this.ptName = ptName;
        this.ptPrice = ptPrice;
        this.ptTime = ptTime;
    }

}