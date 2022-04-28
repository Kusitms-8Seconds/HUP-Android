package com.me.hurryuphup.domain.item.presenter;

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
}
