package com.example.auctionapp.domain.user.presenter;

import android.content.Intent;

public interface ChangeInfoPresenterInterface {
    void init();

    Intent UpdateCheck(String imagePath);
    boolean validLoginIdCheck();
    void duplicateLoginIdCheck(String loginId);
    boolean validPasswordCheck();
    boolean validNameCheck();
}
