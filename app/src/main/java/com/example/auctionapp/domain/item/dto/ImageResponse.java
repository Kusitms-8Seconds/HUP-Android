package com.example.auctionapp.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {

    private byte[] imageByte;

    public static ImageResponse from(byte[] imageByte) {
        return ImageResponse.builder()
                .imageByte(imageByte)
                .build();
    }
}
