package com.me.hurryuphup.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2KakaoLoginRequest {

    private String accessToken;
    private String targetToken;
}
