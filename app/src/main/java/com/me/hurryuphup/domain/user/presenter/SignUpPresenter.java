package com.me.hurryuphup.domain.user.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.me.hurryuphup.databinding.ActivitySignUpBinding;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.domain.user.dto.SignUpRequest;
import com.me.hurryuphup.domain.user.dto.SignUpResponse;
import com.me.hurryuphup.domain.email.view.Email;
import com.me.hurryuphup.domain.user.view.SignUpView;
import com.me.hurryuphup.global.dto.DefaultResponse;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;
import com.me.hurryuphup.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SignUpPresenter implements SignUpPresenterInterface{
    boolean isSignUp = false;
    // Attributes
    private SignUpView signUpView;
    private ActivitySignUpBinding binding;
    private Context context;
    ErrorMessageParser errorMessageParser;

    // Constructor
    public SignUpPresenter(SignUpView signUpView, ActivitySignUpBinding binding, Context context) {
        this.signUpView = signUpView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public Intent signUpCheck() {
        if(!validLoginIdCheck()){
            return null;
        } else if(!validPasswordCheck()){
            return null;
        } else if(!validNameCheck()){
            return null;
        } else if(!validEmailCheck()){
            return null;
        } else if(!agreeCheck()){
            return null;
        } else{
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .loginId(binding.edtUserId.getText().toString())
                    .email(binding.edtEmail.getText().toString())
                    .username(binding.edtNickname.getText().toString())
                    .password(binding.edtPassword.getText().toString())
                    .phoneNumber(binding.edtPhoneNum.getText().toString())
                    .build();
            RetrofitTool.getAPIWithNullConverter().signup(signUpRequest)
                    .enqueue(MainRetrofitTool.getCallback(new SignUpCallback()));

            if(isSignUp) {
                Intent intent = new Intent(context, Email.class);
                intent.putExtra("email", binding.edtEmail.getText().toString());
                return intent;
            }
            else return null;
        }
    }

    @Override
    public boolean validLoginIdCheck() {
        String inputId = binding.edtUserId.getText().toString();
        if(inputId.length() < 5 || inputId.length() > 11){
            signUpView.showToast(Constants.ESignUp.idWarningMessage.getText());
            return false; }
        return true;
    }

    @Override
    public void duplicateLoginIdCheck(String loginId) {
        RetrofitTool.getAPIWithNullConverter().checkDuplicateId(loginId)
                .enqueue(MainRetrofitTool.getCallback(new checkDuplicateIdCheck()));
    }

    @Override
    public boolean validEmailCheck() {
        String inputEmail = binding.edtEmail.getText().toString();
        Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
        if(pattern.matcher(inputEmail).matches()) {
            return true;
        } else {
            signUpView.showToast(Constants.ESignUp.emailFormatErrorMessage.getText());
            return false;
        }
    }

    @Override
    public boolean validPasswordCheck() {
        String inputPW = binding.edtPasswordCheck.getText().toString();
        String inputCheckPW = binding.edtPassword.getText().toString();
        Boolean inputPWEnglishNumberResult = Pattern.matches(Constants.ESignUp.pwEnglishNumberFormat.getText(), inputPW);
        Boolean inputCheckPWEnglishNumberResult = Pattern.matches(Constants.ESignUp.pwEnglishNumberFormat.getText(), inputCheckPW);
        if(inputPW.length()==0 || inputCheckPW.length()==0){
            signUpView.showToast(Constants.ESignUp.pwInputMessage.getText());
            return false;
        }
        if(inputPWEnglishNumberResult==false || inputCheckPWEnglishNumberResult==false){
            signUpView.showToast(Constants.ESignUp.pwWarningMessage.getText());
            return false;
        }
        if(!inputPW.equals(inputCheckPW)){
            signUpView.showToast(Constants.ESignUp.pwNotMatchMessage.getText());
            return false;
        }
        return true;
    }

    @Override
    public boolean validNameCheck() {
        String inputName = binding.edtNickname.getText().toString();
        if(inputName.length()==0){
            signUpView.showToast(Constants.ESignUp.nameInputMessage.getText());
            return false;
        }
        if(inputName.length()<3 || inputName.length()>10 ){
            signUpView.showToast(Constants.ESignUp.nameInput2Message.getText());
            return false;
        }
        return true;
    }

    @Override
    public boolean agreeCheck() {
        if((!binding.radioButton.isChecked())||(!binding.radioButton2.isChecked())||(!binding.radioButton3.isChecked())
                ||(!binding.radioButton4.isChecked())){
            signUpView.showToast(Constants.ESignUp.agreeCheckMessage.getText());
            return false;
        }
        return true;
    }

    class SignUpCallback implements MainRetrofitCallback<SignUpResponse> {

        @Override
        public void onSuccessResponse(Response<SignUpResponse> response) {
            Constants.userId = response.body().getUserId();
            signUpView.showToast(Constants.EUserServiceImpl.eSuccessSignUpMessage.getValue());
            isSignUp = true;
            System.out.println("SignUp_userId: "+Constants.userId);
        }
        @Override
        public void onFailResponse(Response<SignUpResponse> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            isSignUp = false;
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
    class checkDuplicateIdCheck implements MainRetrofitCallback<DefaultResponse> {

        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            signUpView.showToast(response.body().getMessage());
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
