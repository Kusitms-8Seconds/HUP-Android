package com.example.auctionapp.domain.pricesuggestion.dto;
import com.example.auctionapp.domain.item.constant.ItemConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PriceSuggestionResponse {

    private Long itemId;
    private Long userId;
    private Long priceSuggestionId;
    private int suggestionPrice;
    private String username;
    private int maximumPrice;
    private int theNumberOfParticipants;
    private ItemConstants.EItemSoldStatus soldStatus;

//    public static PriceSuggestionResponse from(Long itemId, Long userId, Long priceSuggestionId, int suggestionPrice, String username
//                                               int maximumPrice, int theNumberOfParticipants, ItemConstants.EItemSoldStatus soldStatus) {
//        return PriceSuggestionResponse.builder()
//                .itemId(itemId)
//                .userId(userId)
//                .priceSuggestionId(priceSuggestionId)
//                .suggestionPrice(suggestionPrice)
//                .username(username)
//                .maximumPrice(maximumPrice)
//                .theNumberOfParticipants(theNumberOfParticipants)
//                .soldStatus(soldStatus)
//                .build();
//    }
}