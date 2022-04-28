package com.me.hurryuphup.domain.user.dto;

import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotEmpty(message = "회원의 유저 Id를 입력해주세요.")
    private Long userId;

    @NotEmpty(message = "회원의 비밀번호를 입력해 주세요.")
    @Length(min = 8, max = 16, message = "패스워드는 8글자 이상 16글자 이하여야 합니다.")
    private String password;

    @NotEmpty(message = "회원의 비밀번호를 다시 한 번 입력해 주세요.")
    @Length(min = 8, max = 16, message = "패스워드는 8글자 이상 16글자 이하여야 합니다.")
    private String checkPassword;
}
