package com.example.auctionapp.domain.pricesuggestion.dto;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class PriceSuggestionRequest {

    @NotEmpty(message = "상품의 itemId를 입력해주세요.")
    private Long itemId;

    @NotEmpty(message = "회원의 userId를 입력해주세요.")
    private Long userId;

    @NotEmpty(message = "입찰가격을 입력해주세요.")
    private int suggestionPrice;

    public PriceSuggestionRequest(Long itemId, Long userId, int suggestionPrice) {
        this.itemId = itemId;
        this.userId = userId;
        this.suggestionPrice = suggestionPrice;
    }

}
