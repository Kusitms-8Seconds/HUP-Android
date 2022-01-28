package com.example.auctionapp.domain.item.presenter;

import com.example.auctionapp.domain.user.constant.Constants;

public interface ItemDetailPresenterInterface {
    void init(Long itemId);
    void initializeImageData();
    void qnaInit();

    void heartCheckCallback(Long userId, Long itemId);
    void deleteHeartCallback(Long scrapId);
    void createHeartCallback(Long userId, Long itemId);
    void getItemInfoCallback(Long itemId);
    void getUserInfoCallback(Long userId);
    void deleteItem(Long itemId);

    void exceptionToast(String tag, int statusCode);
}
