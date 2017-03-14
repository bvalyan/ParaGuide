package com.optimalotaku.paraguide;

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
 * Created by Brandon on 1/20/17.
 */

public class MyCardAPI extends AsyncTask<String, Void, String> {
    private String [] heroName;
    Context mcontext;


    public MyCardAPI(Context context){
        this.mcontext = context;

    }

    @Override
    protected String doInBackground(String... Strings) {
        URL url = null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mcontext);
        String accountID = prefs.getString("ACCOUNT_ID", "NULL");
        String authToken = prefs.getString("TOKEN", "NULL");

        try {
            url = new URL("https://developer-paragon.epicgames.com/v1/account/" + accountID + "/cards");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
            urlConnection.addRequestProperty(Constants.AUTH_VAR, "Bearer " + authToken);
            urlConnection.addRequestProperty("Accept-Language", "english");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            return stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected void onPostExecute(String result){
        try {
            JSONArray myCardArray = new JSONArray(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
