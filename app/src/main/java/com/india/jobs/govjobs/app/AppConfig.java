package com.india.jobs.govjobs.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.india.jobs.govjobs.LoadingDialog;

public class AppConfig {
    public static String auth1="6f67931799144f5df38967146da4da47";

    public static String JobsUrl="http://getrecruited.in/V1/posts/getPosts";
    public static String PostUrl="http://getrecruited.in/V1/posts/jobDetails";
    public static String CompaniesUrl="http://getrecruited.in/V1/posts/getCompanies";
    public static String CompanyProfileUrl="http://getrecruited.in/V1/posts/getCompanieDetails";

    public LoadingDialog showLoader(Context context){
        final LoadingDialog dialog=new LoadingDialog(context);
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setLayout(300,300);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public void hideLoader(LoadingDialog dialog){
        dialog.cancel();
    }
}