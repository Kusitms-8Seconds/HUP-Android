package com.example.auctionapp.domain.upload.constant;

public class UploadConstants {

    private static final String tag = "UploadPage";

    public enum EDate {
        datePattern("yyyy-MM-dd"),
        dateTimePattern("yyyy-MM-dd HH:mm:ss"),
        dateZero(" 00:00:00");

        String text;
        EDate(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
    public enum EUploadToast {
        afterLogin("로그인 후 상품 등록이 가능합니다."),
        uploadComplete("상품 등록이 완료되었습니다."),
        unselectImage("이미지를 선택하지 않았습니다."),
        imageSelectOver("사진은 10장까지 선택 가능합니다."),
        editInitPrice("경매시작가 입력하세요"),
        ;

        String text;
        EUploadToast(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
}
