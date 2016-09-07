package com.india.jobs.govjobs;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Created by badarinadh on 9/6/2016.
 */

public class SampleWidgetProvider4_1 extends AppWidgetProvider {
    private Cursor c1 = null;
    ArrayList<String> jobId=new ArrayList<String>();
    ArrayList<String> jobTitle=new ArrayList<String>();
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // implementation will follow
        RemoteViews remoteViews = new RemoteViews(context.getPackageName (), R.layout.simple_widget);
        Intent intent = new Intent(context, SampleWidgetProvider4_1.class);
        intent.setAction(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET);
        intent.putExtra(Constants.INTENT_EXTRA_WIDGET_TEXT,"Icon clicked on Widget");

        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.icon,actionPendingIntent);

        ComponentName thisWidget = new ComponentName(context, SampleWidgetProvider4_1.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, remoteViews);
    }

    public void onReceive(Context context, Intent intent) {
        Log.i(Constants.TAG, "onReceive called with " + intent.getAction());
        RemoteViews remoteViews = new RemoteViews(context.getPackageName (), R.layout.simple_widget);

        if(CustomPost.ACTION_DATA_UPDATED.equals(intent.getAction())){
            setRemoteAdapter(context,remoteViews);
        }else if (intent.getAction().equals(Constants.ACTION_WIDGET_UPDATE_FROM_WIDGET)){
            setRemoteAdapter(context,remoteViews);
        }else {
            super.onReceive(context, intent);
        }
        ComponentName cn = new ComponentName(context, SampleWidgetProvider4_1.class);
        AppWidgetManager.getInstance(context).updateAppWidget(cn, remoteViews);
    }

    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, DetailWidgetRemoteViewsService.class));
    }
}