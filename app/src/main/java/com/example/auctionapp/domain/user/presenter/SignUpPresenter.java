package com.example.auctionapp.domain.user.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.databinding.ActivityLoginBinding;
import com.example.auctionapp.databinding.ActivitySignUpBinding;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.SignUpRequest;
import com.example.auctionapp.domain.user.dto.SignUpResponse;
import com.example.auctionapp.domain.user.view.Email;
import com.example.auctionapp.domain.user.view.LoginView;
import com.example.auctionapp.domain.user.view.SignUp;
import com.example.auctionapp.domain.user.view.SignUpView;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SignUpPresenter implements SignUpPresenterInterface{
    // Attributes
    private SignUpView signUpView;
    private ActivitySignUpBinding binding;
    private Context context;

    // Constructor
    public SignUpPresenter(SignUpView signUpView, ActivitySignUpBinding binding, Context context) {
        this.signUpView = signUpView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public Intent signUpCheck() {
        if(validLoginIdCheck()==false){
            return null;
        } else if(validPasswordCheck()==false){
            return null;
        } else if(validNameCheck()==false){
            return null;
        } else if(validEmailCheck()==false){
            return null;
        } else if(agreeCheck()==false){
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

            Intent intent = new Intent(context, Email.class);
            intent.putExtra("email", binding.edtEmail.getText().toString());
            return intent;
        }
    }

    @Override
    public boolean validLoginIdCheck() {
        String inputId = binding.edtUserId.getText().toString();
        if(inputId.length() < 5 || inputId.length() > 11){
            showToast(Constants.ESignUp.idWarningMessage.getText());
            return false; }
        return true;
    }

    @Override
    public boolean validEmailCheck() {
        String inputEmail = binding.edtEmail.getText().toString();
        Boolean emailFormatResult = Pattern.matches(Constants.ESignUp.emailFormat.getText(), inputEmail);
        if(emailFormatResult==false){
            showToast(Constants.ESignUp.emailFormatErrorMessage.getText());
            return false; }
        return true;
    }

    @Override
    public boolean validPasswordCheck() {
        String inputPW = binding.edtPasswordCheck.getText().toString();
        String inputCheckPW = binding.edtPassword.getText().toString();
        Boolean inputPWEnglishNumberResult = Pattern.matches(Constants.ESignUp.pwEnglishNumberFormat.getText(), inputPW);
        Boolean inputCheckPWEnglishNumberResult = Pattern.matches(Constants.ESignUp.pwEnglishNumberFormat.getText(), inputCheckPW);
        if(inputPW.length()==0 || inputCheckPW.length()==0){
            showToast(Constants.ESignUp.pwInputMessage.getText());
            return false;
        }
        if(inputPWEnglishNumberResult==false || inputCheckPWEnglishNumberResult==false){
            showToast(Constants.ESignUp.pwWarningMessage.getText());
            return false;
        }
        if(!inputPW.equals(inputCheckPW)){
            showToast(Constants.ESignUp.pwNotMatchMessage.getText());
            return false;
        }
        return true;
    }

    @Override
    public boolean validNameCheck() {
        String inputName = binding.edtNickname.getText().toString();
        if(inputName.length()==0){
            showToast(Constants.ESignUp.nameInputMessage.getText());
            return false;
        }
        if(inputName.length()<3 || inputName.length()>10 ){
            showToast(Constants.ESignUp.nameInput2Message.getText());
            return false;
        }
        return true;
    }

    @Override
    public boolean agreeCheck() {
        if((!binding.radioButton.isChecked())||(!binding.radioButton2.isChecked())||(!binding.radioButton3.isChecked())
                ||(!binding.radioButton4.isChecked())){
            showToast(Constants.ESignUp.agreeCheckMessage.getText());
            return false;
        }
        return true;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    class SignUpCallback implements MainRetrofitCallback<SignUpResponse> {

        @Override
        public void onSuccessResponse(Response<SignUpResponse> response) {
            Constants.userId = response.body().getUserId();
            System.out.println("SignUp_userId: "+Constants.userId);
        }
        @Override
        public void onFailResponse(Response<SignUpResponse> response) {
            try {

                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Log.d("error", jObjError.getString("message"));
                showToast(jObjError.getString("message"));
            } catch (Exception e) {
                Log.d("error", e.getMessage());

            }
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
