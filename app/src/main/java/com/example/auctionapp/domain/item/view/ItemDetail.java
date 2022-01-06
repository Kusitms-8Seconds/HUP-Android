package com.example.auctionapp.domain.item.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityItemDetailBinding;
import com.example.auctionapp.databinding.ActivityNoticeDetailBinding;
import com.example.auctionapp.domain.home.view.Home;
import com.example.auctionapp.domain.item.dto.DefaultResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.pricesuggestion.view.BidPage;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemDetail extends AppCompatActivity {
    private ActivityItemDetailBinding binding;

    private ArrayList<String> itemImageList;
    private static final int DP = 24;
    // id
    Long myId = Constants.userId;
    private Long itemId;
    private Long scrapId;

    public Boolean isHeart = false;

    ItemDetailViewPagerAdapter itemDetailViewPagerAdapter;

    ArrayList<qnaData> qnaList = new ArrayList<qnaData>();
    qnaAdapter adapter;

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
//        String getItemId = intent.getExtras().getString("itemId");
        this.itemId = intent.getLongExtra("itemId",0);

        if(Constants.userId == null) {
            binding.participateButton.setText("로그인 후 이용해주세요.");
            binding.participateButton.setEnabled(false);
            binding.participateButton.setBackgroundColor(Color.GRAY);
        }
        binding.participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BidPage.class);
                intent.putExtra("itemId", String.valueOf(itemId));
                startActivity(intent);
            }
        });

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // viewpager
        this.initializeImageData();

        itemDetailViewPagerAdapter = new ItemDetailViewPagerAdapter(this, itemImageList);
        binding.itemDetailViewPager.setAdapter(itemDetailViewPagerAdapter);

        //나중에 수정필요
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                .isCheckedHeart(ScrapCheckedRequest.of(Constants.userId, itemId))
                .enqueue(MainRetrofitTool.getCallback(new isCheckedHeartCallback()));
        //로긘 않 해쓷ㄹ대
        if(Constants.userId==null) {
            binding.isheart.setVisibility(View.GONE);
        }
        binding.isheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHeart) {
                        RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                                .deleteHeart(scrapId)
                                .enqueue(MainRetrofitTool.getCallback(new deleteScrapCallback()));
                }else {
                    RetrofitTool.getAPIWithAuthorizationToken(Constants.token)
                            .createScrap(ScrapRegisterRequest.of(Constants.userId, itemId))
                            .enqueue(MainRetrofitTool.getCallback(new createScrapCallback()));
                }
            }
        });

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getMaximumPrice(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getMaximumPriceCallback()));

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getParticipants(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getParticipantsCallback()));

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).userDetails(UserDetailsInfoRequest.of(Constants.userId))
                .enqueue(MainRetrofitTool.getCallback(new getUserDetailsCallback()));

        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(itemId)
                .enqueue(MainRetrofitTool.getCallback(new getItemDetailsCallback()));

        //item delete
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitTool.getAPIWithAuthorizationToken(Constants.token).deleteItem(itemId)
                        .enqueue(MainRetrofitTool.getCallback(new ItemDetail.DeleteItemCallback()));
            }
        });

        //QNA dumidata
        adapter = new qnaAdapter(this.getApplicationContext(), qnaList);
        binding.itemDetailQnaList.setAdapter(adapter);
        initializeQnAData();

        binding.itemDetailQnaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                qnaData data = (qnaData) parent.getItemAtPosition(position);
                Boolean isFolded = data.getFolded();
                if(isFolded) {
                    data.setFolded(false);
                    adapter.notifyDataSetChanged();
                }else {
                    data.setFolded(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //view all qnas
        binding.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(getApplicationContext(), QnA.class);
                t.putExtra("itemId", itemId);
                startActivity(t);
            }
        });

    }
    public void initializeImageData()
    {
        itemImageList = new ArrayList();

//        itemImageList.add(R.drawable.testitemimage);
//        itemImageList.add(R.drawable.testitemimage);
//        itemImageList.add(R.drawable.testitemimage);
//        itemImageList.add(R.drawable.testitemimage);
//        itemImageList.add(R.drawable.testitemimage);
    }
    public void initializeQnAData() {
        qnaList.add(new qnaData("자전거 많이 무겁나요?", "2021.11.27", "hoa9***", false, true));
        qnaList.add(new qnaData("기능 어떤 것들이 있나요?", "2021.11.26", "둠***", true, true));
        qnaList.add(new qnaData("자전거 브랜드 궁금합니다.", "2021.11.26", "우왕***", true, true));

        binding.qaCount.setText("(" + String.valueOf(qnaList.size()-1) + ")");
        adapter.notifyDataSetChanged();
    }

    private class DeleteItemCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
