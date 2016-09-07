package com.india.jobs.govjobs;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class Bookmarked extends AppCompatActivity {

    Context context=this;
    ArrayList<String> jobId=new ArrayList<String>();
    ArrayList<String> companyId=new ArrayList<String>();
    ArrayList<String> jobTitle=new ArrayList<String>();
    ArrayList<String> companyName=new ArrayList<String>();
    ArrayList<String> description=new ArrayList<String>();
    ArrayList<String> postDate=new ArrayList<String>();
    ArrayList<String> imageUrl=new ArrayList<String>();
    ListView postsLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked);
        postsLv=(ListView) findViewById(R.id.jobsLv);

        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Cursor c1 = getContentResolver().query(JobProvider.CONTENT_URI,null,null,null,null);
        if (c1.moveToFirst()){
            do {
                jobId.add(c1.getString(c1.getColumnIndex(JobProvider.JOB_ID)));
                companyId.add(c1.getString(c1.getColumnIndex(JobProvider.COMPANY_ID)));
                jobTitle.add(c1.getString(c1.getColumnIndex(JobProvider.TITLE)));
                companyName.add(c1.getString(c1.getColumnIndex(JobProvider.COMPANY_NAME)));
                description.add(c1.getString(c1.getColumnIndex(JobProvider.DESCRIPTION)));
                postDate.add(c1.getString(c1.getColumnIndex(JobProvider.POST_DATE)));
                imageUrl.add(c1.getString(c1.getColumnIndex(JobProvider.IMAGE_URL)));
            }while (c1.moveToNext());
        }
        CustomPost adapter = new CustomPost(context,jobId,jobTitle,companyName,description,postDate,imageUrl,companyId);
        postsLv.setAdapter(adapter);
    }
}