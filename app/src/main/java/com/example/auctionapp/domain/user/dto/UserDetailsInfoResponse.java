package com.example.auctionapp.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsInfoResponse {

    private Long userId;
    private String loginId;
    private String email;
    private String username;
    private String phoneNumber;
    private String picture;
    private boolean activated;

}
