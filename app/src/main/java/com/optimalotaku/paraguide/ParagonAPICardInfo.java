package com.optimalotaku.paraguide;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Brandon on 12/19/16.
 */

public class ParagonAPICardInfo extends AsyncTask<Void, Void, String> {

    public CardInfoResponse delegate = null;

    public ParagonAPICardInfo(){

    }

    @Override
    protected String doInBackground(Void... voids) {
        URL url = null;
        try {
            url = new URL("https://developer-paragon.epicgames.com/v1/cards/complete");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onPostExecute(String response) {

        HashMap<String,List<CardData>> cardMap = new HashMap<>();
        List<CardData> cardList = new ArrayList<>();
        List<CardData> equipCardList = new ArrayList<>();

        if (response == null) {
            Log.i("INFO", "CARD DATABASE NOT FOUND!");
        } else {


            try {
                JSONArray cardArray = new JSONArray(response);
                for (int i = 0; i < cardArray.length(); i++) {
                    CardData cData = new CardData();
                    JSONObject card = cardArray.getJSONObject(i);
                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Name: " + card.getString("name"));
                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card ID: " + card.getString("id"));
                    cData.setName(card.getString("name"));
                    cData.setId(card.getString("id"));

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Paragon Version: " + Constants.PARAGON_VERSION);
                    cData.setVersion(Constants.PARAGON_VERSION);

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Rarity: " + card.getString("rarity"));
                    cData.setRarity(card.getString("rarity"));

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card affinity: " + card.getString("affinity"));
                    cData.setRarity(card.getString("affinity"));

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card trait: " + card.getString("trait"));
                    cData.setTrait(card.getString("trait"));

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card intellectGemCost: " + Integer.toString(card.getInt("intellectGemCost")));
                    cData.setIntellectGemCost(card.getInt("intellectGemCost"));

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card vitalityGemCost: " + Integer.toString(card.getInt("vitalityGemCost")));
                    cData.setVitalityGemCost(card.getInt("vitalityGemCost"));

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card dexterityGemCost: " + Integer.toString(card.getInt("dexterityGemCost")));
                    cData.setDexterityGemCost(card.getInt("dexterityGemCost"));

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card goldCost: " + Integer.toString(card.getInt("goldCost")));
                    cData.setGoldCost(card.getInt("goldCost"));

                    List<CardLevel> cardLevels = new ArrayList<>();
                    JSONArray levelArray = card.getJSONArray("levels");

                    for (int j = 0; j < levelArray.length(); j++) {
                        CardLevel cardLevel = new CardLevel();
                        JSONObject level = levelArray.getJSONObject(j);

                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card level: " + Integer.toString(level.getInt("level")));
                        cardLevel.setLevelNum(level.getInt("level"));

                        JSONObject abilities = level.getJSONArray("abilities").getJSONObject(0);
                        Iterator<String> abilityIter = level.getJSONArray("abilities").getJSONObject(0).keys();
                        CardAbility cardAbility = new CardAbility();
                        List<CardAbility> cardAbilityList = new ArrayList<>();
                        while(abilityIter.hasNext()){
                            String abilityKey = abilityIter.next();
                            Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability key: " + abilityKey);

                            Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability name: " + abilities.getString("name"));
                            cardAbility.setName(abilities.getString("name"));

                            Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability description: " + abilities.getString("description"));
                            cardAbility.setDescription(abilities.getString("description"));

                            Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability cooldown: " + abilities.getString("cooldown"));
                            cardAbility.setCooldown(abilities.getString("cooldown"));

                            Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability manacost: " + abilities.getString("manacost"));
                            cardAbility.setManacost(abilities.getString("manacost"));

                            cardAbilityList.add(cardAbility);

                        }

                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "adding card ability list to card level object");
                        cardLevel.setAbilites(cardAbilityList);

                        //TODO: If Epic ever gives us more than one image for this i may need to do the same logic here as i did above with card levels
                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level image url: " + level.getJSONObject("images").getString("large"));
                        cardLevel.setImageURL(level.getJSONObject("images").getString("large"));


                        cardLevels.add(cardLevel);

                    }

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "adding card levels to card object");
                    cData.setCardLevels(cardLevels);

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "adding card data to card list");
                    cardList.add(cData);

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        cardMap.put("All",cardList);
        delegate.processCardInfoFinish(cardMap);

    }

}

