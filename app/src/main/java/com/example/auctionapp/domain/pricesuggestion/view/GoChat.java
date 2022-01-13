package com.example.auctionapp.domain.pricesuggestion.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.R;
import com.example.auctionapp.domain.chat.view.ChatRoom;

public class GoChat extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_fees);

        Intent intent = getIntent();
        Long EndItemId = intent.getLongExtra("itemId", 0);
        Long participantId = intent.getLongExtra("participantId", 0);

        TextView tv_goChat = (TextView) findViewById(R.id.tv_goChat);
        tv_goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tt = new Intent(getApplicationContext(), ChatRoom.class);
                tt.putExtra("itemId", EndItemId);
                tt.putExtra("participantId", participantId);
                tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tt);
            }
        });
    }
}
