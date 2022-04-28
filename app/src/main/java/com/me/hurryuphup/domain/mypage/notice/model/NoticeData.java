package com.me.hurryuphup.domain.mypage.notice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeData {
    private Long noticeId;
    private String noticeTitle;
    private String userName;

    public NoticeData(){

    }

    public NoticeData(Long noticeId, String noticeTitle, String userName){
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
        this.userName = userName;
    }
}
