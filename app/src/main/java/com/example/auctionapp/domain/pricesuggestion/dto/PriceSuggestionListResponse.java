package com.example.auctionapp.domain.pricesuggestion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PriceSuggestionListResponse {

    private Long priceSuggestionId;
    private Long userId;
    private Long itemId;
    private int suggestionPrice;
    private boolean acceptState;

}
