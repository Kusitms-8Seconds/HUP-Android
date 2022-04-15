package com.example.auctionapp.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindLoginIdResponse {
    private Long userId;
    private String loginId;
    public static FindLoginIdResponse from(Long userId, String loginId) {
        return FindLoginIdResponse.builder()
                .userId(userId)
                .loginId(loginId)
                .build();
    }
}