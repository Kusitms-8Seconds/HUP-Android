package com.me.hurryuphup.domain.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellHistoryOngoingData {
    Long itemId;
    String imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int maxPrice;
    String leftTime;

    public SellHistoryOngoingData(Long itemId, String imageURL, String itemName, int maxPrice, String leftTime) {
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.maxPrice = maxPrice;
        this.leftTime = leftTime;

    }

}
