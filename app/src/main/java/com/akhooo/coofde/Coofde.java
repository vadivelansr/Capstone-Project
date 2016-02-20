package com.akhooo.coofde;

import android.app.Application;

import com.akhooo.coofde.config.Constants;
import com.akhooo.coofde.config.CoofdePrefManager;
import com.firebase.client.Firebase;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;


/**
 * Created by vadivelansr on 1/6/2016.
 */
public class Coofde extends Application {
    private static  Firebase ref;
    private Tracker mTracker;
    @Override
    public void onCreate() {
        super.onCreate();
       // initParse();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        CoofdePrefManager.getInstance().initialize(this);


    }

    public static Firebase getFirebaseRef(){
        if(ref == null){
            ref = new Firebase(Constants.FIREBASE_URL);
        }
        return ref;

    }
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
             mTracker = analytics.newTracker(R.xml.coofde_tracker);

        }
        return mTracker;
    }

    public void startTracking(){
        if(mTracker == null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.coofde_tracker);
            analytics.enableAutoActivityReports(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }
}
