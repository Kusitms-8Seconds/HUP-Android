package com.example.auctionapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Interests extends AppCompatActivity {
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);


        // 뒤로가기 화살표
        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // 완료txt
        TextView completeB = (TextView) findViewById(R.id.completeB);
        completeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
            }
        });

        GridView categoryView = (GridView) findViewById(R.id.interestCT);
        adapter = new CategoryAdapter();
        categoryView.setAdapter(adapter);
        init();

        // 이벤트 처리 리스너 설정
        categoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category item = (Category) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 :"+item.getCategory(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void init() {
        adapter.addItem(new Category(R.drawable.necktie, "남성 패션/잡화"));
        adapter.addItem(new Category(R.drawable.womans_clothes, "여성 패션"));
        adapter.addItem(new Category(R.drawable.lipstick, "뷰티/미용"));
        adapter.addItem(new Category(R.drawable.game, "게임/취미"));
        adapter.addItem(new Category(R.drawable.bowling, "스포츠/레저"));
        adapter.addItem(new Category(R.drawable.television, "생활가전"));
        adapter.addItem(new Category(R.drawable.desktop, "디지털기기"));
        adapter.addItem(new Category(R.drawable.dog, "반려동물 용품"));
        adapter.addItem(new Category(R.drawable.book, "도서/음반"));
        adapter.addItem(new Category(R.drawable.plant, "식물"));
        adapter.addItem(new Category(R.drawable.baby, "유아용품"));
        adapter.addItem(new Category(R.drawable.bed, "가구"));

    }
}
class Category {
    int image;
    String category;

    public Category(int image, String category) {
        this.image = image;
        this.category = category;
    }
    public String getCategory() {return category;}
    public int getImage() {return image;}
    public void setCategory(String category) { this.category = category; }
    public void setImage(int image) {this.image = image;}
}
class CategoryAdapter extends BaseAdapter {
    ArrayList<Category> items = new ArrayList<Category>();
    Context context;

    public void addItem(Category item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        Category category = items.get(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_gridview_category, parent, false);
        }

        ImageView category_color = (ImageView) convertView.findViewById(R.id.category_color);
        ImageView category_pic = (ImageView) convertView.findViewById(R.id.category_pic);
        TextView category_name = (TextView) convertView.findViewById(R.id.category_name);

        category_color.setImageResource(R.drawable.category_white);
        category_pic.setImageResource(category.getImage());
        category_name.setText(category.getCategory());

        return convertView;
    }
}
