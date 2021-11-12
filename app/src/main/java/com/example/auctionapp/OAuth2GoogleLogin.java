package com.example.auctionapp;

import com.google.gson.annotations.SerializedName;

public class OAuth2GoogleLogin {

    @SerializedName("idToken")
    private String idToken;

    // toString()을 Override 해주지 않으면 객체 주소값을 출력함
    @Override
    public String toString() {
        return "PostResult{" +
                "idToken=" + idToken +
                '}';
    }
}