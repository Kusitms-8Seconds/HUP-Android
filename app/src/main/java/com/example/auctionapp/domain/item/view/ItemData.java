package com.example.auctionapp.domain.item.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.user.constant.Constants;

import java.util.ArrayList;
import java.util.Vector;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemData {

    Long itemId;
    String imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int itemPrice;
    String endTime;
    int views;
    Long heart;

    public ItemData(Long itemId, String imageURL, String itemName, int itemPrice, String endTime, int views, Long heart){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.endTime = endTime;
        this.views = views;
        this.heart = heart;
    }
}

@Getter
class ItemDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<ItemData> listData = new ArrayList<>();

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_itemlist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).onBind(context, listData.get(position));
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

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name;
        TextView item_price;
        TextView end_time;
        TextView item_views;
        TextView item_hearts;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.sell_history_ongoing_img);
            item_name = itemView.findViewById(R.id.bt_item_name);
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

        public void onBind(Context context, ItemData data){
            Glide.with(context).load(Constants.imageBaseUrl+data.getImageURL()).override(item_image.getWidth()
                    ,item_image.getHeight()).into(item_image);
            item_image.setClipToOutline(true);  //item 테두리
            item_name.setText(data.getItemName());
            item_price.setText(data.getItemPrice()+"");
            end_time.setText(data.getEndTime());
            item_views.setText(data.getViews()+"");
            item_hearts.setText(data.getHeart()+"");
        }


    }

}