package com.me.hurryuphup.domain.notification.dto;
import java.time.LocalDateTime;

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
    private LocalDateTime createdDate;
}