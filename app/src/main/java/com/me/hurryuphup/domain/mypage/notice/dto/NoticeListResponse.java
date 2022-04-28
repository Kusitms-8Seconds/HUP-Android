package com.me.hurryuphup.domain.mypage.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class NoticeListResponse {

    private Long id;
    private String userName;
    private String title;

//    public static NoticeListResponse from(Long id, String userName, String title) {
//        return NoticeListResponse.builder()
//                .id(id)
//                .userName(userName)
//                .title(title)
//                .build();
//    }
}