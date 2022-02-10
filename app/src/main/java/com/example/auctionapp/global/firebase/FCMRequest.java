package com.example.auctionapp.global.firebase;

import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.GetAllItemsByStatusRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FCMRequest {
    private Long itemId;

    public static FCMRequest of(Long itemId) {
        return FCMRequest.builder()
                .itemId(itemId)
                .build();
    }
}
