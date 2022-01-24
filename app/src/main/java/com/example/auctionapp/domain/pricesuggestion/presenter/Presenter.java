package com.example.auctionapp.domain.pricesuggestion.presenter;

public interface Presenter {
    void initializeData(Long itemId);
    void init();

    void exceptionToast(int statusCode);
}
