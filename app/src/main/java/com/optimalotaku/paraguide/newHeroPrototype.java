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
                    JSONArray jsonTraits = arr.getJSONObject(i).getJSONArray("traits");
                    String traits = "";

                    for (int j = 0; j < jsonTraits.length(); j++) {
                        if(j < jsonTraits.length()-1)
                            traits = traits + jsonTraits.getString(j) + ", ";
                        else{
                            traits = traits + jsonTraits.getString(j) + ".";
                        }
                    }
                    heroStats[i].setTraits(traits);
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

                                //Get Modifiers By Level
                                //primary.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
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

                                    //Get Modifiers By Level
                                    //secondary1.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
                                } else if (secondary2.getName() == null) {
                                    Log.i("INFO","Setting secondary2 skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                    Log.i("INFO","Setting secondary2 skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    Log.i("INFO","Setting secondary2 skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                    Log.i("INFO","Setting secondary2 skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                    secondary2.setName(jsonSkills.getJSONObject(k).getString("name"));
                                    secondary2.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    secondary2.setType(jsonSkills.getJSONObject(k).getString("type"));
                                    secondary2.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                    //Get Modifiers By Level
                                    //secondary2.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
                                } else {
                                    Log.i("INFO","Setting secondary3 skill name: "+ jsonSkills.getJSONObject(k).getString("name"));
                                    Log.i("INFO","Setting secondary3 skill desc: "+ jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    Log.i("INFO","Setting secondary3 skill type: "+ jsonSkills.getJSONObject(k).getString("type"));
                                    Log.i("INFO","Setting secondary3 skill icon url: "+ jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                    secondary3.setName(jsonSkills.getJSONObject(k).getString("name"));
                                    secondary3.setDesc(jsonSkills.getJSONObject(k).getString("shortDescription"));
                                    secondary3.setType(jsonSkills.getJSONObject(k).getString("type"));
                                    secondary3.setImageURL("https:"+jsonSkills.getJSONObject(k).getJSONObject("images").getString("icon"));

                                    //Get Modifiers By Level
                                    //secondary3.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
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

                                //Get Modifiers By Level
                                //ultimate.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
                                break;
                            default:
                                Log.i("INFO", "Type: " + type + " does not exist for hero");
                        }
                        heroStats[i].setPrimarySkill(primary);
                        heroStats[i].setSecondarySkillOne(secondary1);
                        heroStats[i].setSecondarySkillTwo(secondary2);
                        heroStats[i].setSecondarySkillThree(secondary3);
                        heroStats[i].setUltimateSkill(ultimate);
                    }
                }
                delegate.processHeroInfoFinish(heroStats);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
