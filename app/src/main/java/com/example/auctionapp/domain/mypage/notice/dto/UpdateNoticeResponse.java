package com.example.auctionapp.domain.mypage.notice.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateNoticeResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String body;
    private List<String> fileNames;
}
