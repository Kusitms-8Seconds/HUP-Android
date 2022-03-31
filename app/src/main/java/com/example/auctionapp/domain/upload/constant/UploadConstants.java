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
        oneselectImage("프로필 이미지는 한장만 선택 가능합니다."),
        imageSelectOver("사진은 10장까지 선택 가능합니다."),
        eEditItemName("제목을 입력하세요."),
        eSelectCategory("카테고리를 선택하세요."),
        eEditInitPrice("경매시작가를 입력하세요."),
        eSelectBuyDate("구매 일자를 선택하세요."),
        eSelectFinalDate("경매 종료 일자를 선택하세요."),
        eSelectFinalTime("경매 종료 시간을 선택하세요."),
//        eSelectItemState("물건 상태를 체크해주세요."),
        eEditItemContent("제품 소개해주세요.");

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
        ePlant("ePlant"),

        itemCategory("itemCategory"),
        itemName("itemName"),
        itemPrice("itemPrice"),
        itemContent("itemContent"),
        itemBuyDate("구매 일자"),
        itemEndDate("경매 종료 일자"),
        itemEndTime("경매 종료 시간"),
        itemStatePoint("itemStatePoint"),
        eNullString("");

        String text;
        ECategory(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }

    public enum EUploadLog {
        newRating("New Rating: "),
        singleChoice("single choice: "),
        multiChoice("multiple choice"),
        fileSelectError("File select error"),
        fileCheck("files체크: ");

        String text;
        EUploadLog(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }

    public enum EMultiPart {
        mediaTypePlain("text/plain"),
        mediaTypeImage("image/jpeg"),
        files("files");

        String text;
        EMultiPart(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
}
