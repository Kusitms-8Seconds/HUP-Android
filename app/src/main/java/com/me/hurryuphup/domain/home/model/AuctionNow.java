package com.me.hurryuphup.domain.home.model;

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

