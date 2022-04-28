package com.me.hurryuphup.domain.scrap.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrapItem {
    Long itemId;
    String imageURL;
    String itemName;
    String itemPrice;
    String endTime;

    public ScrapItem(Long itemId, String imageURL, String itemName, String itemPrice, String endTime){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.endTime = endTime;
    }
}
