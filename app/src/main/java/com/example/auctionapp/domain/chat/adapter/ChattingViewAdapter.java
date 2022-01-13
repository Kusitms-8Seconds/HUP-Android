package com.example.auctionapp.domain.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityChatRoomBinding;
import com.example.auctionapp.domain.chat.model.ChatModel;
import com.example.auctionapp.domain.chat.model.User;
import com.example.auctionapp.domain.chat.view.ChatRoomView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//===============채팅 창===============//
public class ChattingViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ChatRoomView {
    List<ChatModel.Comment> comments;

    //uid
    private String chatRoomUid; //채팅방 하나 id
    private String myuid;       //나의 id
    private String destUid;     //상대방 uid
    private User destUser;
    String profileUrlStr;
    private Long EndItemId;

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    // Attributes
    private ChatRoomView chatRoomView;
    private ActivityChatRoomBinding mBinding;
    private Context context;

    public ChattingViewAdapter() {
        comments = new ArrayList<>();

        database = FirebaseDatabase.getInstance("https://auctionapp-f3805-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = database.getReference();

        myuid = "상대방";
        destUid = "15";
//        Intent intent = getIntent();
//        destUid = intent.getStringExtra("destUid");
//        EndItemId = intent.getLongExtra("itemId", 0);

        getDestUid();
    }

    //상대방 uid 하나(single) 읽기
    private void getDestUid() {
        databaseReference.child("users").child(destUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                destUser = snapshot.getValue(User.class);

                //채팅 내용 읽어들임
                getMessageList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //채팅 내용 읽어들임
    private void getMessageList() {
        databaseReference.child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel.Comment commentUser = dataSnapshot.getValue(ChatModel.Comment.class);
                    String messageStr = commentUser.getMessage();
                    String timestampStr = commentUser.getTimestamp();
                    String UidStr = commentUser.getUid();
                    if (UidStr.equals(myuid)) {
                        comments.add(new ChatModel.Comment(UidStr, messageStr, timestampStr, 1));
                    } else {
                        comments.add(new ChatModel.Comment(UidStr, messageStr, timestampStr, 0));
                    }
                }
                notifyDataSetChanged();
                mBinding.chattingRecyclerView.scrollToPosition(comments.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
        if (viewHolder instanceof CenterViewHolder) {
//                ((CenterViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
        } else if (viewHolder instanceof LeftViewHolder) {
            ((LeftViewHolder) viewHolder).name.setText(comments.get(position).getUid());
            ((LeftViewHolder) viewHolder).content.setText(comments.get(position).getMessage());
            String chatTimeStr = comments.get(position).getTimestamp();
            String [] array = chatTimeStr.split("-");
            String month = array[1];
            String [] array2 = array[2].split("T");
            String [] array3 = array2[1].split(":");
            String chatTime = month + "월 " + array2[0] + "일 " + array3[0] + ":" + array3[1];
            ((LeftViewHolder) viewHolder).chat_time.setText(chatTime);
            ((LeftViewHolder) viewHolder).chat_time.setText(chatTime);
            databaseReference.child("User").orderByChild("name").equalTo(destUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        profileUrlStr = dataSnapshot.child("profile").getValue(String.class);
                    }
                    Glide.with(context).load(profileUrlStr).into(((LeftViewHolder) viewHolder).image);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

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
