package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bvalyan on 8/2/17.
 */

public class APICardList extends AsyncTask<Void, Void, String> {

    String authCode;
    Context mcontext;
    String token;
    String userID;
    ProgressDialog progressDialog;

    public APICardList(String aCode, Context context) {
         /*
            This constructor takes 4 parameters:
             - Authorization code
         */

        this.authCode = aCode;
        this.mcontext = context;


    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection urlConnection2 = null;
        URL url2 = null;
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();

        try {

            url2 = new URL("https://developer-paragon.epicgames.com/v1/cards/complete");
            urlConnection2 = (HttpURLConnection) url2.openConnection();
            urlConnection2.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);

            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));

            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                stringBuilder2.append(line2).append("\n");
            }

            bufferedReader2.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder2.toString();
    }

    @Override
    protected void onPostExecute(String response){

    }
}