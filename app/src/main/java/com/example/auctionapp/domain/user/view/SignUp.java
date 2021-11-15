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
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.constant.Constants.ESignUp;
import com.example.auctionapp.domain.user.dto.SignUpRequest;
import com.example.auctionapp.domain.user.dto.SignUpResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONObject;

import java.util.regex.Pattern;

import lombok.Getter;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SignUp extends AppCompatActivity {

//    static final int SNS_SEND_PERMISSION = 1;
//
//    private EditText edt_phoneNum;       //휴대폰번호 입력창
//    private EditText edt_smsCheck;       //인증번호 입력창
//    private Button btn_sendSms;          //휴대폰번호 인증번호 발송 버튼
//    private Button btn_phoneCheck;       //휴대폰번호 인증번호 확인 버튼
//
//    //인증번호를 비교하기 위해 쉐어드에 저장
//    SharedPreferences prof = getPreferences(MODE_PRIVATE);
//    SharedPreferences.Editor editor = prof.edit();
//
//    String checkNum;

    private EditText edt_loginId;
    private EditText edt_passwordCheck;
    private EditText edt_password;
    private EditText edt_nickname;
    private EditText edt_phoneNum;
    private EditText edt_email;
    private CheckBox radioButtonTotal;
    private CheckBox radioButton1;
    private CheckBox radioButton2;
    private CheckBox radioButton3;
    private CheckBox radioButton4;
    private Button btn_signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_loginId = findViewById(R.id.edt_userId);
        edt_passwordCheck = findViewById(R.id.edt_passwordCheck);
        edt_password = findViewById(R.id.edt_password);
        edt_nickname = findViewById(R.id.edt_nickname);
        edt_phoneNum = findViewById(R.id.edt_phoneNum);
        edt_email = findViewById(R.id.edt_email);
        radioButtonTotal = findViewById(R.id.radioButton5);
        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        btn_signUp = findViewById(R.id.btn_signUp);

        radioButtonTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!radioButtonTotal.isChecked()){
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(false);
                    radioButton4.setChecked(false);
                }else {
                    radioButton1.setChecked(true);
                    radioButton2.setChecked(true);
                    radioButton3.setChecked(true);
                    radioButton4.setChecked(true);
                }
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = signUpCheck();
                if(intent !=null){
                        showToast("회원가입을 성공했습니다.");
                        startActivity(intent); } }
            });





//        edt_phoneNum = findViewById(R.id.edt_phoneNum);
//        edt_smsCheck = findViewById(R.id.edt_smsCheck);
//        btn_sendSms = findViewById(R.id.btn_sendSms);
//        btn_phoneCheck = findViewById(R.id.btn_phoneCheck);

//        //문자 보내기 권한 확인
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
//        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
//            //문자 보내기 권한 거부
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
//                Toast.makeText(getApplicationContext(), "SMS 권한이 필요합니다", Toast.LENGTH_SHORT).show();
//            }
//            //문자 보내기 권한 허용
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SNS_SEND_PERMISSION);
//        }
//
//        //인증번호 발송 클릭 이벤트
//        btn_sendSms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //클릭시 발생하는 이벤트
//                checkNum = numberGen(4,1);
//                editor.putString("checkNum", checkNum);
//                sendSMS(edt_phoneNum.getText().toString(), "인증번호 : " + checkNum);
//            }
//        });
//
//        //인증번호 체크하는 버튼
//        btn_phoneCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(prof.getString("checkNum","").equals(edt_smsCheck.getText().toString())){
//                    Toast.makeText(getApplicationContext(), "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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
                    .loginId(edt_loginId.getText().toString())
                    .email(edt_email.getText().toString())
                    .username(edt_nickname.getText().toString())
                    .password(edt_password.getText().toString())
                    .phoneNumber(edt_phoneNum.getText().toString())
                    .build();
            RetrofitTool.getAPIWithNullConverter().signup(signUpRequest)
                    .enqueue(MainRetrofitTool.getCallback(new SignUpCallback()));

            Intent intent = new Intent(SignUp.this, MainActivity.class);
            return intent;
        }
    }

    private boolean validLoginIdCheck() {
        String inputId = edt_loginId.getText().toString();
        if(inputId.length() < 5 || inputId.length() > 11){
            showToast("아이디는 최소 5글자 이상 10글자 이하여야 합니다.");
            return false; }
        return true;
        }

    public boolean validEmailCheck(){
        String inputEmail = edt_email.getText().toString();
        Boolean emailFormatResult = Pattern.matches(ESignUp.emailFormat.getText(), inputEmail);
        if(emailFormatResult==false){
            showToast(ESignUp.emailFormatErrorMessage.getText());
            return false; }
        return true;
    }

    public boolean validPasswordCheck(){
        String inputPW = edt_passwordCheck.getText().toString();
        String inputCheckPW = edt_password.getText().toString();
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
        String inputName = edt_nickname.getText().toString();
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
        String inputPhoneNumber = edt_phoneNum.getText().toString();
        if(inputPhoneNumber.length()==0){
            showToast(ESignUp.phoneNumberInputMessage.getText());
            return false;
        }return true;
    }


    public boolean agreeCheck(){
        if((!radioButton1.isChecked())||(!radioButton2.isChecked())||(!radioButton3.isChecked())
                ||(!radioButton4.isChecked())){
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

//    //sms 발송 기능
//    private void sendSMS(String phoneNumber, String message){
//        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, SignUp.class), 0);
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber,null, message, pi, null);
//
//        Toast.makeText(getBaseContext(), "메시지가 전송되었습니다.", Toast.LENGTH_SHORT).show();
//    }
//
//    //인증번호 생성 기능
//    // len: 생성할 난수의 길이
//    // dup: 중복 허용 여부(1: 중복 허용, 2: 중복 제거)
//    public static String numberGen(int len, int dup){
//        Random rand = new Random();
//        String numStr = ""; //난수가 저장될 변수
//
//        for (int i = 0; i<len; i++){
//            String ran = Integer.toString(rand.nextInt(10));
//            if(dup==1){
//                //중복 허용시 numStr에 append
//                numStr += ran;
//            }
//            else if (dup==2){
//                //중복 허용하지 않을시 중복된 값이 있는지 검사
//                if(!numStr.contains(ran)){
//                    //중복된 값이 없으면 numStr에 append
//                    numStr += ran;
//                }
//                else{
//                    //생성된 난수가 중복되면 루틴을 다시 실행한다
//                    i -= 1;
//                }
//            }
//        }
//        return numStr;
//    }
