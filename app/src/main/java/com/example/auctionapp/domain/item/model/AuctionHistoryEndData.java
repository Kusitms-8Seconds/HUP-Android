package com.example.auctionapp.domain.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionHistoryEndData {
    Long itemId;
    String imageURL;
    String itemName;
    int itemPrice;
    String sellerName;
    Long chatRoomId;
    Long sellerId;

    public AuctionHistoryEndData(Long itemId, String imageURL, String itemName, int itemPrice, String sellerName, Long chatRoomId, Long sellerId){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.sellerName = sellerName;
        this.chatRoomId = chatRoomId;
        this.sellerId = sellerId;
    }

}
