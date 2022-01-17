package com.example.auctionapp.domain.mypage.notice.constant;

public class NoticeConstants {

    public enum ENoticeDetails {
        noticeTitle("noticeTitle"),
        noticeDate("noticeDate");

        private String text;
        private ENoticeDetails(String text) {
            this.text = text;
        }
        public String getText() { return this.text; }
    }
}
