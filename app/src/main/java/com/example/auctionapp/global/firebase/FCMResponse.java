package com.example.auctionapp.global.firebase;

import com.example.auctionapp.domain.user.dto.LoginResponse;
import com.example.auctionapp.domain.user.dto.TokenInfoResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FCMResponse {
    private Long itemId;
    private Long priceSuggestionId;
    private int salesPrice;

    public static FCMResponse from(Long itemId, Long priceSuggestionId, int salesPrice) {
        return FCMResponse.builder()
                .itemId(itemId)
                .priceSuggestionId(priceSuggestionId)
                .salesPrice(salesPrice)
                .build();
    }
}
