package com.india.jobs.govjobs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.india.jobs.govjobs.app.AppConfig;
import com.india.jobs.govjobs.app.JsonParser;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class JobDetails extends RootActivity {
    String jobId,title,company,description,companyUrl,noPosts,qualification,jobLocation,salary,agelimit,applyProcedure,fee;
    int jobCount;
    TextView titleTv,companyTv,descriptionTv,noPostsTv,qualificationTv,jobLocationTv,salaryTv,agelimitTv,applyProcedureTv,feeTv;
    LoadingDialog ld;
    AppConfig ac=new AppConfig();
    Context context=this;
    ImageView companyIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jobId = bundle.getString("id");
        }

        titleTv=(TextView) findViewById(R.id.jobTitleTv);
        companyTv=(TextView) findViewById(R.id.companyNameTv);
        descriptionTv=(TextView) findViewById(R.id.descriptionTv);
        companyIv=(ImageView) findViewById(R.id.companyLogoIv);
        noPostsTv=(TextView) findViewById(R.id.noPostsTv);
        qualificationTv=(TextView) findViewById(R.id.qualificationTv);
        jobLocationTv=(TextView) findViewById(R.id.joblocationTv);
        salaryTv=(TextView) findViewById(R.id.salaryTv);
        agelimitTv=(TextView) findViewById(R.id.agelimitTv);
        applyProcedureTv=(TextView) findViewById(R.id.applyProcedureTv);
        feeTv=(TextView) findViewById(R.id.feeTv);

        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        new getJobData().execute();
    }

    public class getJobData extends AsyncTask<String, String, String> {
        // set your json string url here
        String yourJsonStringUrl = AppConfig.PostUrl;
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
                String fValues[]={"123",jobId};

                // instantiate our json parser
                JsonParser jParser = new JsonParser(fKeys,fValues);
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                dataJsonArr = json.getJSONArray("details");
                jobCount=dataJsonArr.length();
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    title=c.getString("post_title");
                    description=c.getString("description");
                    company=c.getString("company_name");
                    companyUrl=c.getString("company_logo");
                    noPosts=c.getString("no_of_posts");
                    qualification=c.getString("qualification");
                    jobLocation =c.getString("job_location");
                    salary=c.getString("salary");
                    agelimit=c.getString("age_limit");
                    applyProcedure=c.getString("apply_procedure");
                    fee=c.getString("fee");
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
                titleTv.setText(title);
                companyTv.setText("#"+company);
                descriptionTv.setText(description);
                Picasso.with(context).load(companyUrl).into(companyIv);
                noPostsTv.setText("No of Posts : "+noPosts);
                qualificationTv.setText("Qualification : "+qualification);
                jobLocationTv.setText("Location : "+jobLocation);
                salaryTv.setText("Salary : "+salary);
                agelimitTv.setText("Age Limit : "+agelimit);
                applyProcedureTv.setText("How to Apply : "+applyProcedure);
                feeTv.setText("Fee : "+fee);
            }
            ac.hideLoader(ld);
        }
    }
}