package com.example.auctionapp.global.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token){
        Log.d("FCM Log", "Refreshed token: "+token);
    }
}
