package com.akhooo.coofde.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;

import com.akhooo.coofde.Coofde;
import com.akhooo.coofde.R;
import com.akhooo.coofde.adapter.MainViewPagerAdapter;
import com.akhooo.coofde.fragment.CategoryFragment;
import com.akhooo.coofde.fragment.StoresFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce = false;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.main_tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.main_view_pager)
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupWindowAnimations();
        setSupportActionBar(toolbar);
        ((Coofde)getApplication()).startTracking();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
               /* if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);*/
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {

                    case R.id.navigation_home:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_favourites:
                        intent = new Intent(MainActivity.this, FavouritesActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_about_us:
                        intent = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_contact_us:
                        intent = new Intent(Intent.ACTION_SEND);
                        //intent.setData(Uri.parse("mailto:"));
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.contact_email)});
                        if(intent.resolveActivity(getPackageManager()) != null){
                            startActivity(intent);
                        }
                        return true;
                    default:
                         return true;
                }

            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupWindowAnimations() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);
            Fade fade1 = new Fade();
            fade1.setDuration(1000);
            getWindow().setReturnTransition(fade1);
        }
    }

    public void setUpViewPager(ViewPager paramViewPager){
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CategoryFragment(), getString(R.string.category));
        adapter.addFragment(new StoresFragment(), getString(R.string.stores));
        paramViewPager.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(coordinatorLayout, getString(R.string.exit_feedback), Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);
    }


}
