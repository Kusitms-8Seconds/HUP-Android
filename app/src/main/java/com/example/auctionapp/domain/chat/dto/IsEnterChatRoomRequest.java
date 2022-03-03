package com.example.auctionapp.domain.chat.dto;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IsEnterChatRoomRequest {

    @NotEmpty(message = "메세지를 보내고자 하는 채팅방 id를 입력해주세요.")
    private Long chatRoomId;

    @NotEmpty(message = "메세지를 보내는 유저 id를 입력해주세요.")
    private Long userId;
}