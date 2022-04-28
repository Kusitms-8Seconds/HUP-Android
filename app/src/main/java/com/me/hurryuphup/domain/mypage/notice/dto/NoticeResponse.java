package com.me.hurryuphup.domain.mypage.notice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String body;
    private List<String> fileNames;

}