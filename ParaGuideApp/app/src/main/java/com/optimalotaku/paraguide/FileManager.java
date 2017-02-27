package com.optimalotaku.paraguide;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jerek on 1/25/2017.
 */

public class FileManager {
    private Context context;

    public FileManager(Context context){

        this.context = context;
    }




    public HashMap<String,List<CardData>> readCardsFromStorage() throws IOException{
        HashMap<String,List<CardData>> cDataMap = new HashMap<>();
        List<CardData> cList = new ArrayList<>();
        List<CardData> equipCList = new ArrayList<>();

        Log.i("INFO", "FileManager - readCardsFromStorage: Attempting to read card data from device ");

        //Get path of Internal Storage
        File path = context.getFilesDir();


        File file = new File(path,"card.data");
        int length = (int) file.length();
        byte[] bytes = new byte[length];

        if(file.exists()) {

            FileInputStream stream = new FileInputStream(file);
            stream.read(bytes);

            String cardListStr = new String(bytes);
            Gson gson = new Gson();

            try {
                JSONArray cardArray = new JSONArray(cardListStr);
                for (int i = 0; i < cardArray.length(); i++) {
                    CardData card = new CardData();
                    card = gson.fromJson(cardArray.getJSONObject(i).toString(), CardData.class);
                    if (card.getSlot() == CardData.SlotType.ACTIVE || card.getSlot() == CardData.SlotType.PASSIVE) {
                        equipCList.add(card);
                    }
                    cList.add(card);

                }

                cDataMap.put("Equip", equipCList);
                cDataMap.put("All", cList);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("INFO", "FileManager - readCardsFromStorage: cards.data opened successfully ");
        }
        else{
            Log.i("INFO", "FileManager - readCardsFromStorage: cards.data does not exist, returning empty card data map");
        }

        return cDataMap;


    }

    public void saveCardsToStorage(List<CardData> cList) throws IOException{

        File path = context.getFilesDir();
        File file = new File(path, "card.data");

        Log.i("INFO", "FileManager - saveCardsToStorage: Attempting to save card data to device ");

        FileOutputStream stream = new FileOutputStream(file);

        Gson gson = new Gson();
        String heroData = gson.toJson(cList);
        stream.write(heroData.getBytes());

        stream.close();


        Log.i("INFO", "FileManager - saveCardsToStorage: Card data saved to device successfully");



    }


    public boolean isLatestCardData(HashMap<String,List<CardData>> cDataMap){

        if(cDataMap.isEmpty() || cDataMap.get("All").isEmpty() || cDataMap.get("Equip").isEmpty()){
            Log.i("INFO", "FileManager - isLatestCardData: Card Map is empty or All Card list or Equip Card list is empty. Returning false");
            return false;
        }
        else{
            CardData test = cDataMap.get("All").get(0);
            if(test.getVersion() < Constants.PARAGON_VERSION){
                Log.i("INFO", "FileManager - isLatestCardData: Current version: "+Double.toString(Constants.PARAGON_VERSION)
                             +" Saved Version: "+Double.toString(test.getVersion())+" Returning false");
                return false;
            }
            else{
                Log.i("INFO", "FileManager - isLatestCardData: Current version: "+Double.toString(Constants.PARAGON_VERSION)
                        +" Saved Version: "+Double.toString(test.getVersion())+" Returning true");
                return true;
            }
        }
    }


    public HashMap<String,HeroData> readHeroFromStorage() throws IOException {

        HashMap<String,HeroData> hDataMap = new HashMap<>();
        //Get path of Internal Storage
        File path = context.getFilesDir();

        Log.i("INFO", "FileManager - readHeroesToStorage: Attempting  to retrieve Hero data from device");

        File file = new File(path,"hero.data");
        int length = (int) file.length();
        byte[] bytes = new byte[length];


        if(file.exists()) {
            FileInputStream stream = new FileInputStream(file);
            stream.read(bytes);

            String heroListStr = new String(bytes);
            Gson gson = new Gson();

            try {
                JSONArray heroArray = new JSONArray(heroListStr);
                for (int i = 0; i < heroArray.length(); i++) {
                    HeroData hero = new HeroData();
                    hero = gson.fromJson(heroArray.getJSONObject(i).toString(), HeroData.class);

                    hDataMap.put(hero.getName(), hero);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("INFO", "FileManager - readHeroesToStorage: Hero data obtained from device successfully");
        }
        else{
            Log.i("INFO", "FileManager - readHeroesToStorage: hero.data does not exist, returning empty hero data map");
        }
        return hDataMap;


    }

    public void saveHeroesToStorage( HashMap<String,HeroData> hData) throws IOException {
        File path = context.getFilesDir();
        List<HeroData> hList = new ArrayList<>();

        Log.i("INFO", "FileManager - saveHeroesToStorage: Attempting to save Hero data to device");

        Set<String> keys = hData.keySet();

        for(String key: keys) {
            HeroData hero = hData.get(key);
            hList.add(hero);
        }

        File file = new File(path, "hero.data");
        FileOutputStream stream = new FileOutputStream(file);

        Gson gson = new Gson();
        String heroData = gson.toJson(hList);
        stream.write(heroData.getBytes());

        stream.close();

        Log.i("INFO", "FileManager - saveHeroesToStorage: Hero data (hero.data) saved to device successfully");


    }

    public boolean isLatestHeroData(HashMap<String,HeroData> heroMap){
        if(heroMap.isEmpty()){
            Log.i("INFO", "FileManager - isLatestHeroData: Hero Map is empty. Returning false");
            return false;
        }
        else {
            HeroData test =  heroMap.get("TwinBlast");
            if (test.getVersion() < Constants.PARAGON_VERSION) {
                Log.i("INFO", "FileManager - isLatestHeroData: Current version: "+Double.toString(Constants.PARAGON_VERSION)
                        +" Saved Version: "+Double.toString(test.getVersion())+" Returning false");
                return false;
            }
            else {
                Log.i("INFO", "FileManager - isLatestHeroData: Current version: "+Double.toString(Constants.PARAGON_VERSION)
                        +" Saved Version: "+Double.toString(test.getVersion())+" Returning True");
                return true;
            }
        }

    }




}
