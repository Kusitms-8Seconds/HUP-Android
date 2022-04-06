package com.example.auctionapp.domain.chat.dto;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomResponse {

    private Long id;
    private Long otherUserId;
    private String otherUserName;
    private Long itemId;
    private List<String> fileNames;
    private String latestMessage;
    private LocalDateTime latestTime;
}