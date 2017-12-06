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
                    HeroSkill basic = new HeroSkill();
                    HeroSkill alternate = new HeroSkill();
                    HeroSkill primary = new HeroSkill();
                    HeroSkill secondary = new HeroSkill();
                    HeroSkill ultimate = new HeroSkill();
                    JSONArray jsonSkills = arr.getJSONObject(i).getJSONArray("abilities");
                    for (int k = 0; k < jsonSkills.length(); k++) {
                        String type = jsonSkills.getJSONObject(k).getString("type");
                        Integer maxLvl = jsonSkills.getJSONObject(k).getInt("maxLevel");
                        switch (type) {
                            case "Basic":
                                Log.i("INFO","Setting basic skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                Log.i("INFO","Setting basic skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                Log.i("INFO","Setting basic skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                Log.i("INFO","Setting basic skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                basic.setName(jsonSkills.getJSONObject(k).getString("name"));
                                basic.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                basic.setType(jsonSkills.getJSONObject(k).getString("type"));
                                basic.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));
                                basic.setModifiers(gatherSkillModifiers(jsonSkills.getJSONObject(k).getJSONArray("modifiersByLevel")));
                                break;
                            case "Alternate":
                                Log.i("INFO","Setting alternate skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                Log.i("INFO","Setting alternate skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                Log.i("INFO","Setting alternate skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                Log.i("INFO","Setting alternate skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                alternate.setName(jsonSkills.getJSONObject(k).getString("name"));
                                alternate.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                alternate.setType(jsonSkills.getJSONObject(k).getString("type"));
                                alternate.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));
                                alternate.setModifiers(gatherSkillModifiers(jsonSkills.getJSONObject(k).getJSONArray("modifiersByLevel")));
                                break;
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
                            case "Secondary":
                                Log.i("INFO","Setting secondary skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                Log.i("INFO","Setting secondary skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                Log.i("INFO","Setting secondary skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                Log.i("INFO","Setting secondary skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                secondary.setName(jsonSkills.getJSONObject(k).getString("name"));
                                secondary.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                secondary.setType(jsonSkills.getJSONObject(k).getString("type"));
                                secondary.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));
                                secondary.setModifiers(gatherSkillModifiers(jsonSkills.getJSONObject(k).getJSONArray("modifiersByLevel")));
                                break;
                            case "Ultimate":
                            case "UNKNOWN": //Added extra case because of Wukong
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
                        hdata.setBasicSkill(basic);
                        hdata.setAlternateSkill(alternate);
                        hdata.setPrimarySkill(primary);
                        hdata.setSecondarySkill(secondary);
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
