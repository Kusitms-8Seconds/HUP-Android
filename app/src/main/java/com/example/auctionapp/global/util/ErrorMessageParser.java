package com.example.auctionapp.global.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.Getter;

@Getter
public class ErrorMessageParser {

    JsonParser parser;
    JsonElement element;
    String errorMessage;
    String parsedErrorMessage;
    String expiredCode;

    public ErrorMessageParser(String errorResponse) {
        this.parser = new JsonParser();
        this.element = parser.parse(errorResponse);
//        if(element.getAsJsonObject().get("message").getAsJsonArray() != null) {
//            System.out.println("element: " + element.getAsJsonObject().get("messages"));
            this.errorMessage = element.getAsJsonObject().get("messages").getAsJsonArray().get(0).toString();
            this.parsedErrorMessage = this.errorMessage.substring(1, this.errorMessage.length()-1);
            if(this.parsedErrorMessage.equals("만료된 토큰입니다.")) this.expiredCode = "EXPIRED_TOKEN";
//        }
//        else {
//            this.errorMessage = "null";
//            this.parsedErrorMessage = "null";
//        }
    }
}
