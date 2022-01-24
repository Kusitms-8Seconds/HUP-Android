package com.example.auctionapp.domain.pricesuggestion.constant;

public class PriceConstants {

    public enum EPriceCallback {
        ePriceTAG("PriceCallback: "),
        egetItemDetailsCallback("getItemDetailsCallback::"),
        egetMaximumPriceCallback("getMaximumPriceCallback::"),
        egetParticipantsCallback("getParticipantsCallback::"),
        egetAllPriceSuggestionCallback("getAllPriceSuggestionCallback::"),
        egetUserDetailsCallback("getUserDetailsCallback::"),

        rtSuccessResponse("retrofit success, idToken: "),
        rtFailResponse("onFailResponse"),
        rtConnectionFail("연결실패"),
        errorBody("errorBody"),

        dpDay("일 "),
        dpHour("시간 "),
        dpMinute("분 전");

        private String text;
        private EPriceCallback(String text) {
            this.text = text;
        }
        public String getText() { return this.text; }
    }
}
