package com.me.hurryuphup.domain.user.dto;

import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotEmpty(message = "회원의 로그인Id를 입력해주세요.")
    @Length(min = 5, max = 11, message = "로그인 Id는 크기가 5에서 11사이여야 합니다.")
    private String loginId;

    @NotEmpty(message = "회원의 비밀번호를 입력해 주세요.")
    //@Size(min = 8, max = 16, message = "패스워드는 8글자 이상 16글자 이하여야 합니다.")
    private String password;

    private String targetToken;

    public static LoginRequest of(String loginId, String password, String targetToken) {
        return LoginRequest.builder()
                .loginId(loginId)
                .password(password)
                .targetToken(targetToken)
                .build();
    }
}
