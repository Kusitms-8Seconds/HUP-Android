package com.me.hurryuphup.domain.home.constant;

public class HomeConstants {

    public enum EDate {
        dpYear("년"),
        dpMonth("월"),
        dpMinute("분");

        String text;
        EDate(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
    public enum EHomeCallback {
        eHomeTAG("Home: "),
        egetBestItemsCallback("getBestItemsCallback::"),
        egetAllItemsInfoCallback("getAllItemsInfoCallback::"),
        egetHeartCallback("getHeartCallback::"),
        egetMaximumPriceBestItemCallback("getMaximumPriceBestItemCallback::"),

        rtSuccessResponse("retrofit success, idToken: "),
        rtFailResponse("onFailResponse"),
        rtConnectionFail("연결실패"),
        errorBody("errorBody");

        String text;
        EHomeCallback(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
}
