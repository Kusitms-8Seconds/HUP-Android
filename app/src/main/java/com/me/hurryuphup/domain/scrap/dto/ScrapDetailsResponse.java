package com.me.hurryuphup.domain.scrap.dto;

import com.me.hurryuphup.domain.item.constant.ItemConstants;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScrapDetailsResponse {

    private Long id;
    private Long userId;
    private Long itemId;
    private String itemName;
    private ItemConstants.EItemCategory category;
    private int initPrice;
    private int soldPrice;
    private LocalDateTime buyDate;
    private int itemStatePoint;
    private String description;
    private List<String> fileNames;
    private LocalDateTime auctionClosingDate;
    private int maximumPrice;
}
