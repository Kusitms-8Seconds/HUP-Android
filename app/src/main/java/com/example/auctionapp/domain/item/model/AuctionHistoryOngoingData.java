package com.example.auctionapp.domain.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionHistoryOngoingData {
    Long itemId;
    String imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int myPrice;
    int maxPrice;
    String leftTime;

    public AuctionHistoryOngoingData(Long itemId, String imageURL, String itemName, int myPrice, int maxPrice, String leftTime) {
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.myPrice = myPrice;
        this.maxPrice = maxPrice;
        this.leftTime = leftTime;

    }

}
