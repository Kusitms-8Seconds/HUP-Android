package com.example.auctionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemData {
    int imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int itemPrice;
    String endTime;
    int views;
    int heart;

    public ItemData(int imageURL, String itemName, int itemPrice, String endTime, int views, int heart){
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.endTime = endTime;
        this.views = views;
        this.heart = heart;
    }

    public int getImage() {
        return imageURL;
    }

    public void setImage(int imageURL) {
        this.imageURL = imageURL;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }
}

class ItemDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<ItemData> listData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_itemlist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(ItemData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public class ItemViewHolder extends  RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name;
        TextView item_price;
        TextView end_time;
        TextView item_views;
        TextView item_hearts;



        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            end_time = itemView.findViewById(R.id.end_time);
            item_views = itemView.findViewById(R.id.item_views);
            item_hearts = itemView.findViewById(R.id.item_hearts);

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

        public void onBind(ItemData data){
            item_image.setImageResource(data.getImage());
            item_image.setClipToOutline(true);  //item 테두리
            item_name.setText(data.getItemName());
            item_price.setText(data.getItemPrice()+"");
            end_time.setText(data.getEndTime());
            item_views.setText(data.getViews()+"");
            item_hearts.setText(data.getHeart()+"");
        }
    }
}