package com.example.auctionapp.domain.mypage.notice.dto;
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

//    public static NoticeResponse from(Long id, Long userId, String userName, String title, String body, List<String> filenames) {
//        filenames = new ArrayList<>();
//        if (!filenames.isEmpty()) {
//            List<MyFile> myFiles = notice.getMyFiles();
//            filenames = new ArrayList<>();
//            for (MyFile myFile : myFiles) {
//                filenames.add(myFile.getFilename());
//            }
//        }
//
//        return NoticeResponse.builder()
//                .id(id)
//                .userId(userId)
//                .userName(userName)
//                .title(title)
//                .body(body)
//                .fileNames(filenames)
//                .build();
//    }
}