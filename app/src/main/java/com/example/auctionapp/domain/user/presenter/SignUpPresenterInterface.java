package com.example.auctionapp.domain.user.presenter;

import android.content.Intent;

public interface SignUpPresenterInterface {
    Intent signUpCheck();
    boolean validLoginIdCheck();
    void duplicateLoginIdCheck(String loginId);
    boolean validEmailCheck();
    boolean validPasswordCheck();
    boolean validNameCheck();
    boolean agreeCheck();
}
