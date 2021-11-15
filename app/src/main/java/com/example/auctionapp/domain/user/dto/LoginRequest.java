package com.example.auctionapp.domain.user.dto;

import androidx.annotation.Size;

import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "입력해주세요.")
    @Length(min = 5, max = 11)
    private String loginId;

    @NotEmpty(message = "입력해주세요.")
    @Length(min = 8, max = 16)
    private String password;
}
