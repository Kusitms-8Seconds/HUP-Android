package com.example.auctionapp.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsInfoRequest {

    private Long userId;

    public static UserDetailsInfoRequest of(Long userId) {
        return UserDetailsInfoRequest.builder()
                .userId(userId)
                .build();
    }
}
