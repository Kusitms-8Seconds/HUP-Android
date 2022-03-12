package com.example.auctionapp.domain.notification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationListData {
    private String noticeTitle;
    private String category;

    public NotificationListData(){

    }

    public NotificationListData(String noticeTitle, String category){
        this.noticeTitle = noticeTitle;
        this.category = category;
    }
}