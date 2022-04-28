package com.me.hurryuphup.domain.item.dto;

import com.me.hurryuphup.domain.item.constant.ItemConstants;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemDetailsResponse {
    private Long id;
    private Long userId;
    private String itemName;
    private ItemConstants.EItemCategory category;
    private int initPrice;
    private int soldPrice;
    private LocalDateTime buyDate;
    private int itemStatePoint;
    private String description;
    private ItemConstants.EItemSoldStatus soldStatus;
    private List<String> fileNames;
    private LocalDateTime auctionClosingDate;
    private int scrapCount;
    private int maximumPrice;
    private int participants;
    private Long chatRoomId;
    private Long bidderUserId;
    private String bidderUserName;
}
