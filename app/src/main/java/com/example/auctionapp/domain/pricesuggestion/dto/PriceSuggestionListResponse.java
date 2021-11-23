package com.example.auctionapp.domain.pricesuggestion.dto;

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
    private Long itemId;
    private String itemName;
    private int suggestionPrice;
    private boolean acceptState;
    private List<String> fileNames;
    private LocalDateTime auctionClosingDate;

}
