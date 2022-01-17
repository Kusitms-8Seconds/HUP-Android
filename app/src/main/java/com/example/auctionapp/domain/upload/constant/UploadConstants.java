package com.example.auctionapp.domain.upload.constant;

public class UploadConstants {

    private static final String TAG = "UploadPage";

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
        editInitPrice("경매시작가 입력하세요");

        String text;
        EUploadToast(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
    public enum EUploadCallback {
        TAG("UploadPage"),
        rtSuccessResponse("retrofit success, idToken: "),
        rtFailResponse("onFailResponse"),
        rtConnectionFail("연결실패");

        String text;
        EUploadCallback(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
    public enum ECategory {
        eDigital("eDigital"),
        eHouseHoldAppliance("eHouseHoldAppliance"),
        eFurnitureAndInterior("eFurnitureAndInterior"),
        eChildren("eChildren"),
        eDailyLifeAndProcessedFood("eDailyLifeAndProcessedFood"),
        eChildrenBooks("eChildrenBooks"),
        eSportsAndLeisure("eSportsAndLeisure"),
        eMerchandiseForWoman("eMerchandiseForWoman"),
        eWomenClothing("eWomenClothing"),
        eManFashionAndMerchandise("eManFashionAndMerchandise"),
        eGameAndHabit("eGameAndHabit"),
        eBeauty("eBeauty"),
        ePetProducts("ePetProducts"),
        eBookTicketAlbum("eBookTicketAlbum"),
        ePlant("ePlant")
        ;

        String text;
        ECategory(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
}
