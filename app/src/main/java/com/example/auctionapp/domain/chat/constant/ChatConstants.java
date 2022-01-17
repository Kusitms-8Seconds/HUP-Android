package com.example.auctionapp.domain.chat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatConstants {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum EChatId {
        myuid,
        destUid,
        chatRoomUid,
        itemId;

        String text;
        public String getText(){
            return this.text;
        }
    }
}
