package com.me.hurryuphup.domain.scrap.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapRegisterRequest {

    private Long userId;
    private Long itemId;

    public static ScrapRegisterRequest of(Long userId, Long itemId) {
        return ScrapRegisterRequest.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
    }
}
