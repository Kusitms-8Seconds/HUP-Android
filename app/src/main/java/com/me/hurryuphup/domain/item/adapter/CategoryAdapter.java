package com.me.hurryuphup.domain.item.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.hurryuphup.R;
import com.me.hurryuphup.domain.item.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
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

        if(items.get(position).getSelected()) {
            category_color.setImageResource(R.drawable.category_blue);
        }else {
            category_color.setImageResource(R.drawable.category_white);
        }
        category_pic.setImageResource(category.getImage());
        category_name.setText(category.getCategory());


        return convertView;
    }
}
