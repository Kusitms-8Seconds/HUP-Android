package com.example.auctionapp.domain.pricesuggestion.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.chat.view.ChatRoom;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.view.ItemDetail;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class FeesPage extends AppCompatActivity {

    Boolean isFolded;
    int finalPrice;
    private ImageView chattingItemImage;
    private TextView chattingItemDetailName;
    private TextView chattingItemDetailCategory;
    private TextView chattingItemDetailPrice;

    //뒤로가기
    @Override
    public void onBackPressed() {
        Intent tt = new Intent(getApplicationContext(), MainActivity.class);    //임시
        tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(tt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_page);

        //뒤로가기
        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // 결제할 상품 수수료 detail
        chattingItemImage = (ImageView) findViewById(R.id.chattingItemImage);
        chattingItemDetailName = (TextView) findViewById(R.id.chattingItemDetailName);
        chattingItemDetailCategory = (TextView) findViewById(R.id.chattingItemDetailCategory);
        chattingItemDetailPrice = (TextView) findViewById(R.id.chattingItemDetailPrice);
        chattingItemImage.setClipToOutline(true);

        Intent intent = getIntent();
        Long EndItemId = intent.getLongExtra("itemId", 0);
        Long participantId = intent.getLongExtra("participantId", 0);
        finalPrice = intent.getIntExtra("finalPrice", 0);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(Long.valueOf(7))
                .enqueue(MainRetrofitTool.getCallback(new FeesPage.getItemDetailsCallback()));
        // 최종 결제 수수료 가격
        int feesPrice = finalPrice * 5 / 100;
        TextView tv_fees = (TextView) findViewById(R.id.tv_fees);
        tv_fees.setText(feesPrice+"");
        TextView goChatting = (TextView) findViewById(R.id.goChatting);
        goChatting.setText(feesPrice+"원 결제하기");
        goChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tt = new Intent(getApplicationContext(), GoChat.class);
                tt.putExtra("itemId", EndItemId);
                tt.putExtra("participantId", participantId);
                tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tt);
            }
        });

        //item detail click
        ImageView goSeeItemDetail = (ImageView) findViewById(R.id.goSeeItemDetail);
        goSeeItemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemDetail.class);
                startActivity(intent);
            }
        });
        // 결제수단 창 접기
        isFolded = false;
        ConstraintLayout buymethod = (ConstraintLayout) findViewById(R.id.buymethod);
        ImageView upIcon = (ImageView) findViewById(R.id.upIcon);
        buymethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout ly_buymethod = (ConstraintLayout) findViewById(R.id.ly_buymethod);
                if(isFolded) {
                    ly_buymethod.setVisibility(View.VISIBLE);
                    upIcon.setImageResource(R.drawable.fold_up);
                    isFolded = false;
                }else {
                    ly_buymethod.setVisibility(View.GONE);
                    upIcon.setImageResource(R.drawable.down);
                    isFolded = true;
                }
            }
        });
        // 결제수단 선택
        TextView method_card = (TextView) findViewById(R.id.method_card);
        TextView method_account = (TextView) findViewById(R.id.method_account);
        TextView method_kakaopay = (TextView) findViewById(R.id.method_kakaopay);
        TextView method_no_account = (TextView) findViewById(R.id.method_no_account);
        TextView tv_tv = (TextView) findViewById(R.id.tv_tv);
        TextView tv_buyMethod = (TextView) findViewById(R.id.tv_buyMethod);
        tv_tv.setPaintFlags(tv_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        method_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_card.setBackgroundResource(R.drawable.selected_blue);
                method_card.setTextColor(Color.WHITE);
                method_account.setBackgroundResource(R.drawable.box_edge);
                method_account.setTextColor(Color.BLACK);
                method_kakaopay.setBackgroundResource(R.drawable.box_edge);
                method_kakaopay.setTextColor(Color.BLACK);
                method_no_account.setBackgroundResource(R.drawable.box_edge);
                method_no_account.setTextColor(Color.BLACK);
                tv_tv.setText(method_card.getText().toString() + " 안내");
                tv_buyMethod.setText(method_card.getText().toString());
            }
        });
        method_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_account.setBackgroundResource(R.drawable.selected_blue);
                method_account.setTextColor(Color.WHITE);
                method_card.setBackgroundResource(R.drawable.box_edge);
                method_card.setTextColor(Color.BLACK);
                method_kakaopay.setBackgroundResource(R.drawable.box_edge);
                method_kakaopay.setTextColor(Color.BLACK);
                method_no_account.setBackgroundResource(R.drawable.box_edge);
                method_no_account.setTextColor(Color.BLACK);
                tv_tv.setText(method_account.getText().toString() + " 안내");
                tv_buyMethod.setText(method_account.getText().toString());
            }
        });
        method_kakaopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_kakaopay.setBackgroundResource(R.drawable.selected_blue);
                method_kakaopay.setTextColor(Color.WHITE);
                method_account.setBackgroundResource(R.drawable.box_edge);
                method_account.setTextColor(Color.BLACK);
                method_card.setBackgroundResource(R.drawable.box_edge);
                method_card.setTextColor(Color.BLACK);
                method_no_account.setBackgroundResource(R.drawable.box_edge);
                method_no_account.setTextColor(Color.BLACK);
                tv_tv.setText(method_kakaopay.getText().toString() + " 안내");
                tv_buyMethod.setText(method_kakaopay.getText().toString());
            }
        });
        method_no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_no_account.setBackgroundResource(R.drawable.selected_blue);
                method_no_account.setTextColor(Color.WHITE);
                method_account.setBackgroundResource(R.drawable.box_edge);
                method_account.setTextColor(Color.BLACK);
                method_kakaopay.setBackgroundResource(R.drawable.box_edge);
                method_kakaopay.setTextColor(Color.BLACK);
                method_card.setBackgroundResource(R.drawable.box_edge);
                method_card.setTextColor(Color.BLACK);
                tv_tv.setText(method_no_account.getText().toString() + " 안내");
                tv_buyMethod.setText(method_no_account.getText().toString());
            }
        });

    }
    public class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            chattingItemDetailName.setText(response.body().getItemName());
            if(response.body().getFileNames().size()!=0){
                String fileThumbNail = response.body().getFileNames().get(0);
                Glide.with(getApplicationContext()).load(Constants.imageBaseUrl+fileThumbNail).into(chattingItemImage);
            }
            chattingItemDetailCategory.setText(response.body().getCategory().getName());
            chattingItemDetailPrice.setText(finalPrice+"");    //낙찰가 출력(임시)
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            chattingItemDetailPrice.setText("?연결실패?");
            Log.e("연결실패", t.getMessage());
        }
    }
}
