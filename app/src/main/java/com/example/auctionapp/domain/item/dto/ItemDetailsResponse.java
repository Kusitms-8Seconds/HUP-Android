package com.example.auctionapp.domain.item.dto;

import com.example.auctionapp.domain.item.constant.ItemConstants;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemDetailsResponse {

    private Long id;
    private String itemName;
    private ItemConstants.EItemCategory category;
    private int initPrice;
    private int soldPrice;
    private LocalDateTime buyDate;
    private int itemStatePoint;
    private String description;
    private ItemConstants.EItemSoldStatus soldStatus;
    private List<String> fileNames;
    private LocalDateTime auctionClosingDate;

//    public static ItemDetailsResponse from(Item item) {
//        List<String> fileNames = new ArrayList<>();
//        if (item.getMyFiles().isEmpty() != true) {
//            List<MyFile> myFiles = item.getMyFiles();
//            fileNames = new ArrayList<>();
//            for (MyFile myFile : myFiles) {
//                fileNames.add(myFile.getFilename());
//            }
//        }
//        return ItemDetailsResponse.builder()
//                .id(item.getId())
//                .itemName(item.getItemName())
//                .category(item.getCategory())
//                .initPrice(item.getInitPrice())
//                .soldPrice(item.getSoldPrice())
//                .buyDate(item.getBuyDate())
//                .itemStatePoint(item.getItemStatePoint())
//                .description(item.getDescription())
//                .soldStatus(item.getSoldStatus())
//                .fileNames(fileNames)
//                .auctionClosingDate(item.getAuctionClosingDate())
//                .build();
//    }
}
