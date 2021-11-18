package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.auctionapp.domain.chat.view.Chat;
import com.example.auctionapp.domain.chat.view.ChatRoom;
import com.example.auctionapp.domain.home.view.Home;
import com.example.auctionapp.domain.home.view.Mypage;
import com.example.auctionapp.domain.home.view.UploadPage;
import com.example.auctionapp.domain.item.view.ItemList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;
    Dialog dialog02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                System.out.println("선택될때");
                BottomNavigate(menuItem.getItemId());

                return true;
            }
        });
        
        
        mBottomNV.setSelectedItemId(R.id.home);

        dialog02 = new Dialog(MainActivity.this);
        dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog02.setContentView(R.layout.custom_dialog02);
        showDialog();
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
                Intent intent = new Intent(MainActivity.this, ChatRoom.class);
                startActivity(intent);
            }
        });
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