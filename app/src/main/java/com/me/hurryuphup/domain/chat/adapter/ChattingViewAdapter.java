package com.me.hurryuphup.domain.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.me.hurryuphup.R;
import com.me.hurryuphup.databinding.ActivityChatRoomBinding;
import com.me.hurryuphup.domain.chat.constant.ChatConstants;
import com.me.hurryuphup.domain.chat.dto.ChatMessageResponse;
import com.me.hurryuphup.domain.chat.model.ChatModel;
import com.me.hurryuphup.domain.chat.view.ChatMessageView;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.domain.user.dto.UserInfoResponse;
import com.me.hurryuphup.global.dto.PaginationDto;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;
import com.me.hurryuphup.global.util.ErrorMessageParser;
import com.me.hurryuphup.global.util.GetTime;

import org.json.JSONException;

import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

//===============채팅 창===============//
public class ChattingViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ChatMessageView {
    List<ChatModel.Comment> comments;
    ErrorMessageParser errorMessageParser;

    //uid
    private Long chatRoomUid; //채팅방 하나 id
    private Long myuid;       //나의 id
    private Long destUid;     //상대방 uid
    private Long EndItemId;
    private int page = 0;
    private int size = 10;

    // Attributes
    private ChatMessageView chatMessageView;
    private ActivityChatRoomBinding mBinding;
    private Context context;


    public ChattingViewAdapter() {
        comments = new ArrayList<>();
        myuid = null;
        destUid = null;

//        getChatPage();
        //채팅 내용 읽어들임
        getMessageList(0, size);
    }

    public ChattingViewAdapter(ChatMessageView chatMessageView, ActivityChatRoomBinding mBinding, Context getApplicationContext, Long chatRoomUid, Long myuid, Long destUid) {
        comments = new ArrayList<>();
        this.chatMessageView = chatMessageView;
        this.mBinding = mBinding;
        this.context = getApplicationContext;
        this.chatRoomUid = chatRoomUid;
        this.myuid = myuid;
        this.destUid = destUid;

        //get chat page
//        getChatPage();
        //채팅 내용 읽어들임
        getMessageList(page, size);
        mBinding.progressBar.setVisibility(View.GONE);

        mBinding.nestedScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
//                    if(response.body().isHasNext()) {
                        page++;
                        mBinding.progressBar.setVisibility(View.VISIBLE);
                        getMessageList(page, size);
                    mBinding.progressBar.setVisibility(View.GONE);
//                    } else mBinding.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void addItem(ChatModel.Comment data) {
        // 외부에서 item을 추가시킬 함수입니다.
        comments.add(data);
    }
    private int getChatPage() {
//        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getChatMessages(chatRoomUid, 0, size)
//                .enqueue(MainRetrofitTool.getCallback(new getChatPageCallback()));

        return page;
    }
    //채팅 내용 읽어들임
    private void getMessageList(int page, int size) {
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getChatMessages(chatRoomUid, page, size)
                .enqueue(MainRetrofitTool.getCallback(new getChatMessagesCallback()));
    }
//    public class getChatPageCallback implements MainRetrofitCallback<PaginationDto<List<ChatMessageResponse>>> {
//        @Override
//        public void onSuccessResponse(Response<PaginationDto<List<ChatMessageResponse>>> response) {
//            if(response.body().getData().isEmpty()) page = 0;
//            else {
//                page = response.body().getTotalPage();
//                System.out.println("PAGE::" + page);
//            }
//            Log.d("chat messages", ChatConstants.EChatCallback.rtSuccessResponse.getText() + response.body().toString());
//        }
//        @Override
//        public void onFailResponse(Response<PaginationDto<List<ChatMessageResponse>>> response) throws IOException, JSONException {
//            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
//            chatMessageView.showToast(errorMessageParser.getParsedErrorMessage());
//            Log.d("chat messages", ChatConstants.EChatCallback.rtFailResponse.getText());
//        }
//        @Override
//        public void onConnectionFail(Throwable t) {
//            mBinding.chattingItemDetailPrice.setText(ChatConstants.EChatCallback.rtConnectionFail.getText());
//            Log.e(ChatConstants.EChatCallback.rtConnectionFail.getText(), t.getMessage());
//        }
//    }

    public class getChatMessagesCallback implements MainRetrofitCallback<PaginationDto<List<ChatMessageResponse>>> {
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<ChatMessageResponse>>> response) throws IOException, JSONException {
            if(response.body().getData().isEmpty()) System.out.println("null messages");
            else {
                System.out.println("page:::: " + response.body().getTotalPage());
                System.out.println("current page: " + response.body().getCurrentPage());
                System.out.println("current elements: " + response.body().getCurrentElements());
                for (int i = 0; i < response.body().getData().size(); i++) {
                    String messageStr = response.body().getData().get(i).getMessage();
                    String userName = response.body().getData().get(i).getUserName();

                    int year = response.body().getData().get(i).getCreatedDate().getYear();
                    Month month = response.body().getData().get(i).getCreatedDate().getMonth();
                    String day = String.valueOf(response.body().getData().get(i).getCreatedDate().getDayOfMonth());
                    String hour = String.valueOf(response.body().getData().get(i).getCreatedDate().getHour());
                    String minute = String.valueOf(response.body().getData().get(i).getCreatedDate().getMinute());
                    GetTime getTime = new GetTime(year, month, day, hour, minute);
                    String time = getTime.getLatestTime();

                    Long Uid = response.body().getData().get(i).getUserId();
                    //left : 0          //right : 1     //center:2
                    if(messageStr.contains("님이 채팅방에 참여하였습니다") || messageStr.contains("님이 채팅방을 퇴장했습니다"))
                        comments.add(new ChatModel.Comment(Uid, messageStr, time, 2));
                    else if (Uid.equals(Constants.userId)) {
                        comments.add(new ChatModel.Comment(Uid, messageStr, time, 1));
                    } else {
                        comments.add(new ChatModel.Comment(Uid, messageStr, time, 0));
                    }
                    notifyDataSetChanged();
                }

            }
            Log.d("chat messages", ChatConstants.EChatCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<ChatMessageResponse>>> response) throws IOException, JSONException {
            errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            Log.d("chat messages", ChatConstants.EChatCallback.rtFailResponse.getText());
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
                view = inflater.inflate(R.layout.custom_chatting_enter, parent, false);
                return new CenterViewHolder(view);
        } else if (viewType == Code.ViewType.LEFT_CONTENT) {
            view = inflater.inflate(R.layout.custom_chatting_otherbox, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.custom_chatting_mybox, parent, false);
            return new RightViewHolder(view);
        }
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
                errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
                Log.d(TAG, ChatConstants.EChatCallback.rtFailResponse.getText());
            }
            @Override
            public void onConnectionFail(Throwable t) {
                Log.e(ChatConstants.EChatCallback.rtConnectionFail.getText(), t.getMessage());
            }
        }

        if (viewHolder instanceof CenterViewHolder) {
                ((CenterViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
        } else if (viewHolder instanceof LeftViewHolder) {
            RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).userDetails(comments.get(position).getUid())
                .enqueue(MainRetrofitTool.getCallback(new UserDetailsInfoCallback()));
            ((LeftViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
            ((LeftViewHolder) viewHolder).chat_time.setText(comments.get(position).getTimestamp());

        } else {
//                ((RightViewHolder) viewHolder).name.setText(comments.get(position).getUid());
            ((RightViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
            ((RightViewHolder) viewHolder).chat_time.setText(comments.get(position).getTimestamp());
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
            content = itemView.findViewById(R.id.tv_message);
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
