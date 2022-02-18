package com.example.auctionapp.domain.pricesuggestion.presenter;

import org.json.JSONException;

import java.io.IOException;

public interface Presenter {
    void initializeData(Long itemId);
    void init(Long itemId) throws IOException, JSONException;
}
