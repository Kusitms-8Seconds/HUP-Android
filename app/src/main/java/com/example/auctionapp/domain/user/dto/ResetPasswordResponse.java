package com.example.auctionapp.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordResponse {
    private Long userId;
    private String loginId;
    public static ResetPasswordResponse from(Long userId, String loginId) {
        return ResetPasswordResponse.builder()
                .userId(userId)
                .loginId(loginId)
                .build();
    }
}