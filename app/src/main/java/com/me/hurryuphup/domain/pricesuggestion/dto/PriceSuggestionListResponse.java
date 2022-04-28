package com.me.hurryuphup.domain.pricesuggestion.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PriceSuggestionListResponse {

    private Long priceSuggestionId;
    private Long userId;
    private String userName;
    private String picture;
    private Long itemId;
    private String itemName;
    private int suggestionPrice;
    private boolean acceptState;
    private List<String> fileNames;
    private LocalDateTime auctionClosingDate;
    private Long chatRoomId;
    private Long sellerUserId;
    private String sellerUserName;
}
