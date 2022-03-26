package com.example.auctionapp.domain.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellHistoryEndData {
    Long itemId;
    String imageURL;
    String itemName;
    int itemPrice;
    String bidderName;
    Long chatRoomId;
    Long bidderId;

    public SellHistoryEndData(Long itemId, String imageURL, String itemName, int itemPrice, String bidderName, Long chatRoomId, Long bidderId){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.bidderName = bidderName;
        this.chatRoomId = chatRoomId;
        this.bidderId = bidderId;
    }

}
