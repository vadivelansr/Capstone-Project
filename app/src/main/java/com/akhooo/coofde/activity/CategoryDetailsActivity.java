package com.akhooo.coofde.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akhooo.coofde.Coofde;
import com.akhooo.coofde.R;
import com.akhooo.coofde.adapter.MainViewPagerAdapter;
import com.akhooo.coofde.bean.StoreLogo;
import com.akhooo.coofde.config.Constants;
import com.akhooo.coofde.fragment.CategoryFragment;
import com.akhooo.coofde.fragment.StoreOffersFragment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CategoryDetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = CategoryDetailsActivity.class.getSimpleName();
    Firebase ref;
    String categoryKey;
    ArrayList<StoreLogo> mStoreList = new ArrayList<>();

    @Bind(R.id.stores_tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.stores_view_pager)
    ViewPager viewPager;
    @Bind(R.id.toolbar_category)
    Toolbar toolbar;
    @Bind(R.id.progress)
    ProgressBar mProgressBar;
    @Bind(R.id.adView)
    AdView mAdView;
    @Bind(R.id.coofde_status)
    TextView coofdeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        ButterKnife.bind(this);
        setupWindowAnimations();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        ((Coofde)getApplication()).startTracking();

        categoryKey = getIntent().getStringExtra(CategoryFragment.CATEGORY_KEY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(categoryKey);

        ref = Coofde.getFirebaseRef();
        Log.e(LOG_TAG, "Category:" + categoryKey);
        Log.e(LOG_TAG, "Path:" + ref.toString());
        viewPager.setOffscreenPageLimit(1);
        fetchStores();

    }

    public void setUpViewPager(ViewPager paramViewPager, ArrayList<StoreLogo> storeList ) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        Log.e(LOG_TAG, "Viewpagr");
        for (StoreLogo store : storeList) {
            String storeTitle = store.getStore();
            adapter.addFragment(StoreOffersFragment.newInstance(storeTitle, categoryKey),storeTitle.toUpperCase());
            Log.e(LOG_TAG, storeTitle);
        }
        paramViewPager.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    private void setupWindowAnimations() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);

            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setReturnTransition(slide);
        }
    }

    public ArrayList<StoreLogo> fetchStores() {
         if (!TextUtils.isEmpty(categoryKey)) {
            mStoreList.clear();
            ref.child(Constants.CATEGORY).child(categoryKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            StoreLogo storeLogo = dataSnapshot1.getValue(StoreLogo.class);
                            mStoreList.add(storeLogo);
                            Log.e(LOG_TAG, storeLogo.getStore());
                        }
                        if (mStoreList.size() > 0) {
                            mProgressBar.setVisibility(ProgressBar.GONE);
                            setUpViewPager(viewPager, mStoreList);
                            tabLayout.setupWithViewPager(viewPager);
                        } else {
                            mProgressBar.setVisibility(ProgressBar.GONE);
                            coofdeStatus.setVisibility(View.VISIBLE);
                        }
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                        Log.e(LOG_TAG, Constants.FIREBASE_ERROR);
                }
            });
        }
        return mStoreList;
    }
}
