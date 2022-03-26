package com.example.auctionapp.domain.scrap.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrapItem {
    Long itemId;
    String imageURL;
    String itemName;
    int itemPrice;
    String endTime;

    public ScrapItem(Long itemId, String imageURL, String itemName, int itemPrice, String endTime){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.endTime = endTime;
    }
}
