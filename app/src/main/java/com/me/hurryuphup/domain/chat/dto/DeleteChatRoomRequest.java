package com.me.hurryuphup.domain.chat.dto;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteChatRoomRequest {

    @NotEmpty(message = "채팅방 id를 입력해주세요.")
    private Long chatRoomId;

    @NotEmpty(message = "유저 id를 입력해주세요.")
    private Long userId;
}