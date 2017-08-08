package com.optimalotaku.paraguide;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bvalyan on 8/2/17.
 */

public class APIUserIndividualDecks extends AsyncTask<Void, Void, String> {

    String authCode;
    Context mcontext;
    String token;
    String userID;
    JSONArray minDeck;
    JSONArray deckList = new JSONArray();

    public APIUserIndividualDecks(String aCode, Context context, String userID, String token, JSONArray minDeck) {
         /*
            This constructor takes 4 parameters:
             - Authorization code
         */

        this.minDeck = minDeck;
        this.authCode = aCode;
        this.mcontext = context;
        this.userID = userID;
        this.token = token;

    }


    @Override
    protected String doInBackground(Void... urls) {
        HttpURLConnection urlConnection3 = null;
        StringBuilder stringBuilder3 = new StringBuilder();
        try{
            for(int i = 0; i < minDeck.length(); i++) {
                String deckID = minDeck.getJSONObject(i).getString("id");
                BufferedReader bufferedReader3 = null;
                URL url3 = new URL("https://developer-paragon.epicgames.com/v1/account/" + userID + "/deck/" + deckID);
                urlConnection3 = (HttpURLConnection) url3.openConnection();
                urlConnection3.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
                urlConnection3.addRequestProperty("Accept-Language", "english");
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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

    }
}
