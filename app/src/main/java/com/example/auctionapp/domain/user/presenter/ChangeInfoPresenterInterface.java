package com.example.auctionapp.domain.user.presenter;

import android.content.Intent;

public interface ChangeInfoPresenterInterface {
    Intent UpdateCheck();
    boolean validLoginIdCheck();
    void duplicateLoginIdCheck(String loginId);
    boolean validPasswordCheck();
    boolean validNameCheck();
}
