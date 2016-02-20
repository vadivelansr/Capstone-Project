package com.akhooo.coofde.activity;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.akhooo.coofde.Coofde;
import com.akhooo.coofde.R;
import com.akhooo.coofde.adapter.FavouritesAdapter;
import com.akhooo.coofde.data.CoofdeColumns;
import com.akhooo.coofde.data.CoofdeProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavouritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.favourites_status)
    TextView mFavouritesStatus;


    FavouritesAdapter favouritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);
        setupWindowAnimations();
        ((Coofde)getApplication()).startTracking();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.favourites));

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = CoofdeColumns.COOFDE_ID + " DESC";
        return new CursorLoader(this, CoofdeProvider.Coofde.CONTENT_URI, null, null , null, null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() > 0) mFavouritesStatus.setVisibility(View.GONE);
        favouritesAdapter = new FavouritesAdapter(this, data, 0  );
        if(findViewById(R.id.favourite_offers_list_tablet) == null){
            ListView listView = (ListView)findViewById(R.id.favourite_offers_list);
            listView.setAdapter(favouritesAdapter);
        }else{
            GridView gridView = (GridView)findViewById(R.id.favourite_offers_list_tablet);
            gridView.setAdapter(favouritesAdapter);
        }
        favouritesAdapter.swapCursor(data);
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

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favouritesAdapter.swapCursor(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
