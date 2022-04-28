package com.me.hurryuphup.global.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.me.hurryuphup.MainActivity;
import com.me.hurryuphup.domain.mypage.constant.MypageConstants;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.domain.user.dto.LogoutRequest;
import com.me.hurryuphup.domain.user.dto.TokenInfoResponse;
import com.me.hurryuphup.domain.user.view.Login;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.io.IOException;

import lombok.Getter;
import retrofit2.Response;

@Getter
public class ErrorMessageParser {

    JsonParser parser;
    JsonElement element;
    String errorMessage;
    String parsedErrorMessage;
    String error;
    Context context;

    public ErrorMessageParser(String errorResponse, Context context) {
        this.context = context;
        this.parser = new JsonParser();
        this.element = parser.parse(errorResponse);
        if(element.getAsJsonObject().get("error") != null) {
            this.error = element.getAsJsonObject().get("error").getAsString();
            if (error.equals(Constants.EUserServiceImpl.e403Error.getValue())) {
                showToast(Constants.EUserServiceImpl.eNotActivatedEmailAuthExceptionMessage.getValue());
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
                return;
            }
        }
        this.errorMessage = element.getAsJsonObject().get("messages").getAsJsonArray().get(0).toString();
        this.parsedErrorMessage = this.errorMessage.substring(1, this.errorMessage.length() - 1);
        if (this.parsedErrorMessage.equals(Constants.EUserServiceImpl.eNotValidAccessTokenExceptionMessage.getValue()) || this.parsedErrorMessage.equals(Constants.EOAuth2UserServiceImpl.eGoogleInvalidIdTokenMessage.getValue())) {
            LogoutRequest logoutRequest = new LogoutRequest(Constants.accessToken, Constants.refreshToken);
            RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).reissue(logoutRequest)
                    .enqueue(MainRetrofitTool.getCallback(new ReissueCallback()));
            showToast(this.parsedErrorMessage);

        } else if (this.parsedErrorMessage.equals(Constants.EUserServiceImpl.eNotValidRefreshTokenExceptionMessage.getValue())) {
            Intent intent = new Intent(context, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
    }
    public class ReissueCallback implements MainRetrofitCallback<TokenInfoResponse> {
        @Override
        public void onSuccessResponse(Response<TokenInfoResponse> response) {
            Constants.accessToken = response.body().getAccessToken();
            Constants.refreshToken = response.body().getRefreshToken();
            Log.d("REISSUE", MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<TokenInfoResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            Log.d("REISSUE", errorMessageParser.getParsedErrorMessage());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
