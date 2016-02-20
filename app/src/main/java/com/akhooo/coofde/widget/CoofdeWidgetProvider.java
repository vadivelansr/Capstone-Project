package com.akhooo.coofde.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.akhooo.coofde.R;
import com.akhooo.coofde.activity.FavouritesActivity;

/**
 * Created by vadivelansr on 2/12/2016.
 */
public class CoofdeWidgetProvider extends AppWidgetProvider {
    public static final String ITEM_POSITION  = "ITEM_POSITION";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_listview);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for(int i=0; i<N; i++ ){
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, CoofdeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.coofde_appwidget);
            rv.setRemoteAdapter(R.id.appwidget_listview, intent);
            rv.setEmptyView(R.id.appwidget_listview, R.id.empty_view);

            Intent intentTemplete = new Intent(context, FavouritesActivity.class);
            intentTemplete.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intentTemplete.setData(Uri.parse(intentTemplete.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intentTemplete)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.appwidget_listview, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }
    }
}
