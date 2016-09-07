package com.india.jobs.govjobs;

/**
 * Created by badarinadh on 8/28/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.steamcrafted.materialiconlib.MaterialIconView;
import java.util.ArrayList;

public class CustomPost extends BaseAdapter {
    public static final String ACTION_DATA_UPDATED =
            "com.india.jobs.govjobs.ACTION_DATA_UPDATED";
    public Context context;
    private static LayoutInflater inflater=null;

    ArrayList<String> jobId=new ArrayList<String>();
    ArrayList<String> companyId=new ArrayList<String>();
    ArrayList<String> jobTitle=new ArrayList<String>();
    ArrayList<String> companyName=new ArrayList<String>();
    ArrayList<String> description=new ArrayList<String>();
    ArrayList<String> postDate=new ArrayList<String>();
    ArrayList<String> imageUrl=new ArrayList<String>();
    public CustomPost(Context mainActivity,
                         ArrayList<String> jobId,
                         ArrayList<String > jobTitle,
                         ArrayList<String > companyName,
                         ArrayList<String > description,
                         ArrayList<String > postDate,
                         ArrayList<String > imageUrl,
                      ArrayList<String > companyId
                      ) {
        // TODO Auto-generated constructor stub
        this.jobId=jobId;
        this.jobTitle=jobTitle;
        this.companyName=companyName;
        this.description=description;
        this.postDate=postDate;
        this.imageUrl=imageUrl;
        this.companyId=companyId;
        context=mainActivity;
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
        TextView title,description,postdate,company;
        ImageView companyImage;
        MaterialIconView shareIcon,bookmarkPost;
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.single_post, null);
        holder.title=(TextView) rowView.findViewById(R.id.jobTitleTv);
        holder.companyImage=(ImageView) rowView.findViewById(R.id.companyLogoIv);
        holder.description=(TextView) rowView.findViewById(R.id.descriptionTv);
        holder.company=(TextView) rowView.findViewById(R.id.companyName);
        holder.postdate=(TextView) rowView.findViewById(R.id.postDate);
        holder.title.setText(jobTitle.get(position));
        holder.description.setText(description.get(position));
        holder.company.setText("#"+companyName.get(position));
        holder.postdate.setText(postDate.get(position));
        holder.shareIcon=(MaterialIconView) rowView.findViewById(R.id.sharePost);
        holder.bookmarkPost=(MaterialIconView) rowView.findViewById(R.id.bookmarkPost);

        Picasso.with(context).load(imageUrl.get(position)).into(holder.companyImage);

        String[] selectionArgs1 = {""};
        selectionArgs1[0] = jobId.get(position);

        Cursor c1 = context.getContentResolver().query(JobProvider.CONTENT_URI,null,JobProvider.JOB_ID + "=?",selectionArgs1,null);
        if (c1.moveToFirst()){
            holder.bookmarkPost.setColor(Color.parseColor("#FFC107"));
        }
        holder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,jobTitle.get(position)+" web : url");
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

        holder.bookmarkPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] selectionArgs = {""};
                selectionArgs[0] = jobId.get(position);
                Cursor c = context.getContentResolver().query(JobProvider.CONTENT_URI,null,JobProvider.JOB_ID + "=?",selectionArgs,null);
                if (c.moveToFirst()) {
                    context.getContentResolver().delete(JobProvider.CONTENT_URI,JobProvider.JOB_ID + "=?",selectionArgs);
                    holder.bookmarkPost.setColor(Color.parseColor("#BDBDBD"));
                }else{
                    ContentValues values = new ContentValues();
                    values.put(JobProvider.JOB_ID,jobId.get(position));
                    values.put(JobProvider.TITLE,jobTitle.get(position));
                    values.put(JobProvider.IMAGE_URL,imageUrl.get(position));
                    values.put(JobProvider.DESCRIPTION,description.get(position));
                    values.put(JobProvider.POST_DATE,postDate.get(position));
                    values.put(JobProvider.COMPANY_ID,companyId.get(position));
                    values.put(JobProvider.COMPANY_NAME,companyName.get(position));
                    Uri uri = context.getContentResolver().insert(
                            JobProvider.CONTENT_URI, values);
                    holder.bookmarkPost.setColor(Color.parseColor("#FFC107"));

                    // Setting the package ensures that only components in our app will receive the broadcast
                    Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                            .setPackage(context.getPackageName());
                    context.sendBroadcast(dataUpdatedIntent);

                }
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TastyToast.makeText(context, "post"+position, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                Intent intent=new Intent(context,JobDetails.class);
                intent.putExtra("id",jobId.get(position));
                context.startActivity(intent);
            }
        });

        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,JobDetails.class);
                intent.putExtra("id",jobId.get(position));
                context.startActivity(intent);
            }
        });

        holder.company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TastyToast.makeText(context, "post"+position, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                Intent intent=new Intent(context,CompanyProfile.class);
                intent.putExtra("id",companyId.get(position));
                intent.putExtra("name",companyName.get(position));
                context.startActivity(intent);
            }
        });


        return rowView;
    }
}