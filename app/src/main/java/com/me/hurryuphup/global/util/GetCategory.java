package com.me.hurryuphup.global.util;

import lombok.Getter;

@Getter
public class GetCategory {
    String category;
    public GetCategory(String category) {
        this.category = getCategory(category);
    }
//    eDigital("디지털 기기"),
//    eHouseHoldAppliance("생활가전"),
//    eFurnitureAndInterior("가구/인테리어"),
//    eChildren("유아동"),
//    eDailyLifeAndProcessedFood("생활/가공식품"),
//    eChildrenBooks("유아도서"),
//    eSportsAndLeisure("스포츠/레저"),
//    eMerchandiseForWoman("여성잡화"),
//    eWomenClothing("여성의류"),
//    eManFashionAndMerchandise("남성패션/잡화"),
//    eGameAndHabit("게임/취미"),
//    eBeauty("뷰티/미용"),
//    ePetProducts("반려동물용품"),
//    eBookTicketAlbum("도서/티켓/음반"),
//    ePlant("식물");

    public String getCategory(String category) {
        String str = "";
        switch (category) {
            case "디지털 기기":
                str = "eDigital";
                break;
            case "생활가전":
                str = "eHouseHoldAppliance";
                break;
            case "가구/인테리어":
                str = "eFurnitureAndInterior";
                break;
            case "유아동":
                str = "eChildren";
                break;
            case "생활/가공식품":
                str = "eDailyLifeAndProcessedFood";
                break;
            case "유아도서":
                str = "eChildrenBooks";
                break;
            case "스포츠/레저":
                str = "eSportsAndLeisure";
                break;
            case "여성잡화":
                str = "eMerchandiseForWoman";
                break;
            case "여성의류":
                str = "eWomenClothing";
                break;
            case "남성패션/잡화":
                str = "eManFashionAndMerchandise";
                break;
            case "게임/취미":
                str = "eGameAndHabit";
                break;
            case "뷰티/미용":
                str = "eBeauty";
                break;
            case "반려동물용품":
                str = "ePetProducts";
                break;
            case "도서/티켓/음반":
                str = "eBookTicketAlbum";
                break;
            case "식물":
                str = "ePlant";
                break;

        }
        return str;
    }
}
