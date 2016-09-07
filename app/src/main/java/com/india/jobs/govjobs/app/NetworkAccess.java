package com.india.jobs.govjobs.app;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by badarinadh on 7/27/2016.
 */

public class NetworkAccess {
    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}