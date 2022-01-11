package com.example.auctionapp.domain.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.chat.model.chatListData;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class chatListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<chatListData> data;
    private ImageView itemImageImageView;
    private TextView profileNameTextView;
    private TextView chatTimeTextView;
    private TextView lastChatTextView;


    public chatListAdapter() {}
    public chatListAdapter(Context context, ArrayList<chatListData> dataArray) {
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
    public chatListData getItem(int position) {
        return data.get(position);
    }


    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.custom_chatlist_listview, null);

        itemImageImageView = (ImageView) view.findViewById(R.id.iv_chatlist_itemImage);
        itemImageImageView.setClipToOutline(true);
//        Long itemIdL = data.get(position).getItemId();
        // 상품 이미지 load  //임시
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(Long.valueOf(8))
                .enqueue(MainRetrofitTool.getCallback(new chatListAdapter.getItemDetailsCallback()));
        profileNameTextView = (TextView) view.findViewById(R.id.tv_chatlist_profileName);
        profileNameTextView.setText(data.get(position).getProfileName());
        chatTimeTextView = (TextView) view.findViewById(R.id.tv_chatlist_lastChatTime);
        chatTimeTextView.setText(data.get(position).getChatTime());
        lastChatTextView = (TextView) view.findViewById(R.id.tv_chatlist_lastChat);
        lastChatTextView.setText(data.get(position).getLastChat());

        return view;
    }
    class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            if(response.body().getFileNames().size()!=0){
                String fileThumbNail = response.body().getFileNames().get(0);
                Glide.with(itemImageImageView.getContext()).load(Constants.imageBaseUrl+fileThumbNail).into(itemImageImageView);
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            itemImageImageView.setImageResource(R.drawable.baby);
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            itemImageImageView.setImageResource(R.drawable.baby);
            Log.e("연결실패", t.getMessage());
        }
    }
}