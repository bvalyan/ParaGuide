package com.optimalotaku.paraguide;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Brandon on 12/19/16.
 */

public class ParagonAPICardInfo extends AsyncTask<Void, Void, String> {

    public CardInfoResponse delegate = null;

    @Override
    protected String doInBackground(Void... voids) {
        URL url = null;
        try {
            url = new URL("https://developer-paragon.epicgames.com/v1/cards/complete");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
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

        List<CardData> cardList = new ArrayList<>();

        if (response == null) {
            Log.i("INFO", "CARD DATABASE NOT FOUND!");
        } else {

            String slotType;

            try {
                JSONArray cardArray = new JSONArray(response);
                JSONArray effectArray;
                JSONArray maxEffectArray;
                for (int i = 0; i < cardArray.length(); i++) {
                    CardData cData = new CardData();
                    JSONObject card = cardArray.getJSONObject(i);
                    effectArray = card.getJSONArray("effects");
                    maxEffectArray = card.getJSONArray("maxedEffects");
                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Name: " + card.getString("name"));
                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card ID: " + card.getString("id"));
                    cData.setName(card.getString("name"));
                    cData.setId(card.getString("id"));

                    slotType = card.getString("slotType");
                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Slot Type: " + card.getString("slotType"));

                    List<CardEffect> cEffectList = new ArrayList<>();
                    List<CardEffect> cMaxEffectList = new ArrayList<>();

                    for (int j = 0; j < effectArray.length(); j++) {
                        CardEffect cEffect = new CardEffect();
                        JSONObject effectdisplay = effectArray.getJSONObject(j);
                        if (slotType.equals("Active")) {
                            cData.setSlot(CardData.SlotType.ACTIVE);

                            Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Effect Desc: " + effectdisplay.getString("description"));
                            Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Effect Cooldown: " + effectdisplay.getString("cooldown"));
                            cEffect.setDescription(effectdisplay.getString("description"));
                            cEffect.setCooldown(effectdisplay.getString("cooldown"));

                        }
                        else if(slotType.equals("Passive")){
                            cData.setSlot(CardData.SlotType.PASSIVE);
                        }
                        else if(slotType.equals("Prime")){
                            cData.setSlot(CardData.SlotType.PRIME);

                        }
                        else if(slotType.equals("Upgrade")){
                            cData.setSlot(CardData.SlotType.UPGRADE);
                        }
                        else{
                            cData.setSlot(CardData.SlotType.UNKNOWN);
                        }

                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Effect Stat: " + effectdisplay.getString("stat"));
                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Effect Value: " + effectdisplay.getString("value"));
                        cEffect.setDescription(effectdisplay.getString("stat"));
                        cEffect.setCooldown(effectdisplay.getString("value"));

                        cEffectList.add(cEffect);

                    }

                    cData.setEffectList(cEffectList);


                    for (int j = 0; j < maxEffectArray.length(); j++) {
                        CardEffect cEffect = new CardEffect();
                        JSONObject effectdisplay = effectArray.getJSONObject(j);

                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Max Effect Stat: " + effectdisplay.getString("stat"));
                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Max Effect Value: " + effectdisplay.getString("value"));
                        cEffect.setDescription(effectdisplay.getString("stat"));
                        cEffect.setCooldown(effectdisplay.getString("value"));

                        cMaxEffectList.add(cEffect);

                    }

                    cData.setMaxEffectList(cMaxEffectList);


                    cardList.add(cData);


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        delegate.processCardInfoFinish(cardList);

    }
}
