package com.akhooo.coofde.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akhooo.coofde.R;
import com.akhooo.coofde.activity.StoreDetailsActivity;
import com.akhooo.coofde.adapter.StoreAdapter;
import com.akhooo.coofde.bean.StoreBean;
import com.akhooo.coofde.config.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoresFragment extends Fragment {
    private String LOG_TAG = StoresFragment.class.getSimpleName();
    ArrayList<StoreBean> mStoreList = new ArrayList<>();
    @Bind(R.id.category_grid_view)
    GridView mGridView;
    @Bind(R.id.progress)
    ProgressBar mProgressBar;
    @Bind(R.id.coofde_status)
    TextView coofdeStatus;

    Firebase mRef;
    StoreAdapter mAdapter;

    public StoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stores, container, false);
        ButterKnife.bind(this, rootView);
        mRef = new Firebase("https://coofde.firebaseio.com/StoresList");
        loadStores();
        mAdapter = new StoreAdapter(getActivity(), mStoreList);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), StoreDetailsActivity.class);
                StoreBean storeBean = mStoreList.get(position);
                intent.putExtra(Constants.KEY_STORE, storeBean.getName());
                intent.putExtra(Constants.BACKDROP, storeBean.getBackdrop());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ImageView logo = (ImageView) view.findViewById(R.id.imageview_category);
                    String transitionNameLogo = "logo" + position;
                    logo.setTransitionName(transitionNameLogo);
                    intent.putExtra(Constants.TRANSITION_NAME_LOGO, transitionNameLogo);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), logo, transitionNameLogo);
                    getActivity().startActivity(intent, options.toBundle());

                } else {
                    startActivity(intent);
                }


            }
        });
        return rootView;
    }

    public ArrayList<StoreBean> loadStores() {
        mStoreList.clear();
        Query ref = mRef.orderByChild("name");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    StoreBean storeBean = dataSnapshot1.getValue(StoreBean.class);
                    mStoreList.add(storeBean);
                }
                if (mStoreList.size() > 0) {
                    mProgressBar.setVisibility(ProgressBar.GONE);

                    mAdapter.notifyDataSetChanged();
                } else {
                    coofdeStatus.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG, Constants.FIREBASE_ERROR);
            }
        });


        return mStoreList;
    }


}
