package com.india.jobs.govjobs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by badarinadh on 8/2/2016.
 */

public class LoadingDialog extends AlertDialog {

    private TextView mMessageView;

    public LoadingDialog(Context context) {
        super(context);
        View view= LayoutInflater.from(getContext()).inflate(R.layout.loading_layout,null);
        //mMessageView= (TextView) view.findViewById(R.id.message);
        setView(view);
    }

    @Override
    public void setMessage(CharSequence message) {
        //mMessageView.setText(message);
    }
}