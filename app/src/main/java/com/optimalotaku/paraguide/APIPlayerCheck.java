package com.optimalotaku.paraguide;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bvaly on 2/9/2017.
 */
public class APIPlayerCheck extends AsyncTask<String, Void, String>{
    @Override
    protected String doInBackground(String... Strings) {
        URL url2 = null;
        HttpURLConnection urlConnection2 = null;
        StringBuilder stringBuilder2 = new StringBuilder();
        String playerName = Strings[0];

        try {
            url2 = new URL("https://developer-paragon.epicgames.com/v1/accounts/find/" + playerName);
            urlConnection2 = (HttpURLConnection) url2.openConnection();
            urlConnection2.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);

            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));

            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                stringBuilder2.append(line2);
            }
            bufferedReader2.close(); //search for player data
            String foundAccountID = stringBuilder2.toString();

            return foundAccountID;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(String result){

    }
}