//            heart.setImageResource(R.drawable.heartx);
//            isHeart = false;
//            scrapId = null;
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<DefaultResponse> response) {
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class deleteScrapCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            binding.isheart.setImageResource(R.drawable.heartx);
            isHeart = false;
            scrapId = null;
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<DefaultResponse> response) {
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class createScrapCallback implements MainRetrofitCallback<ScrapRegisterResponse> {
        @Override
        public void onSuccessResponse(Response<ScrapRegisterResponse> response) {
            binding.isheart.setImageResource(R.drawable.hearto);
            isHeart = true;
            scrapId = response.body().getScrapId();
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<ScrapRegisterResponse> response) {
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class isCheckedHeartCallback implements MainRetrofitCallback<ScrapCheckedResponse> {
        @Override
        public void onSuccessResponse(Response<ScrapCheckedResponse> response) {

            if(response.body().getScrapId()!=null){
                binding.isheart.setImageResource(R.drawable.hearto);
                isHeart = true;
                scrapId = response.body().getScrapId();
            } else{
                binding.isheart.setImageResource(R.drawable.heartx);
                isHeart = false;
                scrapId = null;
            }
            Log.d(TAG, "retrofit success: ");
        }

        @Override
        public void onFailResponse(Response<ScrapCheckedResponse> response) {
            Log.d(TAG, "onFailResponse");
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getMaximumPriceCallback implements MainRetrofitCallback<MaximumPriceResponse> {

        @Override
        public void onSuccessResponse(Response<MaximumPriceResponse> response) throws IOException {

            binding.highPrice.setText(String.valueOf(response.body().getMaximumPrice()));
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<MaximumPriceResponse> response) throws IOException, JSONException {
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getParticipantsCallback implements MainRetrofitCallback<ParticipantsResponse> {

        @Override
        public void onSuccessResponse(Response<ParticipantsResponse> response) {
            binding.participants.setText(String.valueOf(response.body().getParticipantsCount()));
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ParticipantsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    private class getUserDetailsCallback implements MainRetrofitCallback<UserDetailsInfoResponse> {

        @Override
        public void onSuccessResponse(Response<UserDetailsInfoResponse> response) {
            binding.sellerName.setText(response.body().getUsername());
            if(!response.body().getPicture().isEmpty()){
                Glide.with(getApplicationContext()).load(response.body().getPicture()).into(binding.sellerImage);
            }
            //delete item
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<UserDetailsInfoResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

    public class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            binding.itemName.setText(response.body().getItemName());
            binding.itemContent.setText(response.body().getDescription());
            if(response.body().getFileNames().size()!=0){
                for (int i=0; i<response.body().getFileNames().size(); i++) {
                    itemImageList.add(response.body().getFileNames().get(i));
                }
                itemDetailViewPagerAdapter = new ItemDetailViewPagerAdapter(getApplicationContext(), itemImageList);
                binding.itemDetailViewPager.setAdapter(itemDetailViewPagerAdapter);
            }
            binding.category.setText(response.body().getCategory().getName());
            LocalDateTime startDateTime = LocalDateTime.now();
            LocalDateTime endDateTime = response.body().getAuctionClosingDate();
            String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
            String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
            String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)/60);
            binding.itemLeftTime.setText(days+"일 "+hours+"시간 "+minutes+"분 전");

            if(response.body().getUserId().equals(myId)) {

                binding.deleteButton.setVisibility(View.VISIBLE);
                //button inactivated
                binding.participateButton.setEnabled(false);
                binding.participateButton.setBackgroundColor(Color.GRAY);
            }else {
                binding.deleteButton.setVisibility(View.GONE);
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}
class qnaData{
    private String title;
    private String date;
    private String id;
    private boolean state;  //true - 답변완료, false - 답변예정
    private boolean isFolded;

    public qnaData(){

    }

    public qnaData(String title, String date, String id, boolean state, boolean isFolded){
        this.title = title;
        this.date = date;
        this.id = id;
        this.state = state;
        this.isFolded = isFolded;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDate(){
        return this.date;
    }
    public String getId(){
        return this.id;
    }
    public Boolean getState(){
        return this.state;
    }
    public Boolean getFolded(){
        return this.isFolded;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setState(Boolean state) {
        this.state = state;
    }
    public void setFolded(Boolean isFolded) {
        this.isFolded = isFolded;
    }

}



class qnaAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<qnaData> data;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView idTextView;
    private TextView stateTextView;
    private  ImageView icon;
    private ConstraintLayout ly_answer;


    public qnaAdapter() {}
    public qnaAdapter(Context context, ArrayList<qnaData> dataArray) {
        mContext = context;
        data = dataArray;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public qnaData getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.custom_qna_list, null);

        titleTextView = (TextView) view.findViewById(R.id.question_title);
        titleTextView.setText(data.get(position).getTitle());
        dateTextView = (TextView) view.findViewById(R.id.question_date);
        dateTextView.setText(data.get(position).getDate());
        idTextView = (TextView) view.findViewById(R.id.question_id);
        idTextView.setText(data.get(position).getId());
        stateTextView = (TextView) view.findViewById(R.id.qna_state);
        if(data.get(position).getState()) {
            stateTextView.setText("답변완료");
            stateTextView.setTextColor(Color.BLUE);
        }else {
            stateTextView.setText("답변예정");
        }
        icon = (ImageView) view.findViewById(R.id.foldIcon);
        ly_answer = (ConstraintLayout) view.findViewById(R.id.ly_answer);
        if(!data.get(position).getFolded()) {
            ly_answer.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.down);
        }else {
            ly_answer.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.fold_up);
        }

        return view;
    }

}
