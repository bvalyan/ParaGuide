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
 * Created by bvaly on 2/19/2017.
 */
public class HeroAPIStats extends AsyncTask<String, Void, String> {

    ProgressDialog progressDialog;
    Context mcontext;

    public HeroAPIStats(Context context){
        this.mcontext = context;
    }

    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... Strings) {
        String accountID =  Strings[0];
        String heroID = Strings[1];

        URL url2 = null;
        URL url3 = null;
        HttpURLConnection urlConnection2 = null;
        HttpURLConnection urlConnection3 = null;
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();

        try {

            url3 = new URL("https://developer-paragon.epicgames.com/v1/account/" + accountID + "/stats/hero/" + heroID);
            urlConnection3 = (HttpURLConnection) url3.openConnection();
            urlConnection3.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);

            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(urlConnection3.getInputStream()));

            String line3;
            while ((line3 = bufferedReader3.readLine()) != null) {
                stringBuilder3.append(line3).append("\n");
            }
            bufferedReader3.close();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        return stringBuilder3.toString();
    }

    protected void onPostExecute(){
        progressDialog.dismiss();
    }
}
