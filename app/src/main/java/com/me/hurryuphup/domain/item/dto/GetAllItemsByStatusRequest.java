package com.me.hurryuphup.domain.item.dto;

import com.me.hurryuphup.domain.item.constant.ItemConstants;

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
