package com.example.auctionapp.domain.user.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityMypageMyinfoBinding;
import com.example.auctionapp.domain.chat.dto.DeleteChatRoomRequest;
import com.example.auctionapp.domain.chat.model.chatListData;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.dto.DefaultResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import lombok.SneakyThrows;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MyInfo extends AppCompatActivity {
    private ActivityMypageMyinfoBinding binding;
    String myLoginType;
    ErrorMessageParser errorMessageParser;

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
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfo.this);
                builder.setTitle("사용자 탈퇴").setMessage("탈퇴하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                    @SneakyThrows
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).deleteUser(Constants.userId)
                                .enqueue(MainRetrofitTool.getCallback(new DeleteUserCallback()));
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) { }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public class UserDetailsInfoCallback implements MainRetrofitCallback<UserInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            String ptImage = "";
            if(response.body().getPicture()!=null) ptImage = response.body().getPicture();
            else
                ptImage = "https://firebasestorage.googleapis.com/v0/b/auctionapp-f3805.appspot.com/o/profile.png?alt=media&token=655ed158-b464-4e5e-aa56-df3d7f12bdc8";
            Glide.with(getApplicationContext()).load(ptImage).into(binding.ivMyprofile);
            binding.tvUsername.setText(response.body().getUsername());
            binding.tvLoginId.setText(response.body().getLoginId());
            binding.tvPhoneNumber.setText(response.body().getPhoneNumber());
            myLoginType = response.body().getLoginType().getText();
            binding.tvLoginType.setText(myLoginType);

            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<UserInfoResponse> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), getApplicationContext());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    public class DeleteUserCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            showToast("탈퇴되었습니다.");
            Constants.userId = null;
            Constants.accessToken = null;
            Constants.refreshToken = null;
            Intent intent = new Intent(MyInfo.this, MainActivity.class);
            startActivity(intent);
            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), getApplicationContext());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
