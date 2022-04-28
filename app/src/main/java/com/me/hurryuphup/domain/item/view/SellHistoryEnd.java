package com.me.hurryuphup.domain.item.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.chat.view.ChatMessage;
import com.me.hurryuphup.domain.item.constant.ItemConstants;
import com.me.hurryuphup.domain.item.adapter.SellHistoryEndAdapter;
import com.me.hurryuphup.domain.item.dto.GetAllItemsByStatusRequest;
import com.me.hurryuphup.domain.item.dto.ItemDetailsResponse;
import com.me.hurryuphup.domain.item.model.SellHistoryEndData;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.global.dto.PaginationDto;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SellHistoryEnd extends Fragment {
    ViewGroup viewGroup;

    SellHistoryEndAdapter adapter;
    SellHistoryEndData data;
    List<SellHistoryEndData> sellHistoryEndDataList;
    int bidderCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_sell_history_end, container, false);

        init();
        getData();

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init(){
        RecyclerView recyclerView = viewGroup.findViewById(R.id.sellHistoryEndRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SellHistoryEndAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SellHistoryEndAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), ItemDetail.class);
                intent.putExtra("itemId", adapter.getListData().get(position).getItemId());
                startActivity(intent);
            }
            @Override
            public void onChatButtonClick(View v, int position) {
                Intent intent = new Intent(getContext(), ChatMessage.class);
                intent.putExtra("chatRoomId", adapter.getListData().get(position).getChatRoomId());
                intent.putExtra("destUid", adapter.getListData().get(position).getBidderId());
                intent.putExtra("itemId", adapter.getListData().get(position).getItemId());
                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
    private void getData(){
        bidderCount = 0;
        sellHistoryEndDataList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllItemsByUserIdAndStatus(GetAllItemsByStatusRequest.of(Constants.userId, ItemConstants.EItemSoldStatus.eSoldOut))
                .enqueue(MainRetrofitTool.getCallback(new getAllItemsByUserIdAndStatusCallback()));
    }

    private class getAllItemsByUserIdAndStatusCallback implements MainRetrofitCallback<PaginationDto<List<ItemDetailsResponse>>> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                Long itemId = response.body().getData().get(i).getId();
                String itemName = response.body().getData().get(i).getItemName();
                int suggestionPrice = response.body().getData().get(i).getSoldPrice();
                String bidderName = response.body().getData().get(i).getBidderUserName();
                Long chatRoomId = response.body().getData().get(i).getChatRoomId();
                Long bidderId = response.body().getData().get(i).getBidderUserId();

                if (response.body().getData().get(i).getFileNames().size() != 0) {
                    String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                    data = new SellHistoryEndData(itemId, fileNameMajor, itemName, suggestionPrice, bidderName, chatRoomId, bidderId);
                } else {
                    data = new SellHistoryEndData(itemId, null, itemName, suggestionPrice, bidderName,  chatRoomId, bidderId);
                }
                sellHistoryEndDataList.add(data);
                adapter.addItem(data);
                adapter.notifyDataSetChanged();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }

        @Override
        public void onFailResponse(Response<PaginationDto<List<ItemDetailsResponse>>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

}


