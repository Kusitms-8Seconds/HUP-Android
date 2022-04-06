package com.example.auctionapp.global.util;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.mypage.presenter.MypagePresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.LogoutRequest;
import com.example.auctionapp.domain.user.dto.TokenInfoResponse;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.io.IOException;

import lombok.Getter;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

@Getter
public class ErrorMessageParser {

    JsonParser parser;
    JsonElement element;
    String errorMessage;
    String parsedErrorMessage;

    public ErrorMessageParser(String errorResponse) {
        this.parser = new JsonParser();
        this.element = parser.parse(errorResponse);
        this.errorMessage = element.getAsJsonObject().get("messages").getAsJsonArray().get(0).toString();
        this.parsedErrorMessage = this.errorMessage.substring(1, this.errorMessage.length()-1);
        if(this.parsedErrorMessage.equals("만료된 토큰입니다.") || this.parsedErrorMessage.equals(Constants.EOAuth2UserServiceImpl.eGoogleInvalidIdTokenMessage.getValue())) {
            LogoutRequest logoutRequest = new LogoutRequest(Constants.accessToken, Constants.refreshToken);
            RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).reissue(logoutRequest)
                    .enqueue(MainRetrofitTool.getCallback(new ReissueCallback()));
//            Thread.sleep(1000);
        }
    }
    public class ReissueCallback implements MainRetrofitCallback<TokenInfoResponse> {
        @Override
        public void onSuccessResponse(Response<TokenInfoResponse> response) {
            Constants.accessToken = response.body().getAccessToken();
            Constants.refreshToken = response.body().getRefreshToken();
            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<TokenInfoResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
//            mypageView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, errorMessageParser.getParsedErrorMessage());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
