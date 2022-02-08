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
    private String body;
    private String targetToken;
    private String title;

    public static FCMRequest of(String body, String targetToken, String title) {
        return FCMRequest.builder()
                .body(body)
                .targetToken(targetToken)
                .title(title)
                .build();
    }
}
