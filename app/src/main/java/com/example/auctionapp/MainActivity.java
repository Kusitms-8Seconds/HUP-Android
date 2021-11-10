package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;

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
        if (fragment == null) {
            if (id == R.id.home) {
                fragment = new Home();
                fragmentTransaction.add(R.id.content_layout, fragment, tag);
            } else if (id == R.id.chat){
                fragment = new Chat();
                fragmentTransaction.add(R.id.content_layout, fragment, tag);
            }else if (id == R.id.upload){
                Intent intent = new Intent(MainActivity.this, UploadPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else if (id == R.id.mypage){
                fragment = new Mypage();
                fragmentTransaction.add(R.id.content_layout, fragment, tag);
            }else if (id == R.id.itemlist){
                fragment = new ItemList();
                fragmentTransaction.add(R.id.content_layout, fragment, tag);
            }


        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();


    }

}