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
    private Long userId;
    private String userName;
    private Long itemId;
    private List<String> fileNames;
    private String latestMessage;
    private LocalDateTime latestTime;

    //채팅룸id, userid, username, item id, item url, 최근메세지, 최근시간
    public static ChatRoomResponse of(Long id, Long userId, String userName, Long itemId,
                                      List<String> fileNames, String latestMessage, LocalDateTime latestTime) {
        return ChatRoomResponse.builder()
                .id(id)
                .userId(userId)
                .userName(userName)
                .itemId(itemId)
                .fileNames(fileNames)
                .latestMessage(latestMessage)
                .latestTime(latestTime)
                .build();
    }
}