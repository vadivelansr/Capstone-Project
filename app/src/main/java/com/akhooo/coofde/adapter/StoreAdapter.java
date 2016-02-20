package com.akhooo.coofde.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akhooo.coofde.R;
import com.akhooo.coofde.bean.StoreBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vadivelansr on 2/8/2016.
 */
public class StoreAdapter extends ArrayAdapter<StoreBean> {
    public StoreAdapter(Context context, ArrayList<StoreBean> items) {
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
        StoreBean storeBean = getItem(position);
        Picasso.with(getContext()).load(storeBean.getLogoUrl()).placeholder(R.drawable.loading)
                .error(R.drawable.error).into(categoryViewHolder.imageView);
        Picasso.with(getContext()).load(storeBean.getBackdrop()).fetch();
        categoryViewHolder.textView.setText(storeBean.getName());

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
