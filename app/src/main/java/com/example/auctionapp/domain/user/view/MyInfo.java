package com.example.auctionapp.domain.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivityMypageMyinfoBinding;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MyInfo extends AppCompatActivity {
    private ActivityMypageMyinfoBinding binding;
    String myLoginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMypageMyinfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).userDetails(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new UserDetailsInfoCallback()));

        binding.editMyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!myLoginType.equals("앱")) {
                    Toast.makeText(getApplicationContext(), myLoginType + "로그인은 정보수정이 불가합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ChangeInfo.class);
                    startActivity(intent);
                }
            }
        });
    }
    public class UserDetailsInfoCallback implements MainRetrofitCallback<UserInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            binding.tvUsername.setText(response.body().getUsername());
            binding.tvLoginId.setText(response.body().getLoginId());
//            binding.tvUserId.setText(response.body().getUserId()+"");
            binding.tvPhoneNumber.setText(response.body().getPhoneNumber());
            myLoginType = response.body().getLoginType().getText();
            binding.tvLoginType.setText(myLoginType);

            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<UserInfoResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
