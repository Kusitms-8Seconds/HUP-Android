package com.example.auctionapp.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileResponse {
    private String profileImageURL;

    public static UpdateProfileResponse from(String fileURL) {
        return UpdateProfileResponse.builder()
                .profileImageURL(fileURL)
                .build();
    }
}