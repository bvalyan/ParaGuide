package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

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

    public static HashMap<Integer, ArrayList<ItemObject>> readItemsFromStorage(Context context) throws IOException{
        HashMap<Integer,ArrayList<ItemObject>>  championDataList = new HashMap<Integer,ArrayList<ItemObject>>();
        FileInputStream fis;
        HashMap<Integer,ArrayList<ItemObject>> returnlist = new HashMap<Integer,ArrayList<ItemObject>> ();

        Log.i("INFO", "FileManager - readChampsFromStorage: Attempting  to retrieve Hero data from device");
        try {
            fis = context.openFileInput("Items");
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnlist = (HashMap<Integer,ArrayList<ItemObject>> ) ois.readObject();
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

}
