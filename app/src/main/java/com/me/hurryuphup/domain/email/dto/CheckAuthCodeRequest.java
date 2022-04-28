package com.me.hurryuphup.domain.email.dto;

import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckAuthCodeRequest {
    @NotEmpty(message = "인증코드를 입력해주세요.")
    @Length(min = 8, max = 8, message = "인증코드는 8자리여야 합니다.")
    private String authCode;
}
