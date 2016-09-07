package com.india.jobs.govjobs;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.india.jobs.govjobs.app.AppConfig;
import com.india.jobs.govjobs.app.JsonParser;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends RootActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int count = 0;
    Context context=this;
    private boolean mTwoPane;
    LoadingDialog ld;
    AppConfig ac=new AppConfig();
    int jobCount=0;
    int totalCount=0;
    private FirebaseAnalytics mFirebaseAnalytics;
    ListView postsLv;
    boolean isLoading=false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    ArrayList<String> jobId=new ArrayList<String>();
    ArrayList<String> companyId=new ArrayList<String>();
    ArrayList<String> jobTitle=new ArrayList<String>();
    ArrayList<String> companyName=new ArrayList<String>();
    ArrayList<String> description=new ArrayList<String>();
    ArrayList<String> postDate=new ArrayList<String>();
    ArrayList<String> imageUrl=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (findViewById(R.id.job_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.job_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        postsLv=(ListView) findViewById(R.id.jobsLv);
        postsLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                if (lastIndexInScreen>= totalItemCount && 	!isLoading && firstVisibleItem!=0){
                    // It is time to load more items
                    isLoading = true;
                    new loadPosts().execute();
                }
            }
        });

        new loadPosts().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(count == 1) {
                count=0;
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startMain);
            }
            else{
                TastyToast.makeText(context, "Press again to exit", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                count++;
            }
        }
        return;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homeNav) {
            Intent intent=new Intent(context,HomeActivity.class);
            startActivity(intent);
        }else if(id == R.id.companiesNav){
            Intent intent=new Intent(context,Companies.class);
            startActivity(intent);
        }else if(id == R.id.bookmarkedNav){
            Intent intent=new Intent(context,Bookmarked.class);
            startActivity(intent);
        }else if(id == R.id.nav_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"app-url");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class loadPosts extends AsyncTask<String, String, String> {
        // set your json string url here
        String yourJsonStringUrl = AppConfig.JobsUrl;
        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ld=ac.showLoader(context);
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String fKeys[]={"auth","count"};
                String fValues[]={"123",""+totalCount};

                // instantiate our json parser
                JsonParser jParser = new JsonParser(fKeys,fValues);
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                dataJsonArr = json.getJSONArray("jobs");
                jobCount=dataJsonArr.length();
                totalCount=totalCount+jobCount;
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    jobId.add(c.getString("pid"));
                    companyId.add(c.getString("cid"));
                    jobTitle.add(c.getString("post_title"));
                    imageUrl.add(c.getString("company_logo"));
                    description.add(c.getString("description"));
                    companyName.add(c.getString("company_name"));
                    postDate.add(c.getString("post_date"));


                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            if(jobCount==0){
                TastyToast.makeText(context, "No Updates", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }else{
                isLoading = false;
                int lastViewedPosition = postsLv.getFirstVisiblePosition();
                //get offset of first visible view
                View v = postsLv.getChildAt(0);
                int topOffset = (v == null) ? 0 : v.getTop();
                CustomPost adapter = new CustomPost(context,jobId,jobTitle,companyName,description,postDate,imageUrl,companyId);
                postsLv.setAdapter(adapter);
                postsLv.setSelectionFromTop(lastViewedPosition, topOffset);
            }
            ac.hideLoader(ld);
        }
    }
}