package com.optimalotaku.paraguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

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
    private Context mcontext;
    public DeckInfoResponse delegate = null;
    Activity activity;
    String accountID;
    JSONArray minDeck;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;



    public ParagonAPIDeckInfo(String aCode, Context context) {
         /*
            This constructor takes 1 parameters:
             - Authorization code
         */

        this.authCode = aCode;
        this.mcontext = context;

    }


    @Override
    protected void onPreExecute(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mcontext);
        editor = prefs.edit();

    }

    @Override
    protected String doInBackground(Void... urls) {

        String token = null;

        String expireTime;
        URL url = null;
        URL url2 = null;
        HttpURLConnection urlConnection = null;
        HttpURLConnection urlConnection2 = null;
        HttpURLConnection urlConnection3 = null;
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();
        JSONArray deckList = new JSONArray();
        try {
            url = new URL("https://developer-paragon.epicgames.com/v1/auth/token/" + authCode);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
            urlConnection.addRequestProperty(Constants.AUTH_VAR, "Basic " + encode64());
            String testEncode = encode64();
            System.out.println(urlConnection.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();

            try {
                JSONObject obj = null;

                obj = new JSONObject(stringBuilder.toString());

                Log.i("INFO", "ParagonAPIDeckInfo - doInBackground - " + "Auth Token: " + obj.getString("token"));
                Log.i("INFO", "ParagonAPIDeckInfo - doInBackground - " + "Account ID: " + obj.getString("accountId"));
                Log.i("INFO", "ParagonAPIDeckInfo - doInBackground - " + "Expire Time: " + obj.getString("expireTime"));
                token = obj.getString("token");
                accountID = obj.getString("accountId");
                expireTime = obj.getString("expireTime");
                editor.putString("TOKEN", token);
                editor.putString("ACCOUNT_ID", accountID);
                editor.apply();

                url2 = new URL("https://developer-paragon.epicgames.com/v1/account/" + accountID + "/decks");
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
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        try {

            for(int i = 0; i < minDeck.length(); i++) {
                String deckID = minDeck.getJSONObject(i).getString("id");
                BufferedReader bufferedReader3 = null;
                URL url3 = new URL("https://developer-paragon.epicgames.com/v1/account/" + accountID + "/deck/" + deckID);
                urlConnection3 = (HttpURLConnection) url3.openConnection();
                urlConnection3.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
                urlConnection3.addRequestProperty(Constants.AUTH_VAR, "Bearer " + token);
                try {
                     bufferedReader3 = new BufferedReader(new InputStreamReader(urlConnection3.getInputStream()));
                }
                catch (IOException e){
                    //If deck cannot be found it is not there. Skip it.
                    e.printStackTrace();
                    continue;
                }
                String line3;
                while ((line3 = bufferedReader3.readLine()) != null) {
                    stringBuilder3.append(line3).append("\n");
                    JSONObject tempDeck = new JSONObject(line3);
                    deckList.put(tempDeck);
                }
                bufferedReader3.close();
            }

    } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            urlConnection3.disconnect();
        }
       return deckList.toString();
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

                JSONArray finalDeckArray = new JSONArray(response);
                for(int i = 0; i < finalDeckArray.length(); i++){
                    DeckData dData = new DeckData();
                    JSONObject deck = finalDeckArray.getJSONObject(i);
                    if(deck.has("name")) {
                        Log.i("INFO", "ParagonAPIDeckInfo - onPostExecute - " + "Deck Name: " + deck.getString("name"));
                        Log.i("INFO", "ParagonAPIDeckInfo - onPostExecute - " + "Deck ID: " + deck.getString("id"));
                        Log.i("INFO", "ParagonAPIDeckInfo - onPostExecute - " + "Deck ID: " + deck.getJSONObject("hero").getString("name"));


                        dData.setDeckID(deck.getString("id"));
                        dData.setDeckName(deck.getString("name"));
                        dData.setHeroName(deck.getJSONObject("hero").getString("name"));
                        dData.setDeckContents(deck.getString("cards"));

                        deckList.add(dData);
                    }

                }

                Log.i("INFO", "HERE");




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            delegate.processDeckInfoFinish(deckList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String encode64() {
        String combineForBytes = Constants.CLIENT_ID + ':' + Constants.CLIENT_SECRET;
        String encodedBytes = Base64.encodeToString(combineForBytes.getBytes(), Base64.NO_WRAP);
        return encodedBytes.toString().trim();
    }
}