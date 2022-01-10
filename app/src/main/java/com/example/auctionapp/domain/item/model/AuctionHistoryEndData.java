package com.example.auctionapp.domain.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionHistoryEndData {
    Long itemId;
    String imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int itemPrice;
    String sellerName;

    public AuctionHistoryEndData(Long itemId, String imageURL, String itemName, int itemPrice, String sellerName){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.sellerName = sellerName;
    }

}
