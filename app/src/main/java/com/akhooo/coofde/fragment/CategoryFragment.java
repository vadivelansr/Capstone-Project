package com.akhooo.coofde.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.akhooo.coofde.R;
import com.akhooo.coofde.activity.CategoryDetailsActivity;
import com.akhooo.coofde.adapter.CategoryAdapter;
import com.akhooo.coofde.bean.CategoryItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    public static final String CATEGORY_KEY = "category_key" ;
    ArrayList<CategoryItem> categoryList = new ArrayList<>();
    @Bind(R.id.category_grid_view)
    GridView gridView;
    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, rootView);
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), loadCategory());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CategoryDetailsActivity.class);
                String categoryKey = ((CategoryItem)categoryList.get(position)).getTitle();
                 intent.putExtra(CATEGORY_KEY, categoryKey);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public ArrayList<CategoryItem> loadCategory(){
        categoryList.clear();
        String[] titleList = getResources().getStringArray(R.array.category_title);
        String[] drawableList = getResources().getStringArray(R.array.category_drawable);
        for(int i=0; i<titleList.length; i++){
            CategoryItem item = new CategoryItem();
            item.setTitle(titleList[i]);
            item.setDrawable(drawableList[i]);
            categoryList.add(item);
        }
        return categoryList;
    }

}
