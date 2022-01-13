package com.example.auctionapp.domain.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityChatRoomBinding;
import com.example.auctionapp.databinding.ActivitySignUpBinding;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.constant.Constants.ESignUp;
import com.example.auctionapp.domain.user.dto.SignUpRequest;
import com.example.auctionapp.domain.user.dto.SignUpResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! binding.radioButton5.isChecked()){
                    binding.radioButton.setChecked(false);
                    binding.radioButton2.setChecked(false);
                    binding.radioButton3.setChecked(false);
                    binding.radioButton4.setChecked(false);
                }else {
                    binding.radioButton.setChecked(true);
                    binding.radioButton2.setChecked(true);
                    binding.radioButton3.setChecked(true);
                    binding.radioButton4.setChecked(true);
                }
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = signUpCheck();
                if(intent !=null){
                        showToast("회원가입을 성공했습니다.");
                        startActivity(intent); } }
            });

    }

    public Intent signUpCheck() {

        if(validLoginIdCheck()==false){
            return null;
        } else if(validPasswordCheck()==false){
            return null;
        } else if(validNameCheck()==false){
            return null;
        } else if(phoneNumberCheck()==false){
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

            Intent intent = new Intent(SignUp.this, MainActivity.class);
            return intent;
        }
    }

    private boolean validLoginIdCheck() {
        String inputId = binding.edtUserId.getText().toString();
        if(inputId.length() < 5 || inputId.length() > 11){
            showToast("아이디는 최소 5글자 이상 10글자 이하여야 합니다.");
            return false; }
        return true;
        }

    public boolean validEmailCheck(){
        String inputEmail = binding.edtEmail.getText().toString();
        Boolean emailFormatResult = Pattern.matches(ESignUp.emailFormat.getText(), inputEmail);
        if(emailFormatResult==false){
            showToast(ESignUp.emailFormatErrorMessage.getText());
            return false; }
        return true;
    }

    public boolean validPasswordCheck(){
        String inputPW = binding.edtPasswordCheck.getText().toString();
        String inputCheckPW = binding.edtPassword.getText().toString();
        Boolean inputPWEnglishNumberResult = Pattern.matches(ESignUp.pwEnglishNumberFormat.getText(), inputPW);
        Boolean inputCheckPWEnglishNumberResult = Pattern.matches(ESignUp.pwEnglishNumberFormat.getText(), inputCheckPW);
        if(inputPW.length()==0 || inputCheckPW.length()==0){
            showToast(ESignUp.pwInputMessage.getText());
            return false;
        }
        if(inputPWEnglishNumberResult==false || inputCheckPWEnglishNumberResult==false){
            showToast(ESignUp.pwWarningMessage.getText());
            return false;
        }
        if(!inputPW.equals(inputCheckPW)){
            showToast(ESignUp.pwNotMatchMessage.getText());
            return false;
        }
        return true;
    }

    public boolean validNameCheck() {
        String inputName = binding.edtNickname.getText().toString();
        if(inputName.length()==0){
            showToast(ESignUp.nameInputMessage.getText());
            return false;
        }
        if(inputName.length()<3 || inputName.length()>10 ){
            showToast(ESignUp.nameInput2Message.getText());
            return false;
        }
        return true;
    }

    public boolean phoneNumberCheck() {
        String inputPhoneNumber = binding.edtPhoneNum.getText().toString();
        if(inputPhoneNumber.length()==0){
            showToast(ESignUp.phoneNumberInputMessage.getText());
            return false;
        }return true;
    }


    public boolean agreeCheck(){
        if((!binding.radioButton.isChecked())||(!binding.radioButton2.isChecked())||(!binding.radioButton3.isChecked())
                ||(!binding.radioButton4.isChecked())){
            showToast(ESignUp.agreeCheckMessage.getText());
            return false;
        }
        return true;
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

    class SignUpCallback implements MainRetrofitCallback<SignUpResponse> {

        @Override
        public void onSuccessResponse(Response<SignUpResponse> response) {
            Constants.userId = response.body().getUserId();
            System.out.println("userId"+Constants.userId);
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
        private void showToast(String message) {
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

