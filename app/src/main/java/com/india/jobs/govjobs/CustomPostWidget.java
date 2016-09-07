package com.india.jobs.govjobs;

/**
 * Created by badarinadh on 9/6/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by badarinadh on 9/3/2016.
 */
public class CustomPostWidget extends BaseAdapter {
    public Context context;
    ArrayList<String> title=new ArrayList<String>();
    ArrayList<String> jobId=new ArrayList<String>();

    public CustomPostWidget(Context mainActivity,
                           ArrayList<String> title,
                           ArrayList<String> jobId){
        this.title=title;
        this.jobId=jobId;
        this.context=mainActivity;
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return jobId.size();
    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public class Holder {
        TextView titleTv;
    }
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.single_post_widget, null);

        holder.titleTv=(TextView) rowView.findViewById(R.id.title_job_widget);
        holder.titleTv.setText(title.get(position));
        return rowView;
    }
}