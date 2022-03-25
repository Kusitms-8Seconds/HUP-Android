package com.example.auctionapp.domain.scrap.adapter;

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
import com.example.auctionapp.domain.scrap.model.ScrapItem;
import com.example.auctionapp.domain.user.constant.Constants;

import java.util.ArrayList;

public class ScrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<ScrapItem> scrapData = new ArrayList<>();
    Context context;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_scrap_items, parent, false);
        return new com.example.auctionapp.domain.scrap.adapter.ScrapAdapter.ScrapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((com.example.auctionapp.domain.scrap.adapter.ScrapAdapter.ScrapViewHolder)holder).onBind(scrapData.get(position));
    }


    @Override
    public int getItemCount() {
        return scrapData.size();
    }

    public void addItem(ScrapItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        scrapData.add(data);
    }

    // 리스너 객체 참조를 저장하는 변수
    private com.example.auctionapp.domain.scrap.adapter.ScrapAdapter.OnItemClickListener mListener = null ;
    public void setOnItemClickListener(com.example.auctionapp.domain.scrap.adapter.ScrapAdapter.OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    public class ScrapViewHolder extends  RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name;
        TextView item_price;
        TextView end_time;

        public ScrapViewHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.scrap_image);
            item_name = itemView.findViewById(R.id.scrap_name);
            item_price = itemView.findViewById(R.id.scrap_price);
            end_time = itemView.findViewById(R.id.scarp_end_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });

        }

        public void onBind(ScrapItem data){
            if(data.getImageURL() == null) {
                Glide.with(context).load(context.getString(R.string.hup_icon_url)).override(item_image.getWidth()
                        ,item_image.getHeight()).into(item_image);
            } else {
                Glide.with(context).load(Constants.imageBaseUrl+data.getImageURL()).override(item_image.getWidth()
                        ,item_image.getHeight()).into(item_image);
            }
            item_image.setClipToOutline(true);  //item 테두리
            item_name.setText(data.getItemName());
            item_price.setText(data.getItemPrice()+"");
            end_time.setText(data.getEndTime());
        }
    }

}
