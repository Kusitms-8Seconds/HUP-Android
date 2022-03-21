package com.example.auctionapp.domain.notification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationListData {
    private String noticeTitle;
    private String time;

    public NotificationListData(){

    }

    public NotificationListData(String noticeTitle, String time){
        this.noticeTitle = noticeTitle;
        this.time = time;
    }
}