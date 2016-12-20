package com.optimalotaku.paraguide;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Jerek on 12/14/2016.
 */

public class ParagonAPIHeroInfo extends AsyncTask<Void, Void, String> {

    private Exception exception;
    private int mProgressStatus = 0;
    private ProgressBar pBar;
    private String heroName;
    private String heroID;
    private HeroData heroData;

    public HeroInfoResponse delegate = null;
    
    
    public ParagonAPIHeroInfo(ProgressBar pb, String hName, HeroData hData){
        /*
            This constructor takes 3 parameters:
             - Progress bar object from the main class
             - The name entered by the user from the UI
             - A HeroData object that the data will be loaded into
         */

        this.pBar = pb;
        this.heroName = hName;
        this.heroData = hData;
    }


    protected void onPreExecute() {
        //Show the progress bar while we load the data from EPIC's servers
        pBar.setVisibility(View.VISIBLE);

    }

    protected String doInBackground(Void... urls) {

        Log.i("INFO", "ParagonAPIHeroInfo - doInBackground() hero name given: "+heroName);

        try {
            switch (heroName.toLowerCase()){
                case "rampage"        : heroID = Constants.RAMPAGE_ID;
                    break;
                case "countess"       : heroID = Constants.COUNTESS_ID;
                    break;
                case "murdock"        : heroID = Constants.MURDOCK_ID;
                    break;
                case "gadget"         : heroID = Constants.GADGET_ID;
                    break;
                case "narbash"        : heroID = Constants.NARBASH_ID;
                    break;
                case "khaimera"       : heroID = Constants.KHAIMERA_ID;
                    break;
                case "steel"          : heroID = Constants.STEEL_ID;
                    break;
                case "lt. belica"     : heroID = Constants.LT_BELICA_ID;
                    break;
                case "iggy & scorch"  : heroID = Constants.IGGY_SCORCH_ID;
                    break;
                case "riktor"         : heroID = Constants.RIKTOR_ID;
                    break;
                case "crunch"         : heroID = Constants.CRUNCH_ID;
                    break;
                case "gideon"         : heroID = Constants.GIDEON_ID;
                    break;
                case "kwang"          : heroID = Constants.KWANG_ID;
                    break;
                case "sevarog"        : heroID = Constants.SEVAROG_ID;
                    break;
                case "howitzer"       : heroID = Constants.HOWITZER_ID;
                    break;
                case "twinblast"      : heroID = Constants.TWINBLAST_ID;
                    break;
                case "grim.exe"       : heroID = Constants.GRIM_EXE_ID;
                    break;
                case "dekker"         : heroID = Constants.DEKKER_ID;
                    break;
                case "kallari"        : heroID = Constants.KALLARI_ID;
                    break;
                case "the fey"        : heroID = Constants.THE_FEY_ID;
                    break;
                case "greystone"      : heroID = Constants.GREYSTONE_ID;
                    break;
                case "feng mao"       : heroID = Constants.FENG_MAO_ID;
                    break;
                case "muriel"         : heroID = Constants.MURIEL_ID;
                    break;
                case "sparrow"        : heroID = Constants.SPARROW_ID;
                    break;
                case "grux"           : heroID = Constants.GRUX_ID;
                    break;
                default               : heroID = null;
            }

            if(heroID != null){
                URL url = new URL( "https://developer-paragon.epicgames.com/v1/hero/" + heroID);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty(Constants.API_KEY, Constants.API_VALUE);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            return null;
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {

        if(response == null) {
            Log.i("INFO","HERO NOT FOUND");
            heroData.setEmpty(true);
            delegate.processHeroInfoFinish(heroData);
        }
        else {
            heroData.setEmpty(false);

            //pBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            JSONArray arr = new JSONArray();
            try {
                JSONObject obj = new JSONObject(response);

                heroData.setParagonVersion(Constants.PARAGON_VERSION);
                heroData.setName(obj.getString("name"));
                heroData.setAttackType(obj.getString("attack"));

                //Read the Traits array and make them a comma delimited list
                JSONArray jsonTraits = obj.getJSONArray("traits");
                String traits = "";

                for (int i = 0; i < jsonTraits.length(); i++) {
                    traits = traits + jsonTraits.getString(i);
                }

                heroData.setTraits(traits);

                // End Set Traits

                heroData.setScale(obj.getString("scale"));

                JSONArray jsonAffinities = obj.getJSONArray("affinities");
                Log.i("INFO","ParagonAPIHeroInfo - onPostExecute() - Affinity 1: "+ jsonAffinities.getString(0));
                Log.i("INFO","ParagonAPIHeroInfo - onPostExecute() - Affinity 2: "+ jsonAffinities.getString(1));
                heroData.setAffinity1(jsonAffinities.getString(0));
                heroData.setAffinity2(jsonAffinities.getString(1));

                heroData.setDifficulty(obj.getInt("difficulty"));

                heroData.setMobility(obj.getJSONObject("stats").getInt("Mobility"));
                heroData.setBasicAttack(obj.getJSONObject("stats").getInt("BasicAttack"));
                heroData.setDurability(obj.getJSONObject("stats").getInt("Durability"));
                heroData.setAbilityAttack(obj.getJSONObject("stats").getInt("AbilityAttack"));

                Log.i("INFO","ParagonAPIHeroInfo - onPostExecute() - ImageURL: "+ obj.getJSONObject("images").getString("icon"));
                heroData.setImageIconURL(obj.getJSONObject("images").getString("icon"));


                // Extract Character Skills
                HeroSkill primary = new HeroSkill();
                HeroSkill secondary1 = new HeroSkill();
                HeroSkill secondary2 = new HeroSkill();
                HeroSkill secondary3 = new HeroSkill();
                HeroSkill ultimate = new HeroSkill();

                JSONArray jsonSkills = obj.getJSONArray("abilities");

                for (int i = 0; i < jsonSkills.length(); i++) {
                    String type = jsonSkills.getJSONObject(i).getString("type");
                    Integer maxLvl = jsonSkills.getJSONObject(i).getInt("maxLevel");
                    switch (type) {
                        case "Primary":
                            Log.i("INFO","Setting primary skill name: "+ jsonSkills.getJSONObject(i).getString("name"));
                            Log.i("INFO","Setting primary skill desc: "+ jsonSkills.getJSONObject(i).getString("shortDescription"));
                            Log.i("INFO","Setting primary skill type: "+ jsonSkills.getJSONObject(i).getString("type"));
                            Log.i("INFO","Setting primary skill icon url: "+ jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                            primary.setName(jsonSkills.getJSONObject(i).getString("name"));
                            primary.setDesc(jsonSkills.getJSONObject(i).getString("shortDescription"));
                            primary.setType(jsonSkills.getJSONObject(i).getString("type"));
                            primary.setImageURL(jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                            //Get Modifiers By Level
                            //primary.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
                            break;
                        case "Normal":
                            if (secondary1.getName() == null ) {
                                Log.i("INFO","Setting secondary1 skill name: "+ jsonSkills.getJSONObject(i).getString("name"));
                                Log.i("INFO","Setting secondary1 skill desc: "+ jsonSkills.getJSONObject(i).getString("shortDescription"));
                                Log.i("INFO","Setting secondary1 skill type: "+ jsonSkills.getJSONObject(i).getString("type"));
                                Log.i("INFO","Setting secondary1 skill icon url: "+ jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                                secondary1.setName(jsonSkills.getJSONObject(i).getString("name"));
                                secondary1.setDesc(jsonSkills.getJSONObject(i).getString("shortDescription"));
                                secondary1.setType(jsonSkills.getJSONObject(i).getString("type"));
                                secondary1.setImageURL(jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                                //Get Modifiers By Level
                                //secondary1.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
                            } else if (secondary2.getName() == null) {
                                Log.i("INFO","Setting secondary2 skill name: "+ jsonSkills.getJSONObject(i).getString("name"));
                                Log.i("INFO","Setting secondary2 skill desc: "+ jsonSkills.getJSONObject(i).getString("shortDescription"));
                                Log.i("INFO","Setting secondary2 skill type: "+ jsonSkills.getJSONObject(i).getString("type"));
                                Log.i("INFO","Setting secondary2 skill icon url: "+ jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                                secondary2.setName(jsonSkills.getJSONObject(i).getString("name"));
                                secondary2.setDesc(jsonSkills.getJSONObject(i).getString("shortDescription"));
                                secondary2.setType(jsonSkills.getJSONObject(i).getString("type"));
                                secondary2.setImageURL(jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                                //Get Modifiers By Level
                                //secondary2.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
                            } else {
                                Log.i("INFO","Setting secondary3 skill name: "+ jsonSkills.getJSONObject(i).getString("name"));
                                Log.i("INFO","Setting secondary3 skill desc: "+ jsonSkills.getJSONObject(i).getString("shortDescription"));
                                Log.i("INFO","Setting secondary3 skill type: "+ jsonSkills.getJSONObject(i).getString("type"));
                                Log.i("INFO","Setting secondary3 skill icon url: "+ jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                                secondary3.setName(jsonSkills.getJSONObject(i).getString("name"));
                                secondary3.setDesc(jsonSkills.getJSONObject(i).getString("shortDescription"));
                                secondary3.setType(jsonSkills.getJSONObject(i).getString("type"));
                                secondary3.setImageURL(jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                                //Get Modifiers By Level
                                //secondary3.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
                            }
                            break;
                        case "Ultimate":
                            Log.i("INFO","Setting ultimate skill name: "+ jsonSkills.getJSONObject(i).getString("name"));
                            Log.i("INFO","Setting ultimate skill desc: "+ jsonSkills.getJSONObject(i).getString("shortDescription"));
                            Log.i("INFO","Setting ultimate skill type: "+ jsonSkills.getJSONObject(i).getString("type"));
                            Log.i("INFO","Setting ultimate skill icon url: "+ jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                            ultimate.setName(jsonSkills.getJSONObject(i).getString("name"));
                            ultimate.setDesc(jsonSkills.getJSONObject(i).getString("shortDescription"));
                            ultimate.setType(jsonSkills.getJSONObject(i).getString("type"));
                            ultimate.setImageURL(jsonSkills.getJSONObject(i).getJSONObject("images").getString("icon"));

                            //Get Modifiers By Level
                            //ultimate.setModifiers(getModifierMap(jsonSkills.getJSONObject(i), maxLvl));
                            break;
                        default:
                            Log.i("INFO", "Type: " + type + " does not exist for hero - " + heroData.getName());
                    }
                }
                // Extract Character Skills
                heroData.setPrimarySkill(primary);
                heroData.setSecondarySkillOne(secondary1);
                heroData.setSecondarySkillTwo(secondary2);
                heroData.setSecondarySkillThree(secondary3);
                heroData.setUltimateSkill(ultimate);

            /*responseView.setText(mobilityStatement);
            responseView.append("\n");
            responseView.append(basicAttackStatement);
            responseView.append("\n");
            responseView.append(durabilityStatement);
            responseView.append("\n");
            responseView.append(abilityAttackStatement);*/


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //responseView.setText(response);
        }

        delegate.processHeroInfoFinish(heroData);
    }
    
    public HashMap<Integer,Double[]> getModifierMap(JSONObject skill, Integer maxLvl ){

        //Get Modifiers By Level
        try {
            maxLvl = skill.getInt("maxLevel");
            Double[] vals = new Double[2];
            HashMap<Integer, Double[]> modifiers = new HashMap<Integer, Double[]>();

            for (int j = 0; j < maxLvl; j++) {

                Log.i("INFO","skill: "+ skill.getString("name")+" lvl: "+Integer.toString(j)+" damage mod: "+ skill.getJSONArray("modifiersByLevel").getJSONObject(j).getDouble("damage")+" cooldown mod: "+skill.getJSONArray("modifiersByLevel").getJSONObject(j).getDouble("cooldown")+"\n");

                vals[0] = skill.getJSONArray("modifiersByLevel").getJSONObject(j).getDouble("damage");
                vals[1] = skill.getJSONArray("modifiersByLevel").getJSONObject(j).getDouble("cooldown");

                modifiers.put(j, vals);
            }

            return modifiers;
            
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        
    }
}
