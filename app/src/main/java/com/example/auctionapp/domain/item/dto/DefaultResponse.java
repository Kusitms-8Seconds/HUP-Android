package com.example.auctionapp.domain.item.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DefaultResponse {

    private String message;

    private DefaultResponse(String message) {
        this.message = message;
    }
//    public static DefaultResponse from(String message) {
//        return new DefaultResponse(message);
//    }

}
