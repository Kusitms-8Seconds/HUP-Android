package com.example.auctionapp.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

//    private String token;
//    private Long userId;
//
//    public static LoginResponse from(String token, Long userId) {
//        return LoginResponse.builder()
//                .token(token)
//                .userId(userId)
//                .build();
//    }

    private Long userId;
    private String accessToken;
    private String refreshToken;

    public static LoginResponse from(Long userId, TokenInfoResponse tokenInfoResponse) {
        return LoginResponse.builder()
                .userId(userId)
                .accessToken(tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse.getRefreshToken())
                .build();
    }

}
