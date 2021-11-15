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

    public OAuth2GoogleLoginRequest(String idToken) {
        this.idToken = idToken;
    }
}