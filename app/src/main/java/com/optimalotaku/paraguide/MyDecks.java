package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvalyan on 8/2/17.
 */

public class MyDecks extends AppCompatActivity {

    String authCode = "";
    String token = "";
    String userID = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = getSharedPreferences("authInfo", MODE_PRIVATE);
        final SharedPreferences.Editor e = getSharedPreferences("authInfo",Context.MODE_PRIVATE).edit();

        authCode = prefs.getString("signedIn", "null");
        userID = prefs.getString("ACCOUNT_ID", "null");
        token = prefs.getString("TOKEN", "null");
        JSONArray parseCards = new JSONArray();

        APIUserDeckList decks = new APIUserDeckList(authCode, this, userID, token);
        try {
            parseCards = new JSONArray(decks.execute().get());
            APIUserIndividualDecks allDecks = new APIUserIndividualDecks(authCode, this, userID, token, parseCards);
            String response = allDecks.execute().get();
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

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }


    }


}
