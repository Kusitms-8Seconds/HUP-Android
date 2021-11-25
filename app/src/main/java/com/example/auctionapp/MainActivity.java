package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.domain.chat.view.Chat;
import com.example.auctionapp.domain.chat.view.ChatRoom;
import com.example.auctionapp.domain.home.view.Home;
import com.example.auctionapp.domain.home.view.Mypage;
import com.example.auctionapp.domain.home.view.UploadPage;
import com.example.auctionapp.domain.item.view.ItemList;
import com.example.auctionapp.domain.pricesuggestion.view.BidPage;
import com.example.auctionapp.domain.pricesuggestion.view.FeesPage;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;
    Dialog dialog02;    //경매 종료 알림 다이얼로그
    Dialog dialog03;    //경매 진행 버튼 클릭 후 수수료 타이머 다이얼로그
    TextView tv_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());

                return true;
            }
        });
        
        
        mBottomNV.setSelectedItemId(R.id.home);

        dialog02 = new Dialog(MainActivity.this);
        dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog02.setContentView(R.layout.custom_dialog02);

        if(!(Constants.userId == null))
            showDialog();

        dialog03 = new Dialog(MainActivity.this);
        dialog03.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog03.setContentView(R.layout.custom_dialog03);
    }
    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        //if (fragment == null) {
            if (id == R.id.home) {
                fragment = new Home();
                fragmentTransaction.add(R.id.content_layout, fragment, tag);
                fragmentTransaction.show(fragment);
            } else if (id == R.id.chat){
                fragment = new Chat();
                fragmentTransaction.add(R.id.content_layout, fragment, tag);
                fragmentTransaction.show(fragment);
            }else if (id == R.id.upload){
                Intent intent = new Intent(MainActivity.this, UploadPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else if (id == R.id.mypage){
                fragment = new Mypage();
                fragmentTransaction.add(R.id.content_layout, fragment, tag);
                fragmentTransaction.show(fragment);
            }else if (id == R.id.itemlist){
                fragment = new ItemList();
                fragmentTransaction.add(R.id.content_layout, fragment, tag);
                fragmentTransaction.show(fragment);
            }


       // } else {
//        fragmentTransaction.show(fragment);
        //}

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();

    }
    // dialog02을 디자인하는 함수
    public void showDialog(){
        dialog02.show(); // 다이얼로그 띄우기

        // 홈으로 돌아가기 버튼
        ImageView goHome = dialog02.findViewById(R.id.refuseAuction);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog02.dismiss();

            }
        });
        // 참여내역 확인 버튼
        dialog02.findViewById(R.id.ongoAuction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ChatRoom.class);
//                startActivity(intent);
                showFeesDialog();
            }
        });
    }
    // dialog03을 디자인하는 함수
    public void showFeesDialog(){
        dialog03.show(); // 다이얼로그 띄우기

        ImageView timer = dialog03.findViewById(R.id.iv_timer);
        Glide.with(this).load(R.raw.timer).into(timer);

        tv_timer = (TextView) dialog03.findViewById(R.id.tv_timer);
        class MyTimer extends CountDownTimer
        {
            public MyTimer(long millisInFuture, long countDownInterval)
            {
                super(millisInFuture, countDownInterval);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                tv_timer.setText(millisUntilFinished/1000 + 1 + "");
            }

            @Override
            public void onFinish() {
                Intent tt = new Intent(MainActivity.this, FeesPage.class);
                tt.putExtra("itemId", 5);
                tt.putExtra("participantId", 1);
                tt.putExtra("finalPrice", 500000);
                tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tt);
            }
        }
        MyTimer myTimer = new MyTimer(3000, 1000);
        myTimer.start();

    }

}
