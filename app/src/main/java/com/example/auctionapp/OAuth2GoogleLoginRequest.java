package com.example.auctionapp;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2GoogleLoginRequest {

    @SerializedName("idToken")
    private String idToken;
}