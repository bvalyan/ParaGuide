package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bvalyan on 8/2/17.
 */

public class APIUserDeckList extends AsyncTask<Void, Void, String> {

    String authCode;
    Context mcontext;
    String token;
    String userID;
    ProgressDialog progressDialog;
    JSONArray minDeck;

    public APIUserDeckList(String aCode, Context context, String userID, String token) {
         /*
            This constructor takes 4 parameters:
             - Authorization code
         */

        this.authCode = aCode;
        this.mcontext = context;
        this.userID = userID;
        this.token = token;

    }

    @Override
    protected void onPreExecute(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mcontext);

        progressDialog = new ProgressDialog(this.mcontext);
        progressDialog.setMessage("Loading your decks.... One sec!");
        progressDialog.show();

    }


    @Override
    protected String doInBackground(Void... urls)  {
        HttpURLConnection urlConnection2 = null;
        URL url2 = null;
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();

        try {

            url2 = new URL("https://developer-paragon.epicgames.com/v1/account/" + userID + "/decks");
            urlConnection2 = (HttpURLConnection) url2.openConnection();
            urlConnection2.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
            urlConnection2.addRequestProperty(Constants.AUTH_VAR, "Bearer " + token);

            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));

            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                stringBuilder2.append(line2).append("\n");
            }
            bufferedReader2.close();
            minDeck = new JSONArray(stringBuilder2.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return minDeck.toString();
    }

    @Override
    protected void onPostExecute(String response){

    }
}
