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
    private String body;
    private String statusCode;
    private Long statusCodeValue;

    public static FCMResponse from(String body, String statusCode, Long statusCodeValue) {
        return FCMResponse.builder()
                .body(body)
                .statusCode(statusCode)
                .statusCodeValue(statusCodeValue)
                .build();
    }
}
