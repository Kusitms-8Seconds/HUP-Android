package com.example.auctionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class SignUp extends AppCompatActivity {

    static final int SNS_SEND_PERMISSION = 1;

    private EditText edt_phoneNum;       //휴대폰번호 입력창
    private EditText edt_smsCheck;       //인증번호 입력창
    private Button btn_sendSms;          //휴대폰번호 인증번호 발송 버튼
    private Button btn_phoneCheck;       //휴대폰번호 인증번호 확인 버튼

    //인증번호를 비교하기 위해 쉐어드에 저장
    SharedPreferences prof = getPreferences(MODE_PRIVATE);
    SharedPreferences.Editor editor = prof.edit();

    String checkNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_phoneNum = findViewById(R.id.edt_phoneNum);
        edt_smsCheck = findViewById(R.id.edt_smsCheck);
        btn_sendSms = findViewById(R.id.btn_sendSms);
        btn_phoneCheck = findViewById(R.id.btn_phoneCheck);

        //문자 보내기 권한 확인
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            //문자 보내기 권한 거부
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                Toast.makeText(getApplicationContext(), "SMS 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            }
            //문자 보내기 권한 허용
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SNS_SEND_PERMISSION);
        }

        //인증번호 발송 클릭 이벤트
        btn_sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭시 발생하는 이벤트
                checkNum = numberGen(4,1);
                editor.putString("checkNum", checkNum);
                sendSMS(edt_phoneNum.getText().toString(), "인증번호 : " + checkNum);
            }
        });

        //인증번호 체크하는 버튼
        btn_phoneCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prof.getString("checkNum","").equals(edt_smsCheck.getText().toString())){
                    Toast.makeText(getApplicationContext(), "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //sms 발송 기능
    private void sendSMS(String phoneNumber, String message){
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, SignUp.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber,null, message, pi, null);

        Toast.makeText(getBaseContext(), "메시지가 전송되었습니다.", Toast.LENGTH_SHORT).show();
    }

    //인증번호 생성 기능
    // len: 생성할 난수의 길이
    // dup: 중복 허용 여부(1: 중복 허용, 2: 중복 제거)
    public static String numberGen(int len, int dup){
        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for (int i = 0; i<len; i++){
            String ran = Integer.toString(rand.nextInt(10));
            if(dup==1){
                //중복 허용시 numStr에 append
                numStr += ran;
            }
            else if (dup==2){
                //중복 허용하지 않을시 중복된 값이 있는지 검사
                if(!numStr.contains(ran)){
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                }
                else{
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i -= 1;
                }
            }
        }
        return numStr;
    }
}