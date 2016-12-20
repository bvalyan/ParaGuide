package com.optimalotaku.paraguide;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerek on 12/19/2016.
 */

public class ParagonAPIDeckInfo extends AsyncTask<Void, Void, String> {

    private String authCode;
    public DeckInfoResponse delegate = null;

    public ParagonAPIDeckInfo(String aCode) {
         /*
            This constructor takes 1 parameters:
             - Authorization code
         */

        this.authCode = aCode;

    }

    @Override
    protected String doInBackground(Void... urls) {

        String token;
        String accountID;
        String expireTime;
        URL url = null;
        URL url2 = null;
        HttpURLConnection urlConnection = null;
        HttpURLConnection urlConnection2 = null;
        try {
            url = new URL("https://developer-paragon.epicgames.com/v1/auth/token/" + authCode);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
            urlConnection.addRequestProperty(Constants.AUTH_VAR, "Basic " + encode64());
            String testEncode = encode64();
            System.out.println(urlConnection.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder stringBuilder2 = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();

            try {
                JSONObject obj = null;

                obj = new JSONObject(stringBuilder.toString());
                token = obj.getString("token");
                accountID = obj.getString("accountId");
                expireTime = obj.getString("expireTime");
                url2 = new URL("https://developer-paragon.epicgames.com/v1/account/"+accountID+"/decks");
                urlConnection2 = (HttpURLConnection) url2.openConnection();
                urlConnection2.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
                urlConnection2.addRequestProperty(Constants.AUTH_VAR, "Bearer " + token);

                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));

                String line2;
                while ((line2 = bufferedReader2.readLine()) != null) {
                    stringBuilder2.append(line2).append("\n");
                }
                bufferedReader2.close();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return stringBuilder2.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }  finally {
            urlConnection.disconnect();
        }
    }


    protected void onPostExecute(String response){

        List<DeckData> deckList = new ArrayList<>();

        if (response == null) {
            Log.i("INFO", "ACCOUNT ERROR");
        } else {

            Log.i("INFO", response);
            JSONArray deckArray = null;

            try {
                //setContentView(R.layout.heroDataScreen);

                deckArray = new JSONArray(response);
                for(int i = 0; i < deckArray.length(); i++){
                    DeckData dData = new DeckData();
                    JSONObject deck = deckArray.getJSONObject(i);
                    Log.i("INFO", "ParagonAPIDeckInfo - onPostExecute - "+"Deck Name: " + deck.getString("name"));
                    Log.i("INFO", "ParagonAPIDeckInfo - onPostExecute - "+"Deck ID: " + deck.getString("id"));
                    Log.i("INFO", "ParagonAPIDeckInfo - onPostExecute - "+"Deck ID: " + deck.getJSONObject("hero").getString("name"));


                    dData.setDeckID(deck.getString("id"));
                    dData.setDeckName(deck.getString("name"));
                    dData.setHeroName(deck.getJSONObject("hero").getString("name"));



                    deckList.add(dData);

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        delegate.processDeckInfoFinish(deckList);
    }

    private String encode64() {
        String combineForBytes = Constants.CLIENT_ID + ':' + Constants.CLIENT_SECRET;
        String encodedBytes = Base64.encodeToString(combineForBytes.getBytes(), Base64.NO_WRAP);
        return encodedBytes.toString().trim();
    }
}