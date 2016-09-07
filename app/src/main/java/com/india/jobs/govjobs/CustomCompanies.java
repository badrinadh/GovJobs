package com.india.jobs.govjobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by badarinadh on 9/3/2016.
 */
public class CustomCompanies extends BaseAdapter {
    public Context context;
    ArrayList<String> companyName=new ArrayList<String>();
    ArrayList<String> imageUrl=new ArrayList<String>();
    ArrayList<String> companyId=new ArrayList<String>();

    public CustomCompanies(Context mainActivity,
                      ArrayList<String> companyName,
                           ArrayList<String> imageUrl,
                           ArrayList<String> companyId){
        this.companyName=companyName;
        this.imageUrl=imageUrl;
        this.companyId=companyId;
        this.context=mainActivity;
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return companyId.size();
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
        TextView company;
    }
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.single_companies, null);

        holder.company=(TextView) rowView.findViewById(R.id.companyNameTv);
        holder.company.setText(companyName.get(position));
        return rowView;
    }
}