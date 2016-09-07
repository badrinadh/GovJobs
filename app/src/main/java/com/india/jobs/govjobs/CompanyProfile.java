package com.india.jobs.govjobs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.india.jobs.govjobs.app.AppConfig;
import com.india.jobs.govjobs.app.JsonParser;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CompanyProfile extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int followStatus=0;
    String companyId,companyNameSmall,companyNameFull,companyUrl,fallowing="";
    Context context=this;
    LoadingDialog ld;
    AppConfig ac=new AppConfig();
    ImageView companyIv;
    TextView companyNameTv;
    int jobCount,jobCount1;
    ListView postsLv;
    ArrayList<String> jobId=new ArrayList<String>();
    ArrayList<String> companyIds=new ArrayList<String>();
    ArrayList<String> jobTitle=new ArrayList<String>();
    ArrayList<String> companyName=new ArrayList<String>();
    ArrayList<String> description=new ArrayList<String>();
    ArrayList<String> postDate=new ArrayList<String>();
    ArrayList<String> imageUrl=new ArrayList<String>();
    ArrayList<String> fallowingComapines=new ArrayList<String>();
    Button fallow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            companyId = bundle.getString("id");
            companyNameSmall=bundle.getString("name");
            Toast.makeText(context,companyId,Toast.LENGTH_SHORT).show();
        }
        setTitle(companyNameSmall);

        pref =getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        fallow=(Button) findViewById(R.id.fallow);
        fallowing=pref.getString("following","");
        Log.e("follow",fallowing);
        String data = fallowing;
        List<String> myList = new ArrayList<String>(Arrays.asList(data.split(",")));

        if (myList.contains("com_"+companyId)) {
            followStatus=1;
            fallow.setText("UnFollow");
        }else{
            followStatus=0;
        }

        fallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(followStatus==0){
                    followStatus=1;
                    fallowing=pref.getString("following","");
                    fallow.setText("UnFollow");
                    editor.putString("following",fallowing+",com_"+companyId);
                    editor.commit();
                    FirebaseMessaging.getInstance().subscribeToTopic("com_"+companyId);
                    Log.i("Result", pref.getString("following",""));
                }else{
                    followStatus=0;
                    fallowing=pref.getString("following","");
                    String data1 = fallowing;
                    List<String> myList1 = new ArrayList<String>(Arrays.asList(data1.split(",")));
                    myList1.remove("com_"+companyId);
                    String allIds = TextUtils.join(",", myList1);

                    editor.putString("following",allIds);
                    editor.commit();
                    fallow.setText("Follow");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("com_"+companyId);
                    Log.i("Result", pref.getString("following",""));
                }
            }
        });

        companyIv=(ImageView) findViewById(R.id.company_image);
        companyNameTv=(TextView) findViewById(R.id.companyNameTv);
        postsLv=(ListView) findViewById(R.id.jobsLv);

        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        new getJobData().execute();
    }

    public class getJobData extends AsyncTask<String, String, String> {
        // set your json string url here
        String yourJsonStringUrl = AppConfig.CompanyProfileUrl;
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
                String fKeys[]={"auth","id"};
                String fValues[]={"123",companyId};

                // instantiate our json parser
                JsonParser jParser = new JsonParser(fKeys,fValues);
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                dataJsonArr = json.getJSONArray("profile");
                jobCount=dataJsonArr.length();
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    companyNameFull=c.getString("company_name");
                    companyUrl=c.getString("company_logo");
                }

                dataJsonArr = json.getJSONArray("jobs");
                jobCount1=dataJsonArr.length();
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    jobId.add(c.getString("pid"));
                    companyIds.add(c.getString("cid"));
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
                TastyToast.makeText(context, "Please try again", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }else{
                Picasso.with(context).load(companyUrl).into(companyIv);
                companyNameTv.setText(companyNameFull);

                CustomPost adapter = new CustomPost(context,jobId,jobTitle,companyName,description,postDate,imageUrl,companyIds);
                postsLv.setAdapter(adapter);
            }
            ac.hideLoader(ld);
        }
    }

}