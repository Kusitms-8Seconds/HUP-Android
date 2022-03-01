package com.example.auctionapp.domain.chat.presenter;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.auctionapp.databinding.ActivityChatBinding;
import com.example.auctionapp.domain.chat.adapter.chatListAdapter;
import com.example.auctionapp.domain.chat.constant.ChatConstants;
import com.example.auctionapp.domain.chat.dto.ChatRoomResponse;
import com.example.auctionapp.domain.chat.model.ChatModel;
import com.example.auctionapp.domain.chat.model.chatListData;
import com.example.auctionapp.domain.chat.view.ChatRoomView;
import com.example.auctionapp.domain.home.adapter.BestItemAdapter;
import com.example.auctionapp.domain.home.constant.HomeConstants;
import com.example.auctionapp.domain.home.presenter.MainPresenter;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ChatRoomPresenter implements ChatRoomPresenterInterface {
    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    //uid
    String myuid;
    String chatRoomUid;
    Long itemId;
    //chatting room list
    ArrayList<chatListData> chatroomList = new ArrayList<chatListData>();
    com.example.auctionapp.domain.chat.adapter.chatListAdapter chatListAdapter;

    // Attributes
    private ChatRoomView chatRoomView;
    private ActivityChatBinding mBinding;
    private Context context;

    // Constructor
    public ChatRoomPresenter(ChatRoomView chatRoomView, ActivityChatBinding mBinding, Context getApplicationContext){
        this.chatRoomView = chatRoomView;
        this.mBinding = mBinding;
        this.context = getApplicationContext;
    }

    @Override
    public void init() {
        chatListAdapter = new chatListAdapter(context, chatroomList);
        mBinding.chattingRoomListView.setAdapter(chatListAdapter);

        myuid = String.valueOf(Constants.userId);
        getChatList();
    }

    @Override
    public void getChatList() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getChatRooms(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getChatRoomsCallback()));
    }

    private class getChatRoomsCallback implements MainRetrofitCallback<List<ChatRoomResponse>> {

        @Override
        public void onSuccessResponse(Response<List<ChatRoomResponse>>response) throws IOException {
            for(int i=0; i<response.body().size(); i++) {
                Long chatroomId = response.body().get(i).getId();
                Long destId = response.body().get(i).getUserId();
                String userName = response.body().get(i).getUserName();
                Long itemId = response.body().get(i).getItemId();
                String itemUrl = "";
                if (response.body().get(i).getFileNames() != null)
                    itemUrl = response.body().get(i).getFileNames().get(0);
                String latestMessage = response.body().get(i).getLatestMessage();

                System.out.println(userName + " !!: " + chatroomId);
//                String LocallatestDate = response.body().get(i).getLatestTime().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
//                String month = LocallatestDate.substring(4,6);
//                String day = LocallatestDate.substring(6,8);
//                String hour = LocallatestDate.substring(9,11);
//                String minute = LocallatestDate.substring(12,14);
//                String time = LocallatestDate.substring(9,14);
//                String latestTime = LocallatestDate;

                chatroomList.add(new chatListData(chatroomId, destId, userName, itemId, itemUrl, latestMessage, null));
                chatListAdapter.notifyDataSetChanged();
            }

            Log.d("getChatRooms", "onSuccess");
        }
        @Override
        public void onFailResponse(Response<List<ChatRoomResponse>> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            chatRoomView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d("getChatRooms", HomeConstants.EHomeCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(HomeConstants.EHomeCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
