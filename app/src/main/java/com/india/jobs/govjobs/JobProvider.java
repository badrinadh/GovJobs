package com.india.jobs.govjobs;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by badarinadh on 9/4/2016.
 */

public class JobProvider extends ContentProvider {
    public static final String ACTION_DATA_UPDATED =
            "com.example.android.sunshine.app.ACTION_DATA_UPDATED";
    static final String PROVIDER_NAME = "com.india.jobs.govjobs";
    static final String URL = "content://" + PROVIDER_NAME + "/posts";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String JOB_ID = "job_id";
    static final String TITLE = "title";
    static final String IMAGE_URL = "image_url";
    static final String DESCRIPTION = "description";
    static final String POST_DATE = "post_date";
    static final String COMPANY_ID = "company_id";
    static final String COMPANY_NAME = "company_name";
    static final String COMPANY="company";

    private static HashMap<String, String> JOBS_PROJECTION_MAP;

    static final int POSTS = 1;
    static final int POST_ID = 2;
    static final int JOB_SEARCH_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "posts", POSTS);
        uriMatcher.addURI(PROVIDER_NAME, "posts/#", POST_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "jobs";
    static final String JOBS_TABLE_NAME = "posts";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + JOBS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    JOB_ID+" TEXT NOT NULL, " +
                    TITLE+" TEXT NOT NULL, " +
                    IMAGE_URL+" TEXT NOT NULL, " +
                    DESCRIPTION+" TEXT NOT NULL, " +
                    POST_DATE+" TEXT NOT NULL, " +
                    COMPANY_ID+" TEXT NOT NULL, " +
                    COMPANY_NAME+" TEXT NOT NULL);";
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  JOBS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(	JOBS_TABLE_NAME, "", values);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(JOBS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case POSTS:
                qb.setProjectionMap(JOBS_PROJECTION_MAP);
                break;

            case JOB_SEARCH_ID:
                qb.appendWhere( JOB_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = _ID;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case POSTS:
                count = db.delete(JOBS_TABLE_NAME, selection, selectionArgs);
                break;

            case POST_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( JOBS_TABLE_NAME, JOB_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case POSTS:
                count = db.update(JOBS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case POST_ID:
                count = db.update(JOBS_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case POSTS:
                return "vnd.android.cursor.dir/vnd.example.students";

            /**
             * Get a particular student
             */
            case POST_ID:
                return "vnd.android.cursor.item/vnd.example.students";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}