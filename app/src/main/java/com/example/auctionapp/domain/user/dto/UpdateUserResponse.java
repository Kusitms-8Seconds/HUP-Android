package com.example.auctionapp.domain.user.dto;

import com.example.auctionapp.domain.user.constant.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserResponse {

    private Long userId;
    private String loginId;
    private String email;
    private String username;
    private String phoneNumber;
    private String picture;
    private boolean activated;
    private Constants.ELoginType loginType;

    public static UpdateUserResponse from(Long userId, String loginId, String email, String username, String phoneNumber,
                                          String picture, boolean activated, Constants.ELoginType loginType) {
        return UpdateUserResponse.builder()
                .userId(userId)
                .loginId(loginId)
                .email(email)
                .username(username)
                .phoneNumber(phoneNumber)
                .picture(picture)
                .activated(activated)
                .loginType(loginType)
                .build();
    }
}