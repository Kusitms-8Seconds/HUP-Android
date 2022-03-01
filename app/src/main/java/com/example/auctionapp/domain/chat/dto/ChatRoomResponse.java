package com.example.auctionapp.domain.chat.dto;
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
    private String itemName;

    public static ChatRoomResponse of(Long id, Long userId, String userName, Long itemId,String itemName) {
        return ChatRoomResponse.builder()
                .id(id)
                .userId(userId)
                .userName(userName)
                .itemId(itemId)
                .itemName(itemName)
                .build();
    }
}