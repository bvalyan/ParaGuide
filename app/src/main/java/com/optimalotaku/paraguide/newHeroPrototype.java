package com.optimalotaku.paraguide;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Brandon on 1/16/17.
 */

public class newHeroPrototype extends AsyncTask<Void, Void, String> {
    public HeroView delegate = null;



    @Override
    protected String doInBackground(Void... voids) {

        try {
            URL url = new URL( "https://developer-paragon.epicgames.com/v1/heroes/complete");
            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected void onPostExecute(String response){

        if(response == null) {
            Log.i("INFO","HERO NOT FOUND");
        }
        else {
            Log.i("INFO", response);

            try {
                JSONArray arr = new JSONArray(response);
                HeroData [] heroStats = new HeroData[arr.length()];
                for (int i = 0; i < arr.length(); i++) {
                    Log.i("BP1", arr.getJSONObject(i).getString("name"));
                    heroStats[i] = new HeroData();
                    heroStats[i].setName(arr.getJSONObject(i).getString("name"));
                    heroStats[i].setId(arr.getJSONObject(i).getString("id"));
                    heroStats[i].setAttackType(arr.getJSONObject(i).getString("attack"));
                    heroStats[i].setDifficulty(arr.getJSONObject(i).getInt("difficulty"));
                    heroStats[i].setImageIconURL("https:"+arr.getJSONObject(i).getJSONObject("images").getString("icon"));
                    JSONArray jsonAffinities = arr.getJSONObject(i).getJSONArray("affinities");
                    heroStats[i].setAffinity1(jsonAffinities.getString(0));
                    heroStats[i].setAffinity2(jsonAffinities.getString(1));
                    heroStats[i].setAbilityAttack(arr.getJSONObject(i).getJSONObject("stats").getInt("AbilityAttack"));
                    heroStats[i].setMobility(arr.getJSONObject(i).getJSONObject("stats").getInt("Mobility"));
                    heroStats[i].setBasicAttack(arr.getJSONObject(i).getJSONObject("stats").getInt("BasicAttack"));
                    heroStats[i].setDurability(arr.getJSONObject(i).getJSONObject("stats").getInt("Durability"));
                }
                delegate.processHeroInfoFinish(heroStats);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
