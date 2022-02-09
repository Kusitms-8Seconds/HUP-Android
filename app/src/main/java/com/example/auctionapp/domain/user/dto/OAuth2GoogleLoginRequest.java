package com.example.auctionapp.domain.user.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

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