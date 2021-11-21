package com.example.auctionapp.domain.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapCheckedRequest {

    private Long userId;
    private Long itemId;

    public static ScrapCheckedRequest of(Long userId, Long itemId) {
            return ScrapCheckedRequest.builder()
                    .userId(userId)
                    .itemId(itemId)
                    .build();
        }
    }
