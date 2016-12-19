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
import java.util.HashMap;

/**
 * Created by Brandon on 12/19/16.
 */

public class cardOfTheDay extends AppCompatActivity {

    private HashMap<String, String> cardMap = new HashMap<String, String>();

    public class CardPull extends AsyncTask<Void,Void,String> {



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

            if (response == null) {
                Log.i("INFO", "CARD DATABASE NOT FOUND!");
            } else {
                String cardID;
                String cardName = null;
                String description;
                String slotType;
                String cardStat;
                String statValue;
                String cooldown;
                TextView cardDay = (TextView) findViewById(R.id.cardOfTheDayText);
                try {
                    JSONArray cardArray = new JSONArray(response);
                    JSONArray effectArray = new JSONArray();
                    for (int i = 0; i < cardArray.length(); i++) {
                        JSONObject card = cardArray.getJSONObject(i);
                        effectArray = card.getJSONArray("effects");
                        cardName = "Card Name: " + card.getString("name");
                        cardID = "Card ID: " + card.getString("id");
                        slotType = card.getString("slotType");
                        for (int j = 0; j < effectArray.length(); j++) {
                            JSONObject effectdisplay = effectArray.getJSONObject(j);
                            if (slotType.equals("Active")) {
                                description = "Card Description: " + effectdisplay.getString("description");
                                cooldown = "Cooldown: " + effectdisplay.getString("cooldown");
                            }
                            cardStat = effectdisplay.getString("stat");
                            statValue = effectdisplay.getString("value");

                            cardDay.setText(cardName);
                            cardDay.append(cardStat);
                            cardDay.append(statValue);

                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
