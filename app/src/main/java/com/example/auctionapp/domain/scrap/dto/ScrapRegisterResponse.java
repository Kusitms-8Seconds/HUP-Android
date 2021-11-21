package com.example.auctionapp.domain.scrap.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapRegisterResponse {

    private LocalDateTime responseTime;
    private String message;
    private Long scrapId;

    private ScrapRegisterResponse(String message, Long scrapId) {
        this.responseTime = LocalDateTime.now();
        this.message = message;
        this.scrapId = scrapId;
    }

    public static ScrapRegisterResponse from(String message, Long scrapId) {
        return new ScrapRegisterResponse(message, scrapId);
    }
}