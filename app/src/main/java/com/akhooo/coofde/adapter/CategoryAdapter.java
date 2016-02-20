package com.akhooo.coofde.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akhooo.coofde.R;
import com.akhooo.coofde.bean.CategoryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vadivelansr on 1/8/2016.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryItem> {
    public CategoryAdapter(Context context, ArrayList<CategoryItem> items) {
        super(context, 0, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryViewHolder categoryViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_category, null);
            categoryViewHolder = new CategoryViewHolder(convertView);
            convertView.setTag(categoryViewHolder);
        } else {
            categoryViewHolder = (CategoryViewHolder) convertView.getTag();
        }
        CategoryItem categoryItem = getItem(position);
        int id = getContext().getResources().getIdentifier(categoryItem.getDrawable(), "drawable", getContext().getPackageName());
        Picasso.with(getContext()).load(id).into(categoryViewHolder.imageView);
        categoryViewHolder.textView.setText(categoryItem.getTitle());

        return convertView;
    }

    public static class CategoryViewHolder {
        @Bind(R.id.imageview_category)
        ImageView imageView;
        @Bind(R.id.textview_category)
        TextView textView;

        public CategoryViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
