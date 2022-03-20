package com.example.auctionapp.domain.upload.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.auctionapp.databinding.ActivityUploadPageBinding;
import com.example.auctionapp.domain.item.dto.RegisterItemResponse;
import com.example.auctionapp.domain.upload.constant.UploadConstants;
import com.example.auctionapp.domain.upload.view.UploadView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitConstants;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UploadPresenter implements Presenter{
    String selectedCategory;

    // Attributes
    private UploadView uploadView;
    private ActivityUploadPageBinding binding;
    private Context context;

    // Constructor
    public UploadPresenter(UploadView uploadView, ActivityUploadPageBinding binding, Context context){
        this.uploadView = uploadView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public String choiceCategory(String category) {
        switch (category){
            case "디지털 기기":
                selectedCategory = UploadConstants.ECategory.eDigital.getText();
                break;
            case "생활가전":
                selectedCategory = UploadConstants.ECategory.eHouseHoldAppliance.getText();
                break;
            case "가구/인테리어":
                selectedCategory = UploadConstants.ECategory.eFurnitureAndInterior.getText();
                break;
            case "유아동":
                selectedCategory = UploadConstants.ECategory.eChildren.getText();
                break;
            case "생활/가공식품":
                selectedCategory = UploadConstants.ECategory.eDailyLifeAndProcessedFood.getText();
                break;
            case "유아도서":
                selectedCategory = UploadConstants.ECategory.eChildrenBooks.getText();
                break;
            case "스포츠/레저":
                selectedCategory = UploadConstants.ECategory.eSportsAndLeisure.getText();
                break;
            case "여성잡화":
                selectedCategory = UploadConstants.ECategory.eMerchandiseForWoman.getText();
                break;
            case "여성의류":
                selectedCategory = UploadConstants.ECategory.eWomenClothing.getText();
                break;
            case "남성패션/잡화":
                selectedCategory = UploadConstants.ECategory.eManFashionAndMerchandise.getText();
                break;
            case "게임/취미":
                selectedCategory = UploadConstants.ECategory.eGameAndHabit.getText();
                break;
            case "뷰티/미용":
                selectedCategory = UploadConstants.ECategory.eBeauty.getText();
                break;
            case "반려동물용품":
                selectedCategory = UploadConstants.ECategory.ePetProducts.getText();
                break;
            case "도서/티켓/음반":
                selectedCategory = UploadConstants.ECategory.eBookTicketAlbum.getText();
                break;
            case "식물":
                selectedCategory = UploadConstants.ECategory.ePlant.getText();
                break;
            default:
        }
        return selectedCategory;
    }

    @Override
    public void upload(ArrayList<MultipartBody.Part> files, RequestBody userIdR, RequestBody itemNameR, RequestBody categoryR, RequestBody initPriceR, RequestBody buyDateR, RequestBody itemStatePointR, RequestBody auctionClosingDateR, RequestBody descriptionR) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).uploadItem(files, userIdR,
                itemNameR, categoryR, initPriceR, buyDateR, itemStatePointR,
                auctionClosingDateR, descriptionR)
                .enqueue(MainRetrofitTool.getCallback(new RegisterItemCallback()));
    }

    private class RegisterItemCallback implements MainRetrofitCallback<RegisterItemResponse> {
        @Override
        public void onSuccessResponse(Response<RegisterItemResponse> response) {
            RegisterItemResponse result = response.body();
            Log.d(UploadConstants.EUploadCallback.TAG.getText(), UploadConstants.EUploadCallback.rtSuccessResponse.getText() + result.toString());
            uploadView.showToast(UploadConstants.EUploadToast.uploadComplete.getText());
        }
        @Override
        public void onFailResponse(Response<RegisterItemResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            uploadView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(UploadConstants.EUploadCallback.TAG.getText(), UploadConstants.EUploadCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(UploadConstants.EUploadCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
