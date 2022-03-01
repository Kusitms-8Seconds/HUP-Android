package com.example.auctionapp.domain.chat.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class chatListData {
    private Long chatroomId;
    private Long destId;
    private String userName;
    private Long itemId;
    private String itemUrl;
    private String latestMessage;
    private String latestTime;

    public chatListData(){ }

    public chatListData(Long chatroomId, Long destId, String userName, Long itemId, String itemUrl, String latestMessage, String latestTime) {
        this.chatroomId =  chatroomId;
        this.destId = destId;
        this.itemId = itemId;
        this.userName = userName;
        this.itemUrl = itemUrl;
        this.latestMessage = latestMessage;
        this.latestTime = latestTime;
    }
}
