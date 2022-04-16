package com.example.auctionapp.domain.user.dto;

import com.example.auctionapp.global.dto.DefaultResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailResetPasswordResponse extends DefaultResponse {
    private Long userId;
    private String loginId;

    public static EmailResetPasswordResponse from(Long userId, String loginId, String message) {
        EmailResetPasswordResponse emailResetPasswordResponse = EmailResetPasswordResponse.builder()
                .userId(userId)
                .loginId(loginId)
                .build();
        return emailResetPasswordResponse;
    }
}