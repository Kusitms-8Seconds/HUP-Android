package com.me.hurryuphup.domain.home.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestItem {
    String btImage;
    String btName;
    String btTime;
    String btTempMax;

    public BestItem(String btImage, String btName, String btTime, String btPrice) {
        this.btImage = btImage;
        this.btName = btName;
        this.btTime = btTime;
        this.btTempMax = btPrice;
    }

}

