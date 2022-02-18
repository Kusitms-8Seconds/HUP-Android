package com.example.auctionapp.domain.mypage.notice.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class NoticeListResponse {

    private Long id;
    private String userName;
    private String title;

    public static NoticeListResponse from(Long id, String userName, String title) {
        return NoticeListResponse.builder()
                .id(id)
                .userName(userName)
                .title(title)
                .build();
    }
}