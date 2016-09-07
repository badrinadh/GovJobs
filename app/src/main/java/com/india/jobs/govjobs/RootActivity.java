package com.india.jobs.govjobs;

/**
 * Created by badarinadh on 8/5/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.india.jobs.govjobs.app.NetworkAccess;
import com.sdsmdg.tastytoast.TastyToast;



public class RootActivity extends AppCompatActivity {
    int onStartCount = 0;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!NetworkAccess.isInternetAvailable(context)){
            TastyToast.makeText(context, "No Connectivity", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);

        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }
}