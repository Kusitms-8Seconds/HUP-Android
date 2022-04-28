package com.me.hurryuphup.domain.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemData {

    Long itemId;
    String imageURL;
    String itemName;
    String itemPrice;
    String endTime;
    int views;
    int heart;

    public ItemData(Long itemId, String imageURL, String itemName, String itemPrice, String endTime, int views, int heart){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.endTime = endTime;
        this.views = views;
        this.heart = heart;
    }
}
