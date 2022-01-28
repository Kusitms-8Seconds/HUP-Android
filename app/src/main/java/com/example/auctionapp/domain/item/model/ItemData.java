package com.example.auctionapp.domain.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemData {

    Long itemId;
    String imageURL;
    String itemName;
    int itemPrice;
    String endTime;
    int views;
    Long heart;

    public ItemData(Long itemId, String imageURL, String itemName, int itemPrice, String endTime, int views, Long heart){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.endTime = endTime;
        this.views = views;
        this.heart = heart;
    }
}
