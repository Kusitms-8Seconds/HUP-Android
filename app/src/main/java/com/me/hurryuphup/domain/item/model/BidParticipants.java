package com.me.hurryuphup.domain.item.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidParticipants {
    Long userId;
    String ptImage;
    String ptName;
    String ptPrice;
    String ptTime;

    public BidParticipants(Long userId, String ptImage, String ptName, String ptPrice, String ptTime) {
        this.userId = userId;
        this.ptImage = ptImage;
        this.ptName = ptName;
        this.ptPrice = ptPrice;
        this.ptTime = ptTime;
    }

}