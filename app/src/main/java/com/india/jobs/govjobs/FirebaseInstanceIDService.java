package com.india.jobs.govjobs;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by badarinadh on 6/27/2016.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token= FirebaseInstanceId.getInstance().getId();
        registerToken(token);
    }

    private void registerToken(String token) {
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("token",token)
                .build();

        Request request=new Request.Builder()
                .url("http://192.168.0.111/srs/test/")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}