package com.me.hurryuphup.domain.upload.presenter;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface Presenter {
    String choiceCategory(String category);
    void upload(ArrayList<MultipartBody.Part> files, RequestBody userIdR,
                RequestBody itemNameR, RequestBody categoryR, RequestBody initPriceR,
                RequestBody buyDateR, RequestBody itemStatePointR,
                RequestBody auctionClosingDateR, RequestBody descriptionR);
}
