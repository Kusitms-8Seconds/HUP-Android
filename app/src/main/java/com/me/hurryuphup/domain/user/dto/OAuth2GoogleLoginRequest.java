package com.me.hurryuphup.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuth2GoogleLoginRequest {
    
    private String idToken;
    private String targetToken;

    public OAuth2GoogleLoginRequest(String idToken, String targetToken) {
        this.idToken = idToken;
        this.targetToken = targetToken;
    }
}