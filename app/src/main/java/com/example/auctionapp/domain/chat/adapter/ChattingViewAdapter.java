package com.example.auctionapp.domain.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityChatRoomBinding;
import com.example.auctionapp.domain.chat.constant.ChatConstants;
import com.example.auctionapp.domain.chat.dto.ChatMessageResponse;
import com.example.auctionapp.domain.chat.model.ChatModel;
import com.example.auctionapp.domain.chat.model.User;
import com.example.auctionapp.domain.chat.presenter.ChatMessagePresenter;
import com.example.auctionapp.domain.chat.view.ChatMessageView;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
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
import org.json.JSONObject;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

//===============채팅 창===============//
public class ChattingViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ChatMessageView {
    List<ChatModel.Comment> comments;

    //uid
    private Long chatRoomUid; //채팅방 하나 id
    private Long myuid;       //나의 id
    private Long destUid;     //상대방 uid
    private User destUser;
    String profileUrlStr;
    private Long EndItemId;

    // Attributes
    private ChatMessageView chatMessageView;
    private ActivityChatRoomBinding mBinding;
    private Context context;


    public ChattingViewAdapter() {
        comments = new ArrayList<>();
        myuid = null;
        destUid = null;

        //채팅 내용 읽어들임
        getMessageList();
    }

    public ChattingViewAdapter(ChatMessageView chatMessageView, ActivityChatRoomBinding mBinding, Context getApplicationContext, Long chatRoomUid, Long myuid, Long destUid) {
        comments = new ArrayList<>();
        this.chatMessageView = chatMessageView;
        this.mBinding = mBinding;
        this.context = getApplicationContext;
        this.chatRoomUid = chatRoomUid;
        this.myuid = myuid;
        this.destUid = destUid;

        //채팅 내용 읽어들임
        getMessageList();
    }

    //채팅 내용 읽어들임
    private void getMessageList() {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getChatMessages(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getChatMessagesCallback()));
    }

    public class getChatMessagesCallback implements MainRetrofitCallback<ChatMessageResponse> {
        @Override
        public void onSuccessResponse(Response<ChatMessageResponse> response) {
            String messageStr = response.body().getMessage();
            String LocallatestDate = response.body().getCreatedDate().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
            String month = LocallatestDate.substring(4,6);
            String day = LocallatestDate.substring(6,8);
            String hour = LocallatestDate.substring(9,11);
            String minute = LocallatestDate.substring(12,14);
            String time = LocallatestDate.substring(9,14);
            String timestampStr = month + "월 " + day + "일 " + time;
            Long Uid = response.body().getId();
            if (Uid == Constants.userId) {
                comments.add(new ChatModel.Comment(Uid, messageStr, timestampStr, 1));
            } else {
                comments.add(new ChatModel.Comment(Uid, messageStr, timestampStr, 0));
            }

            Log.d(TAG, ChatConstants.EChatCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ChatMessageResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            chatMessageView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, ChatConstants.EChatCallback.rtFailResponse.getText());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            mBinding.chattingItemDetailPrice.setText(ChatConstants.EChatCallback.rtConnectionFail.getText());
            Log.e(ChatConstants.EChatCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == Code.ViewType.CENTER_CONTENT) {
//                view = inflater.inflate(R.layout.center_content, parent, false);
//                return new CenterViewHolder(view);
        } else if (viewType == Code.ViewType.LEFT_CONTENT) {
            view = inflater.inflate(R.layout.custom_chatting_otherbox, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.custom_chatting_mybox, parent, false);
            return new RightViewHolder(view);
        }
        return new RightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //user detail callback
        class UserDetailsInfoCallback implements MainRetrofitCallback<UserInfoResponse> {
            @Override
            public void onSuccessResponse(Response<UserInfoResponse> response) {
                ((LeftViewHolder) viewHolder).name.setText(response.body().getUsername());
                if(response.body().getPicture() != null)
                    Glide.with(context).load(response.body().getPicture()).into(((LeftViewHolder) viewHolder).image);
                else
                    Glide.with(context).load(R.drawable.profile).into(((LeftViewHolder) viewHolder).image);

                Log.d(TAG, ChatConstants.EChatCallback.rtSuccessResponse.getText() + response.body().toString());
            }
            @Override
            public void onFailResponse(Response<UserInfoResponse> response) throws IOException, JSONException {
                Log.d(TAG, response.errorBody().string());
                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(context, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                } catch (Exception e) { Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show(); }
                Log.d(TAG, ChatConstants.EChatCallback.rtFailResponse.getText());
            }
            @Override
            public void onConnectionFail(Throwable t) {
                Log.e(ChatConstants.EChatCallback.rtConnectionFail.getText(), t.getMessage());
            }
        }

        if (viewHolder instanceof CenterViewHolder) {
//                ((CenterViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
        } else if (viewHolder instanceof LeftViewHolder) {
            RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).userDetails(Long.valueOf(comments.get(position).getUid()))
                .enqueue(MainRetrofitTool.getCallback(new UserDetailsInfoCallback()));
            ((LeftViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
            String chatTimeStr = comments.get(position).getTimestamp();
            String [] array = chatTimeStr.split("-");
            String month = array[1];
            String [] array2 = array[2].split("T");
            String [] array3 = array2[1].split(":");
            String chatTime = month + "월 " + array2[0] + "일 " + array3[0] + ":" + array3[1];
            ((LeftViewHolder) viewHolder).chat_time.setText(chatTime);
            ((LeftViewHolder) viewHolder).chat_time.setText(chatTime);

        } else {
//                ((RightViewHolder) viewHolder).name.setText(comments.get(position).getUid());
            ((RightViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
            String chatTimeStr = comments.get(position).getTimestamp();
            String [] array = chatTimeStr.split("-");
            String month = array[1];
            String [] array2 = array[2].split("T");
            String [] array3 = array2[1].split(":");
            String chatTime = month + "월 " + array2[0] + "일 " + array3[0] + ":" + array3[1];
            ((RightViewHolder) viewHolder).chat_time.setText(chatTime);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return comments.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        CenterViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView name;
        TextView chat_time;
        ImageView image;

        LeftViewHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.tv_message);
            name = itemView.findViewById(R.id.tv_name);
            chat_time = itemView.findViewById(R.id.tv_time);
            image = itemView.findViewById(R.id.iv_profile);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView chat_time;

        RightViewHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.tv_message);
            chat_time = itemView.findViewById(R.id.tv_time);
        }
    }

    class Code {
        public class ViewType {
            public static final int LEFT_CONTENT = 0;
            public static final int RIGHT_CONTENT = 1;
            public static final int CENTER_CONTENT = 2;
        }
    }
}
