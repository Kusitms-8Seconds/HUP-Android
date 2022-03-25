package com.example.auctionapp.domain.item.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class ItemConstants {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum EFileServiceImpl {
        BASE_DIR("user.dir"),
        IMAGES_DIR("images/"),
        FILE_NOT_FOUND_EXCEPTION_MESSAGE("파일이 존재하지 않습니다."),
        FILE_TO_SAVE_NOT_EXIST_EXCEPTION_MESSAGE("저장할 파일이 존재하지 않습니다.");

        private String message;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum EItemCategory{
        eDigital("디지털 기기"),
        eHouseHoldAppliance("생활가전"),
        eFurnitureAndInterior("가구/인테리어"),
        eChildren("유아동"),
        eDailyLifeAndProcessedFood("생활/가공식품"),
        eChildrenBooks("유아도서"),
        eSportsAndLeisure("스포츠/레저"),
        eMerchandiseForWoman("여성잡화"),
        eWomenClothing("여성의류"),
        eManFashionAndMerchandise("남성패션/잡화"),
        eGameAndHabit("게임/취미"),
        eBeauty("뷰티/미용"),
        ePetProducts("반려동물용품"),
        eBookTicketAlbum("도서/티켓/음반"),
        ePlant("식물");

        private String name;

        public static EItemCategory from(String s) {
            return EItemCategory.valueOf(s);
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum EItemSoldStatus{
        eNew("새로운 상품"),
        eOnGoing("경매중인 상품"),
        eSoldOut("판매 완료");

        private String name;

        public static EItemSoldStatus from(String s) {
            return EItemSoldStatus.valueOf(s);
        }
    }

    public enum EItemCallback {
        eHupIcon("https://user-images.githubusercontent.com/61726631/149874766-fcb10202-e727-4841-bfa4-2ebddc515b8d.jpg"),

        rtSuccessResponse("retrofit success, idToken: "),
        rtFailResponse("onFailResponse"),
        rtConnectionFail("연결실패"),
        errorBody("errorBody");

        String text;
        EItemCallback(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }

    @Getter
    @RequiredArgsConstructor
    public enum EItemServiceImpl{
        eInvalidItemSoldStatusExceptionMessage("유효하지 않은 상품판매상태입니다."),
        eInvalidCategoryExceptionMessage("유효하지 않은 카테고리입니다."),
        eNotFoundItemExceptionForCategoryMessage("해당 카테고리에 해당하는 상품이 없습니다."),
        eNotFoundItemExceptionForDefaultMessage("해당 아이디로 상품을 찾을 수 없습니다."),
        eNotOnGoingExceptionMessage("경매중인 상품이 아닙니다."),
        eNotSoldOutTimeExceptionMessage("낙찰 가능한 시간이 아닙니다."),
        eNotDesirableAuctionEndTimeExceptionMessage("경매종료일자가 현재시각보다 빠릅니다."),
        eNotPriceSuggestionContentExceptionMessage("경매입찰내역이 없습니다."),
        eDeleted("삭제가 완료되었습니다.");
        private final String value;
    }
}
