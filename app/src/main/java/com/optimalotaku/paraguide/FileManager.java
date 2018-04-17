package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Jerek on 1/25/2017.
 */

public class FileManager {
    private Context context;
    SharedPreferences prefs;
    RequestQueue mRequestQueue;
    SharedPreferences.Editor e;

    public FileManager(Context context){

        this.context = context;
    }




    public HashMap<String,List<CardData>> readCardsFromStorage() throws IOException{
        HashMap<String,List<CardData>> cDataMap = new HashMap<>();
        List<CardData> cList = new ArrayList<>();
        List<CardData> equipCList = new ArrayList<>();

        // Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

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
                    CardData card;
                    card = gson.fromJson(cardArray.getJSONObject(i).toString(), CardData.class);
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
            Log.i("INFO", "FileManager - readCardsFromStorage: cards.data does not exist, returning empty card data championList");
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

    public static ArrayList<ChampionData> readChampsFromStorage(Context context) throws IOException{
        ArrayList<ChampionData> championDataList = new ArrayList<>();
        FileInputStream fis;
        ArrayList<ChampionData> returnlist = new ArrayList<>();

        Log.i("INFO", "FileManager - readChampsFromStorage: Attempting  to retrieve Hero data from device");
        try {
            fis = context.openFileInput("Champions");
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnlist = (ArrayList<ChampionData>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return championDataList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return returnlist;


    }

    public static ArrayList<ItemObject> readItemsFromStorage(Context context) throws IOException{
        ArrayList<ItemObject> championDataList = new ArrayList<>();
        FileInputStream fis;
        ArrayList<ItemObject> returnlist = new ArrayList<>();

        Log.i("INFO", "FileManager - readChampsFromStorage: Attempting  to retrieve Hero data from device");
        try {
            fis = context.openFileInput("Items");
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnlist = (ArrayList<ItemObject>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return championDataList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return returnlist;


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
            Log.i("INFO", "FileManager - readHeroesToStorage: hero.data does not exist, returning empty hero data championList");
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






}
