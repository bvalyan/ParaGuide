package com.optimalotaku.paraguide;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Brandon on 1/16/17.
 */

public class ParagonAPIHeroInfo extends AsyncTask<Void, Void, String> {
    public MainActivity delegate = null;



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
                HashMap<String, HeroData> heroStats = new HashMap<>();
                for (int i = 0; i < arr.length(); i++) {
                    Log.i("BP1", arr.getJSONObject(i).getString("name"));
                    HeroData hdata = new HeroData();
                    hdata.setName(arr.getJSONObject(i).getString("name"));
                    hdata.setId(arr.getJSONObject(i).getString("id"));
                    hdata.setVersion(Constants.PARAGON_VERSION);
                    hdata.setAttackType(arr.getJSONObject(i).getString("attack"));
                    hdata.setScale(arr.getJSONObject(i).getString("scale"));
                    hdata.setDifficulty(arr.getJSONObject(i).getInt("difficulty"));
                    hdata.setImageIconURL("https:"+arr.getJSONObject(i).getJSONObject("images").getString("icon"));
                    JSONArray jsonAffinities = arr.getJSONObject(i).getJSONArray("affinities");
                    hdata.setAffinity1(jsonAffinities.getString(0));
                    hdata.setAffinity2(jsonAffinities.getString(1));
                    hdata.setAbilityAttack(arr.getJSONObject(i).getJSONObject("stats").getInt("AbilityAttack"));
                    hdata.setMobility(arr.getJSONObject(i).getJSONObject("stats").getInt("Mobility"));
                    hdata.setBasicAttack(arr.getJSONObject(i).getJSONObject("stats").getInt("BasicAttack"));
                    hdata.setDurability(arr.getJSONObject(i).getJSONObject("stats").getInt("Durability"));
                    JSONArray jsonTraits = arr.getJSONObject(i).getJSONArray("traits");
                    String traits = "";

                    for (int j = 0; j < jsonTraits.length(); j++) {
                        if(j < jsonTraits.length()-1)
                            traits = traits + jsonTraits.getString(j) + ", ";
                        else{
                            traits = traits + jsonTraits.getString(j) + ".";
                        }
                    }
                    hdata.setTraits(traits);
                    HeroSkill primary = new HeroSkill();
                    HeroSkill secondary1 = new HeroSkill();
                    HeroSkill secondary2 = new HeroSkill();
                    HeroSkill secondary3 = new HeroSkill();
                    HeroSkill ultimate = new HeroSkill();
                    JSONArray jsonSkills = arr.getJSONObject(i).getJSONArray("abilities");
                    for (int k = 0; k < jsonSkills.length(); k++) {
                        String type = jsonSkills.getJSONObject(k).getString("type");
                        Integer maxLvl = jsonSkills.getJSONObject(k).getInt("maxLevel");
                        switch (type) {
                            case "Primary":
                                Log.i("INFO","Setting primary skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                Log.i("INFO","Setting primary skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                Log.i("INFO","Setting primary skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                Log.i("INFO","Setting primary skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                primary.setName(jsonSkills.getJSONObject(k).getString("name"));
                                primary.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                primary.setType(jsonSkills.getJSONObject(k).getString("type"));
                                primary.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));
                                primary.setModifiers(gatherSkillModifiers(jsonSkills.getJSONObject(k).getJSONArray("modifiersByLevel")));

                                break;
                            case "Normal":
                                if (secondary1.getName() == null ) {
                                    Log.i("INFO","Setting secondary1 skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                    Log.i("INFO","Setting secondary1 skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    Log.i("INFO","Setting secondary1 skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                    Log.i("INFO","Setting secondary1 skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));


                                    secondary1.setName(jsonSkills.getJSONObject(k).getString("name"));
                                    secondary1.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    secondary1.setType(jsonSkills.getJSONObject(k).getString("type"));
                                    secondary1.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));
                                    secondary1.setModifiers(gatherSkillModifiers(jsonSkills.getJSONObject(k).getJSONArray("modifiersByLevel")));

                                } else if (secondary2.getName() == null) {
                                    Log.i("INFO","Setting secondary2 skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                    Log.i("INFO","Setting secondary2 skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    Log.i("INFO","Setting secondary2 skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                    Log.i("INFO","Setting secondary2 skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                    secondary2.setName(jsonSkills.getJSONObject(k).getString("name"));
                                    secondary2.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    secondary2.setType(jsonSkills.getJSONObject(k).getString("type"));
                                    secondary2.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));
                                    secondary2.setModifiers(gatherSkillModifiers(jsonSkills.getJSONObject(k).getJSONArray("modifiersByLevel")));

                                } else {
                                    Log.i("INFO","Setting secondary3 skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                    Log.i("INFO","Setting secondary3 skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    Log.i("INFO","Setting secondary3 skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                    Log.i("INFO","Setting secondary3 skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                    secondary3.setName(jsonSkills.getJSONObject(k).getString("name"));
                                    secondary3.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    secondary3.setType(jsonSkills.getJSONObject(k).getString("type"));
                                    secondary3.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));
                                    secondary3.setModifiers(gatherSkillModifiers(jsonSkills.getJSONObject(k).getJSONArray("modifiersByLevel")));

                                }
                                break;
                            case "Ultimate":
                                Log.i("INFO","Setting ultimate skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                Log.i("INFO","Setting ultimate skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                Log.i("INFO","Setting ultimate skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                Log.i("INFO","Setting ultimate skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                ultimate.setName(jsonSkills.getJSONObject(k).getString("name"));
                                ultimate.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                ultimate.setType(jsonSkills.getJSONObject(k).getString("type"));
                                ultimate.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));
                                ultimate.setModifiers(gatherSkillModifiers(jsonSkills.getJSONObject(k).getJSONArray("modifiersByLevel")));
                                break;
                            default:
                                Log.i("INFO", "Type: " + type + " does not exist for hero");
                        }
                        hdata.setPrimarySkill(primary);
                        hdata.setSecondarySkillOne(secondary1);
                        hdata.setSecondarySkillTwo(secondary2);
                        hdata.setSecondarySkillThree(secondary3);
                        hdata.setUltimateSkill(ultimate);
                    }

                    heroStats.put(hdata.getName(),hdata);
                }
                delegate.processHeroInfoFinish(heroStats);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public Map<String,List<String>> gatherSkillModifiers(JSONArray skillMods){

        Map<String,List<String>> mods = new HashMap<>();


        for(int i=0; i<skillMods.length(); i++){

            try {
                JSONObject mod = skillMods.getJSONObject(i);
                Iterator<String> keys = mod.keys();

                while( keys.hasNext() ) {
                    String key = keys.next();
                    List<String> modCol =  mods.get(key);
                    if(modCol == null){
                        modCol = new ArrayList<>();
                        modCol.add(Integer.toString(mod.getInt(key)));
                        mods.put(key,modCol);
                    }
                    else{
                        modCol.add(Integer.toString(mod.getInt(key)));
                        mods.put(key,modCol);
                    }
                }


            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return mods;

    }

}
