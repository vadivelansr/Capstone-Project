package com.akhooo.coofde.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akhooo.coofde.Coofde;
import com.akhooo.coofde.R;
import com.akhooo.coofde.bean.Backdrop;
import com.akhooo.coofde.bean.OfferBean;
import com.akhooo.coofde.config.Constants;
import com.akhooo.coofde.data.CoofdeColumns;
import com.akhooo.coofde.data.CoofdeProvider;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfferDetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = OfferDetailsActivity.class.getSimpleName();
    private Firebase mRef;
    private OfferBean mOfferBean = null;
    String mStore;
    String mCategory;
    String mListId;

    ClipboardManager mClipboardManager;
    private ShareActionProvider mShareAction;
    private Intent shareIntent;
    private String mShareData;
    private boolean mIsFavourite = false;
    private Snackbar mSnackbar;
    private DBTask mDbTask;

    @Bind(R.id.offer_title)
    TextView mOfferTitle;
    @Bind(R.id.offer_views)
    TextView mOfferViews;
    @Bind(R.id.offer_desc)
    TextView mOfferDesc;
    @Bind(R.id.offer_views_label)
    TextView mOfferViewsLabel;
    @Bind(R.id.toolbar_category)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.button_offer_operation)
    Button mOfferButton;
    @Bind(R.id.fab_favourite)
    FloatingActionButton mFabFavourite;
    @Bind(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.header_image_view)
    ImageView imageView;

    Boolean mIsCoupon = false;
    String mUrl;
    String mCode;
    ArrayList<String> backdropUrls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        ButterKnife.bind(this);
        setupWindowAnimations();
        ((Coofde) getApplication()).startTracking();

        mListId = this.getIntent().getStringExtra(Constants.KEY_LIST_ID);
        mDbTask = new DBTask();
        mDbTask.execute(mListId);
        mStore = this.getIntent().getStringExtra(Constants.KEY_STORE);
        mCategory = this.getIntent().getStringExtra(Constants.KEY_CATEGORY);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mStore + " - " + mCategory);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));

        String refPath = this.getIntent().getStringExtra(Constants.REF_PATH);

        if (refPath == null && TextUtils.isEmpty(refPath)) {
            finish();
            return;
        }

        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        backdropUrls = new ArrayList<String>();
        final Firebase backdropRef = new Firebase(Constants.FIREBASE_URL).child("Images").child(mCategory);

        backdropRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    backdropUrls.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Backdrop backdrop = dataSnapshot1.getValue(Backdrop.class);
                        backdropUrls.add(backdrop.getUrl());
                    }
                    Random r = new Random();
                    if (!backdropUrls.isEmpty() && backdropUrls.size() > 0) {
                        int result = r.nextInt(backdropUrls.size());
                        String url = backdropUrls.get(result);
                        if (!TextUtils.isEmpty(url))
                            Picasso.with(OfferDetailsActivity.this).load(backdropUrls.get(result)).placeholder(R.color.colorPrimary)
                                    .into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRef = new Firebase(refPath);

        mRef.addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           mOfferBean = dataSnapshot.getValue(OfferBean.class);
                                           if (mOfferBean != null) {
                                               progressBar.setVisibility(ProgressBar.GONE);
                                               mOfferTitle.setText(mOfferBean.getTitle());
                                               mOfferDesc.setText(mOfferBean.getDesc());
                                               mOfferViewsLabel.setVisibility(View.VISIBLE);
                                               mOfferViews.setText(Long.toString(mOfferBean.getViews()));
                                               mIsCoupon = mOfferBean.getCoupon() == 1;
                                               if (mIsCoupon) {
                                                   mOfferButton.setText(getString(R.string.get_code));

                                               } else {
                                                   mOfferButton.setText(getString(R.string.activate_deal));
                                               }
                                               mUrl = mOfferBean.getUrl();
                                               mCode = mOfferBean.getCode();

                                               mShareData = mOfferBean.getTitle() + "\n" + getString(R.string.share_description);
                                               setupShareIntent();
                                           }

                                       }

                                       @Override
                                       public void onCancelled(FirebaseError firebaseError) {
                                           Log.e(LOG_TAG, Constants.FIREBASE_ERROR);
                                       }
                                   }

        );

        Firebase viewsRef = mRef.child(Constants.VIEWS);
        viewsRef.runTransaction(new Transaction.Handler()

                                {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData currentData) {
                                        if (currentData == null) {
                                            currentData.setValue(1);
                                        } else {
                                            currentData.setValue((Long) currentData.getValue() + 1);
                                        }
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot
                                            dataSnapshot) {

                                    }
                                }

        );

    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);

            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setReturnTransition(slide);
        }
    }

    private void setupShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mShareData);

        if (mShareAction != null) {
            mShareAction.setShareIntent(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_offer, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return super.onCreateOptionsMenu(menu);
    }

    private Cursor checkFavourite(String coofdeId) {
        Cursor c = null;
        if (coofdeId != null && !coofdeId.isEmpty()) {
            String[] projection = {CoofdeColumns.COOFDE_ID};
            String selectionClause = CoofdeColumns.COOFDE_ID + "=?";
            String[] selectionArgs = {coofdeId};
            String sortOrder = "_id ASC";
            c = this.getContentResolver().query(CoofdeProvider.Coofde.CONTENT_URI, projection,
                    selectionClause, selectionArgs, sortOrder);
        }
        return c;
    }

    private Snackbar snackBar(View view, String snackbarData) {
        mSnackbar = Snackbar.make(mCoordinatorLayout, snackbarData, Snackbar.LENGTH_SHORT);
        return mSnackbar;
    }

    @OnClick(R.id.fab_favourite)
    public void onClickFabFavourite(View view) {
        if (mIsFavourite) {
            int rows = deleteCoofde();
            if (rows > 0) {
                snackBar(view, getResources().getString(R.string.snackbar_remove_favourites));
                if (mSnackbar != null && mSnackbar.isShown()) {
                    mSnackbar.dismiss();
                }
                mSnackbar.show();
                mFabFavourite.setImageDrawable(getResources().getDrawable(R.drawable.button_fav_star_off));
                mIsFavourite = false;
            }
        } else {
            insertCoofde();
            snackBar(view, getResources().getString(R.string.snackbar_add_favourites));
            if (mSnackbar != null && mSnackbar.isShown()) {
                mSnackbar.dismiss();
            }
            mSnackbar.show();
            mFabFavourite.setImageDrawable(getResources().getDrawable(R.drawable.button_fav_star_on));
            mIsFavourite = true;
        }
    }

    private int deleteCoofde() {
        String selectionClause = CoofdeColumns.COOFDE_ID + "=?";
        String[] selectionArgs = {mListId};
        int rows = this.getContentResolver().delete(CoofdeProvider.Coofde.CONTENT_URI,
                selectionClause, selectionArgs);
        return rows;
    }

    private void insertCoofde() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CoofdeColumns.COOFDE_ID, mListId);
        contentValues.put(CoofdeColumns.CODE, mOfferBean.getCode());
        contentValues.put(CoofdeColumns.COUPON, mOfferBean.getCoupon());
        contentValues.put(CoofdeColumns.DESC, mOfferBean.getDesc());
        contentValues.put(CoofdeColumns.TITLE, mOfferBean.getTitle());
        contentValues.put(CoofdeColumns.TYPE, mOfferBean.getType());
        contentValues.put(CoofdeColumns.VIEWS, mOfferBean.getViews());
        contentValues.put(CoofdeColumns.URL, mOfferBean.getUrl());
        contentValues.put(CoofdeColumns.STORE, mStore);

        this.getContentResolver().insert(CoofdeProvider.Coofde.CONTENT_URI, contentValues);

    }

    @OnClick(R.id.button_offer_operation)
    public void onOperationClick(View view) {
        if (mIsCoupon) {
            mOfferButton.setText(mCode);
            ClipData clipData = ClipData.newPlainText(Constants.KEY_CODE, mCode);
            mClipboardManager.setPrimaryClip(clipData);
            Toast.makeText(OfferDetailsActivity.this, getString(R.string.code_copied), Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(mUrl)) {
                mOfferButton.setText(getString(R.string.deal_activated));
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                startActivity(Intent.createChooser(sendIntent, getString(R.string.open_browser)));
            }
        }
    }

    private class DBTask extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(String... string) {
            return checkFavourite(string[0]);
        }

        @Override
        protected void onPostExecute(Cursor c) {
            if (c != null && c.getCount() > 0) {
                mIsFavourite = true;
                mFabFavourite.setImageDrawable(getResources().getDrawable(R.drawable.button_fav_star_on));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbTask.cancel(true);
    }

}
