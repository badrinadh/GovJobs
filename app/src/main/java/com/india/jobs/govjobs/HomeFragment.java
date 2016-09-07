package com.india.jobs.govjobs;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.india.jobs.govjobs.app.AppConfig;
import com.india.jobs.govjobs.app.JsonParser;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment{
    int count = 0;
    Context context=getActivity();
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
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        postsLv=(ListView) view.findViewById(R.id.jobsLv);

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
        return view;
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