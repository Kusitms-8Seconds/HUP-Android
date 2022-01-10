package com.example.auctionapp.domain.mypage.notice.model;

public class NoticeData {
    private String noticeTitle;
    private String noticeDate;

    public NoticeData(){

    }

    public NoticeData(String noticeTitle, String noticeDate){
        this.noticeTitle = noticeTitle;
        this.noticeDate = noticeDate;
    }
    public String getNoticeTitle() {
        return this.noticeTitle;
    }
    public String getNoticeDate(){
        return this.noticeDate;
    }
}
