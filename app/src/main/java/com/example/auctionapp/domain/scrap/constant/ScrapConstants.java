package com.example.auctionapp.domain.scrap.constant;

public class ScrapConstants {

    public enum EScrapCallback {
        eScrapTAG("Scrap: "),
        egetAllScrapsCallback("getAllScrapsCallback::"),
        egetMaximumPriceCallback("getMaximumPriceCallback::"),

        dpMinute("분"),
        logItemId("itemId: "),
        rtSuccessResponse("retrofit success, idToken: "),
        rtFailResponse("onFailResponse"),
        rtConnectionFail("연결실패");

        private String text;
        private EScrapCallback(String text) {
            this.text = text;
        }
        public String getText() { return this.text; }
    }
}
