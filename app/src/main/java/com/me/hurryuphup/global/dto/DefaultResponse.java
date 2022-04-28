package com.me.hurryuphup.global.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultResponse {
    private String message;
    private DefaultResponse(String message) {
        this.message = message;
    }
    public static DefaultResponse from(String message) {
        return new DefaultResponse(message);
    }
}
