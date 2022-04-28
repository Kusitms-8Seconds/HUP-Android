package com.me.hurryuphup.domain.chat.dto;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class ChatMessageRequest {

    @NotEmpty(message = "메세지를 보내고자 하는 채팅방 id를 입력해주세요.")
    private Long chatRoomId;

    @NotEmpty(message = "메세지를 보내는 유저 id를 입력해주세요.")
    private Long userId;

    @NotEmpty(message = "보내고자 하는 메세지를 입력해주세요.")
    private String message;
}