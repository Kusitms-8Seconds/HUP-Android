package com.example.auctionapp.domain.user.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.databinding.ActivityChangeInfoBinding;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.mypage.presenter.MypagePresenter;
import com.example.auctionapp.domain.upload.constant.UploadConstants;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UpdateProfileImgRequest;
import com.example.auctionapp.domain.user.dto.UpdateProfileResponse;
import com.example.auctionapp.domain.user.dto.UpdateUserRequest;
import com.example.auctionapp.domain.user.dto.UpdateUserResponse;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.domain.user.view.ChangeInfoView;
import com.example.auctionapp.global.dto.DefaultResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ChangeInfoPresenter implements ChangeInfoPresenterInterface{
    boolean isValidId;
    // Attributes
    private ChangeInfoView changeInfoView;
    private ActivityChangeInfoBinding binding;
    private Context context;

    // Constructor
    public ChangeInfoPresenter(ChangeInfoView changeInfoView, ActivityChangeInfoBinding binding, Context context){
        this.changeInfoView = changeInfoView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void init() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).userDetails(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new UserDetailsInfoCallback()));
    }

    @Override
    public Intent UpdateCheck(String imagePath) {
        if(validLoginIdCheck()==false){
            return null;
        } else if(validPasswordCheck()==false){
            return null;
        } else if(validNameCheck()==false){
            return null;
        } else if(!isValidId){
            return null;
        } else{
            UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                    .userId(Constants.userId)
                    .loginId(binding.edtLoginId.getText().toString())
                    .username(binding.edtNickname.getText().toString())
                    .password(binding.edtPassword.getText().toString())
                    .phoneNumber(binding.edtPhoneNum.getText().toString())
                    .build();
            RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).updateUser(updateUserRequest)
                    .enqueue(MainRetrofitTool.getCallback(new UpdateCallback()));
            //profile img
            File profileFile = new File(imagePath);
            // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), profileFile);
            // 사진 파일 이름
            LocalDateTime localDateTime = LocalDateTime.now();
            String fileName = "photo" + localDateTime + ".jpg";
            // RequestBody로 Multipart.Part 객체 생성
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", fileName, fileBody);
            RequestBody userIdR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Constants.userId));
            RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).updateUserProfileImg(filePart, userIdR)
                    .enqueue(MainRetrofitTool.getCallback(new UpdateProfileImgCallback()));

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;
        }
    }

    @Override
    public boolean validLoginIdCheck() {
        String inputId = binding.edtLoginId.getText().toString();
        if(inputId.length() < 5 || inputId.length() > 11){
            changeInfoView.showToast(Constants.ESignUp.idWarningMessage.getText());
            return false; }
        return true;
    }

    @Override
    public void duplicateLoginIdCheck(String loginId) {
        RetrofitTool.getAPIWithNullConverter().checkDuplicateId(loginId)
                .enqueue(MainRetrofitTool.getCallback(new checkDuplicateIdCheck()));
    }

    @Override
    public boolean validPasswordCheck() {
        String inputPW = binding.edtPasswordCheck.getText().toString();
        String inputCheckPW = binding.edtPassword.getText().toString();
        Boolean inputPWEnglishNumberResult = Pattern.matches(Constants.ESignUp.pwEnglishNumberFormat.getText(), inputPW);
        Boolean inputCheckPWEnglishNumberResult = Pattern.matches(Constants.ESignUp.pwEnglishNumberFormat.getText(), inputCheckPW);
        if(inputPW.length()==0 || inputCheckPW.length()==0){
            changeInfoView.showToast(Constants.ESignUp.pwInputMessage.getText());
            return false;
        }
        if(inputPWEnglishNumberResult==false || inputCheckPWEnglishNumberResult==false){
            changeInfoView.showToast(Constants.ESignUp.pwWarningMessage.getText());
            return false;
        }
        if(!inputPW.equals(inputCheckPW)){
            changeInfoView.showToast(Constants.ESignUp.pwNotMatchMessage.getText());
            return false;
        }
        return true;
    }

    @Override
    public boolean validNameCheck() {
        String inputName = binding.edtNickname.getText().toString();
        if(inputName.length()==0){
            changeInfoView.showToast(Constants.ESignUp.nameInputMessage.getText());
            return false;
        }
        if(inputName.length()<3 || inputName.length()>10 ){
            changeInfoView.showToast(Constants.ESignUp.nameInput2Message.getText());
            return false;
        }
        return true;
    }

    class UpdateCallback implements MainRetrofitCallback<UpdateUserResponse> {

        @Override
        public void onSuccessResponse(Response<UpdateUserResponse> response) {
//            Constants.userId = response.body().getUserId();
            System.out.println("UpdateUser: "+Constants.userId);
        }
        @Override
        public void onFailResponse(Response<UpdateUserResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            changeInfoView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
    class UpdateProfileImgCallback implements MainRetrofitCallback<UpdateProfileResponse> {
        @Override
        public void onSuccessResponse(Response<UpdateProfileResponse> response) {
//            Constants.userId = response.body().getUserId();
            System.out.println("UpdateProfileImg: "+Constants.userId);
            System.out.println("UpdateProfileImgURL: "+response.body().getProfileImageURL());
        }
        @Override
        public void onFailResponse(Response<UpdateProfileResponse> response) throws IOException, JSONException {
//            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
//            changeInfoView.showToast(errorMessageParser.getParsedErrorMessage());
//            Log.d(TAG, "UpdateProfileImg: onFailResponse: " + response.errorBody().string());
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Log.d("UpdateProfileImg fail", jObjError.getString("error"));
            } catch (Exception e) {
                Log.d("UpdateProfileImg fail", e.getMessage());
            }
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
    class checkDuplicateIdCheck implements MainRetrofitCallback<DefaultResponse> {

        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            changeInfoView.showToast(response.body().getMessage());
            isValidId = true;
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            changeInfoView.showToast(errorMessageParser.getParsedErrorMessage());
            isValidId = false;
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
    public class UserDetailsInfoCallback implements MainRetrofitCallback<UserInfoResponse> {
        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            if(response.body().getPicture()!=null){
                Glide.with(context).load(response.body().getPicture()).into(binding.ivProfileChange);
            }
            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<UserInfoResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            changeInfoView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
