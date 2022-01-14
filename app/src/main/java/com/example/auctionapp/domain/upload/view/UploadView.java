package com.example.auctionapp.domain.upload.view;

import android.net.Uri;

public interface UploadView {
    void showToast(String message);
    void makeMultiPart();
    String getRealpath(Uri uri);
}
