package com.me.hurryuphup.domain.pricesuggestion.dto;
import com.me.hurryuphup.domain.item.constant.ItemConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PriceSuggestionResponse {

    private Long itemId;
    private Long userId;
    private String picture;
    private Long priceSuggestionId;
    private int suggestionPrice;
    private String username;
    private int maximumPrice;
    private int theNumberOfParticipants;
    private ItemConstants.EItemSoldStatus soldStatus;
}