package com.example.auctionapp.domain.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.home.model.AuctionNow;
import com.example.auctionapp.domain.user.constant.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lombok.Getter;

public @Getter
class AuctionNowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<AuctionNow> AuctionNowData = new ArrayList<>();
    Context context;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home_auction_list, parent, false);
        return new AuctionNowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AuctionNowViewHolder) holder).onBind(AuctionNowData.get(position));
    }

    @Override
    public int getItemCount() {
        return AuctionNowData.size();
    }

    public void addItem(AuctionNow data) {
        // 외부에서 item을 추가시킬 함수입니다.
        AuctionNowData.add(data);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mAuctionListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mAuctionListener = listener;
    }

    public class AuctionNowViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name;
        TextView item_upPrice;
        TextView item_date;
        TextView item_info;
        TextView upArrow;
        TextView won;
        LinearLayout ly_price;
        DecimalFormat myFormatter = new DecimalFormat("###,###");

        public AuctionNowViewHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.sell_history_ongoing_img);
            item_name = itemView.findViewById(R.id.sell_history_ongoing_edt_name);
            item_upPrice = itemView.findViewById(R.id.auc_list_price);
            item_date = itemView.findViewById(R.id.sell_history_ongoing_max);
            item_info = itemView.findViewById(R.id.sell_history_ongoing_myPrice);
            upArrow = itemView.findViewById(R.id.upArrow);
            won = itemView.findViewById(R.id.won);
            ly_price = itemView.findViewById(R.id.ly_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mAuctionListener != null) {
                            mAuctionListener.onItemClick(v, pos);
                        }
                    }
                }
            });

        }

        public void onBind(AuctionNow data) {
            if(data.getImageURL() == null) {
                Glide.with(context).load(context.getString(R.string.hup_icon_url)).override(item_image.getWidth()
                        ,item_image.getHeight()).into(item_image);
            } else {
                Glide.with(context).load(Constants.imageBaseUrl+data.getImageURL()).override(item_image.getWidth()
                        ,item_image.getHeight()).into(item_image);
            }
            item_image.setClipToOutline(true);  //item 테두리
            item_name.setText(data.getItemName());
            int dif = data.getItemPrice();
            if(dif <= 0) {
                upArrow.setText("");
                won.setVisibility(View.GONE);
                item_upPrice.setText("ㅡ");
                item_upPrice.setTextColor(Color.BLUE);
                item_upPrice.setTypeface(item_upPrice.getTypeface(), Typeface.BOLD);
                ly_price.setBackgroundResource(R.drawable.dialog_edge);
            } else
                item_upPrice.setText(myFormatter.format(data.getItemPrice()));
            item_date.setText(data.getDate());
            if(data.getDate().equals("경매 시간 종료")) item_date.setTypeface(item_date.getTypeface(), Typeface.BOLD);
            item_info.setText(data.getItemInfo() + "");
        }
    }
}
