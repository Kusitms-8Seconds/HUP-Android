package com.example.auctionapp.domain.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessageResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String message;
    private LocalDateTime createdDate;
}