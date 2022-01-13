package com.example.auctionapp.domain.upload.view;

import android.net.Uri;

public interface UploadView {
    void makeMultiPart();
    String getRealpath(Uri uri);
}
