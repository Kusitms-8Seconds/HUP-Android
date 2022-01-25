package com.example.auctionapp.domain.user.presenter;

import android.content.Intent;

public interface ChangeInfoPresenterInterface {
    Intent UpdateCheck();
    boolean validLoginIdCheck();
    boolean validPasswordCheck();
    boolean validNameCheck();
    void showToast(String message);
}
