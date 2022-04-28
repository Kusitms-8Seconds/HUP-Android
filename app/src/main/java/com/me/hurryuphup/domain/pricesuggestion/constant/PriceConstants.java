package com.me.hurryuphup.domain.pricesuggestion.constant;

import lombok.Getter;

public class PriceConstants {

    public enum EPriceCallback {
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
    @Getter
    public enum EPriceSuggestionServiceImpl{
        eZero(0),
        eNotOnGoingExceptionMessage("경매 진행중인 상품이 아닙니다."),
        eTimeOutExceptionMessage("경매 시간이 종료되었습니다."),
        eAlreadySoldOutExceptionMessage("이미 팔린 상품입니다."),
        ePriorPriceSuggestionExceptionMessage("이전의 입찰가격이 지금의 입찰보다 높거나 같습니다."),
        eEditPriceMessage("가격을 입력하세요.");
        private int size;
        private String value;

        EPriceSuggestionServiceImpl(int size) {this.size = size;}
        EPriceSuggestionServiceImpl(String value) {this.value = value;}
    }

}
