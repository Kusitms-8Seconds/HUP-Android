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

    public ErrorMessageParser(String errorResponse) {
        this.parser = new JsonParser();
        this.element = parser.parse(errorResponse);
        this.errorMessage = element.getAsJsonObject().get("messages").getAsJsonArray().get(0).toString();
        this.parsedErrorMessage = this.errorMessage.substring(1, this.errorMessage.length()-1);
    }
}
