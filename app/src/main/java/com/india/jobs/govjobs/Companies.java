package com.india.jobs.govjobs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.india.jobs.govjobs.app.AppConfig;
import com.india.jobs.govjobs.app.JsonParser;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Companies extends RootActivity {
    LoadingDialog ld;
    AppConfig ac=new AppConfig();
    ListView companies;
    int companiesCount;
    ArrayList<String> companyName=new ArrayList<String>();
    ArrayList<String> imageUrl=new ArrayList<String>();
    ArrayList<String> companyId=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies);

        companies=(ListView) findViewById(R.id.companiesLv);
        companies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent=new Intent(context,CompanyProfile.class);
                intent.putExtra("id",companyId.get(position));
                intent.putExtra("name",companyName.get(position));
                startActivity(intent);
            }
        });
        new loadPosts().execute();
    }

    public class loadPosts extends AsyncTask<String, String, String> {
        // set your json string url here
        String yourJsonStringUrl = AppConfig.CompaniesUrl;
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
                String fKeys[]={"auth"};
                String fValues[]={"123"};

                // instantiate our json parser
                JsonParser jParser = new JsonParser(fKeys,fValues);
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                dataJsonArr = json.getJSONArray("companies");
                companiesCount=dataJsonArr.length();
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    imageUrl.add(c.getString("company_logo"));
                    companyName.add(c.getString("company_name"));
                    companyId.add(c.getString("cid"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            if(companiesCount==0){
                TastyToast.makeText(context, "please try again", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }else{
                CustomCompanies adapter = new CustomCompanies(context,companyName,imageUrl,companyId);
                companies.setAdapter(adapter);
            }
            ac.hideLoader(ld);
        }
    }
}
