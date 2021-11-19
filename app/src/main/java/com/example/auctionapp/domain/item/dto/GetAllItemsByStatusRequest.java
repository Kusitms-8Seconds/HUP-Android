package com.example.auctionapp.domain.item.dto;

import com.example.auctionapp.domain.item.constant.ItemConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllItemsByStatusRequest {

    private Long userId;
    private ItemConstants.EItemSoldStatus soldStatus;

}
