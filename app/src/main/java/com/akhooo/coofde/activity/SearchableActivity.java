package com.akhooo.coofde.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akhooo.coofde.R;
import com.akhooo.coofde.adapter.FavouritesAdapter;
import com.akhooo.coofde.data.CoofdeColumns;
import com.akhooo.coofde.data.CoofdeProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchableActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.progress)
    ProgressBar mProgressBar;
    @Bind(R.id.search_result)
    TextView mTextView;


    FavouritesAdapter mFavouritesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }
    private void doSearch(String query){
        getSupportActionBar().setTitle(getString(R.string.results_for) + query);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        new SearchTask().execute(query);

    }
    private class SearchTask extends AsyncTask<String, Void, Cursor>{
        @Override
        protected Cursor doInBackground(String... params) {
            String selection = CoofdeColumns.STORE + " LIKE ? OR " + CoofdeColumns.TYPE + " LIKE ? OR "
                    + CoofdeColumns.TITLE + " LIKE ? OR " + CoofdeColumns.DESC + " LIKE ?";
            String[] selectionArgs = {
                    "%" + params[0] + "%",
                    "%" + params[0] + "%",
                    "%" + params[0] + "%",
                    "%" + params[0] + "%"
            };
            Cursor cursor = getContentResolver().query(CoofdeProvider.Coofde.CONTENT_URI,
                    null, selection, selectionArgs, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            mProgressBar.setVisibility(ProgressBar.GONE);
            if(cursor == null){
                mTextView.setVisibility(View.VISIBLE);
            }else if(!cursor.moveToFirst()){
                cursor.close();
                mTextView.setVisibility(View.VISIBLE);
            }else{
                mFavouritesAdapter = new FavouritesAdapter(SearchableActivity.this, cursor, 0 );
                if(findViewById(R.id.search_result_list_tablet) == null){
                    ListView listView = (ListView)findViewById(R.id.search_result_list);
                    listView.setAdapter(mFavouritesAdapter);
                }else{
                    GridView gridView = (GridView)findViewById(R.id.search_result_list_tablet);
                    gridView.setAdapter(mFavouritesAdapter);
                }

            }

        }
    }

}
