package com.example.auctionapp.domain.item.dto;

import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllItemsByStatusRequest {

    private Long userId;
    private ItemConstants.EItemSoldStatus soldStatus;

    public static GetAllItemsByStatusRequest of(Long userId, ItemConstants.EItemSoldStatus soldStatus) {
        return GetAllItemsByStatusRequest.builder()
                .userId(userId)
                .soldStatus(soldStatus)
                .build();
    }

}
