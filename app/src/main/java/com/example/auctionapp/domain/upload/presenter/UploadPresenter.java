package com.example.auctionapp.domain.upload.presenter;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.auctionapp.domain.item.dto.RegisterItemResponse;
import com.example.auctionapp.domain.upload.view.UploadPage;
import com.example.auctionapp.domain.upload.view.UploadView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UploadPresenter implements Presenter{
    private final UploadView view;
    String selectedCategory;

    public UploadPresenter(UploadView view) {
        this.view = view;
    }

    @Override
    public String choiceCategory(String category) {
        switch (category){
            case "디지털 기기":
                selectedCategory = "eDigital";
                break;
            case "생활가전":
                selectedCategory = "eHouseHoldAppliance";
                break;
            case "가구/인테리어":
                selectedCategory = "eFurnitureAndInterior";
                break;
            case "유아동":
                selectedCategory = "eChildren";
                break;
            case "생활/가공식품":
                selectedCategory = "eDailyLifeAndProcessedFood";
                break;
            case "유아도서":
                selectedCategory = "eChildrenBooks";
                break;
            case "스포츠/레저":
                selectedCategory = "eSportsAndLeisure";
                break;
            case "여성잡화":
                selectedCategory = "eMerchandiseForWoman";
                break;
            case "여성의류":
                selectedCategory = "eWomenClothing";
                break;
            case "남성패션/잡화":
                selectedCategory = "eManFashionAndMerchandise";
                break;
            case "게임/취미":
                selectedCategory = "eGameAndHabit";
                break;
            case "뷰티/미용":
                selectedCategory = "eBeauty";
                break;
            case "반려동물용품":
                selectedCategory = "ePetProducts";
                break;
            case "도서/티켓/음반":
                selectedCategory = "eBookTicketAlbum";
                break;
            case "식물":
                selectedCategory = "ePlant";
                break;
            default:
        }
        return selectedCategory;
    }

    @Override
    public void upload(ArrayList<MultipartBody.Part> files, RequestBody userIdR, RequestBody itemNameR, RequestBody categoryR, RequestBody initPriceR, RequestBody buyDateR, RequestBody itemStatePointR, RequestBody auctionClosingDateR, RequestBody descriptionR) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).uploadItem(files, userIdR,
                itemNameR, categoryR, initPriceR, buyDateR, itemStatePointR,
                auctionClosingDateR, descriptionR)
                .enqueue(MainRetrofitTool.getCallback(new RegisterItemCallback()));
    }
    private class RegisterItemCallback implements MainRetrofitCallback<RegisterItemResponse> {
        @Override
        public void onSuccessResponse(Response<RegisterItemResponse> response) {
            RegisterItemResponse result = response.body();
            Log.d("UploadPage", "retrofit success, idToken: " + result.toString());

        }
        @Override
        public void onFailResponse(Response<RegisterItemResponse> response) {
            Log.d("UploadPage", "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
