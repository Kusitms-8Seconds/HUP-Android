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
import com.example.auctionapp.domain.pricesuggestion.view.FeesPage;
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

//        dialog02 = new Dialog(MainActivity.this);
//        dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog02.setContentView(R.layout.custom_dialog02);
//        showDialog();

//        dialog03 = new Dialog(MainActivity.this);
//        dialog03.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog03.setContentView(R.layout.custom_dialog03);
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

}
