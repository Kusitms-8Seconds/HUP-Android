package com.example.auctionapp.domain.item.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DefaultResponse {

    private LocalDateTime responseTime;
    private String message;

    private DefaultResponse(String message) {
        this.responseTime = LocalDateTime.now();
        this.message = message;
    }

    public static DefaultResponse from(String message) {
        return new DefaultResponse(message);
    }

}