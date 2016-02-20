package com.akhooo.coofde.utils;

import com.akhooo.coofde.Coofde;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by vadivelansr on 2/15/2016.
 */
public class Analytics {
    public static void startTracking(Coofde application, String screenName){
        Tracker tracker = application.getDefaultTracker();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
