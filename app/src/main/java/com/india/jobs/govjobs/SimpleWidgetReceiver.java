package com.india.jobs.govjobs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by badarinadh on 9/6/2016.
 */
public class SimpleWidgetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName (), R.layout.widget_test);
        if (intent.getAction().equals(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY)) {

        }
    }

    public void onUpdate(){

    }
}
