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
public class UserInfoResponse {

    private Long userId;
    private String loginId;
    private String email;
    private String username;
    private String phoneNumber;
    private String picture;
    private boolean activated;
    private Constants.ELoginType loginType;

}
