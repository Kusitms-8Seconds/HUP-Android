package com.me.hurryuphup.domain.user.dto;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class LogoutRequest {
    @NotEmpty(message = "잘못된 요청입니다.")
    private String accessToken;

    @NotEmpty(message = "잘못된 요청입니다.")
    private String refreshToken;

    public LogoutRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}