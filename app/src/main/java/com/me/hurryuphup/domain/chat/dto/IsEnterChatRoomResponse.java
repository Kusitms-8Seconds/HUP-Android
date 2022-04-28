package com.me.hurryuphup.domain.chat.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IsEnterChatRoomResponse {

    private boolean isEnter;

    public static IsEnterChatRoomResponse from(boolean isEnter) {
        return IsEnterChatRoomResponse.builder()
                .isEnter(isEnter)
                .build();
    }
}