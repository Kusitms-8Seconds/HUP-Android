package com.example.auctionapp.domain.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.auctionapp.databinding.ActivityMypageMyinfoBinding;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.mypage.presenter.MypagePresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MyInfo extends AppCompatActivity {
    private ActivityMypageMyinfoBinding binding;
//    SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMypageMyinfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new UserDetailsInfoCallback()));
        binding.editMyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChangeInfo.class);
                startActivity(intent);
            }
        });
    }
    public class UserDetailsInfoCallback implements MainRetrofitCallback<UserInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            binding.tvUsername.setText(response.body().getUsername());
            binding.tvLoginId.setText(response.body().getLoginId());
            binding.tvUserId.setText(response.body().getUserId()+"");
            binding.tvPhoneNumber.setText(response.body().getPhoneNumber());

            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<UserInfoResponse> response) throws IOException, JSONException {
//            exceptionToast(MypageConstants.EMyPageCallback.eUserDetailsInfoCallback.getText(), response.code());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                Toast.makeText(activity, jObjError.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
