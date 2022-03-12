package com.example.auctionapp.domain.notification.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationListResponse {

    private Long id;
    private String username;
    private Long userId;
    private String message;
    private String eNotificationCategory;

}