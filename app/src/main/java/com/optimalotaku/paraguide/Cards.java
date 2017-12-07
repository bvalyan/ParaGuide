package com.optimalotaku.paraguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by bvalyan on 8/2/17.
 */

public class Cards extends Fragment {

    GridView gridview;
    FileManager cardmanager;
    static CardData incCard;
    static String selectedAffinity;
    static  String prevName;
    String TAG = Cards.class.getSimpleName();

    public static Cards newInstance(CardData cotd, String affinity, String oldName) {

        Bundle args = new Bundle();
        incCard = cotd;
        Cards fragment = new Cards();
        fragment.setArguments(args);
        selectedAffinity = affinity;
        prevName = oldName;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final Intent intent;
        View view = inflater.inflate(R.layout.cardlist2, container, false);
        cardmanager = new FileManager(getContext());
        Map<String,List<CardData>> cDataMap = new HashMap<>();
        try {
            cDataMap = cardmanager.readCardsFromStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Map<String, List<CardData>> finalCDataMap = cDataMap;
        gridview = (GridView) view.findViewById(R.id.gridview2);
        final SharedPreferences prefs = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = getActivity().getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        String authCode = prefs.getString("signedIn", "null");
        APICardList allCards = new APICardList(authCode, getActivity());
        JSONArray cardArray = null;
        try {
            String response = allCards.execute().get();
            cardArray = new JSONArray(response);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        final CardData[] cardList = new CardData[cardArray.length()];
        for(int j = 0; j<cardArray.length();j++){
            cardList[j] = new CardData();
        }

        try {
            for (int i = 0; i < cardArray.length(); i++) {
                cardList[i].setId(cardArray.getJSONObject(i).getString("id"));
                cardList[i].setName(cardArray.getJSONObject(i).getString("name"));
                cardList[i].setAffinity(cardArray.getJSONObject(i).getString("affinity"));
                Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card Rarity: " + cardArray.getJSONObject(i).getString("rarity"));
                cardList[i].setRarity(cardArray.getJSONObject(i).getString("rarity"));

                Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card affinity: " + cardArray.getJSONObject(i).getString("affinity"));
                cardList[i].setAffinity(cardArray.getJSONObject(i).getString("affinity"));

                Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card trait: " + cardArray.getJSONObject(i).getString("trait"));
                cardList[i].setTrait(cardArray.getJSONObject(i).getString("trait"));

                Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card intellectGemCost: " + Integer.toString(cardArray.getJSONObject(i).getInt("intellectGemCost")));
                cardList[i].setIntellectGemCost(cardArray.getJSONObject(i).getInt("intellectGemCost"));

                Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card vitalityGemCost: " + Integer.toString(cardArray.getJSONObject(i).getInt("vitalityGemCost")));
                cardList[i].setVitalityGemCost(cardArray.getJSONObject(i).getInt("vitalityGemCost"));

                Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card dexterityGemCost: " + Integer.toString(cardArray.getJSONObject(i).getInt("dexterityGemCost")));
                cardList[i].setDexterityGemCost(cardArray.getJSONObject(i).getInt("dexterityGemCost"));

                Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card goldCost: " + Integer.toString(cardArray.getJSONObject(i).getInt("goldCost")));
                cardList[i].setGoldCost(cardArray.getJSONObject(i).getInt("goldCost"));

                List<CardLevel> cardLevels = new ArrayList<>();
                JSONArray levelArray = cardArray.getJSONObject(i).getJSONArray("levels");

                for (int j = 0; j < levelArray.length(); j++) {
                    CardLevel cardLevel = new CardLevel();
                    JSONObject level = levelArray.getJSONObject(j);

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "Card level: " + Integer.toString(level.getInt("level")));
                    cardLevel.setLevelNum(level.getInt("level"));

                    JSONObject abilities = level.getJSONArray("abilities").getJSONObject(0);
                    Iterator<String> abilityIter = level.getJSONArray("abilities").getJSONObject(0).keys();
                    CardAbility cardAbility = new CardAbility();
                    List<CardAbility> cardAbilityList = new ArrayList<>();
                    while(abilityIter.hasNext()){
                        String abilityKey = abilityIter.next();
                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability key: " + abilityKey);

                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability name: " + abilities.getString("name"));
                        cardAbility.setName(abilities.getString("name"));

                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability description: " + abilities.getString("description"));
                        cardAbility.setDescription(abilities.getString("description"));

                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability cooldown: " + abilities.getString("cooldown"));
                        cardAbility.setCooldown(abilities.getString("cooldown"));

                        Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level ability manacost: " + abilities.getString("manacost"));
                        cardAbility.setManacost(abilities.getString("manacost"));

                        cardAbilityList.add(cardAbility);

                    }

                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "adding card ability list to card level object");
                    cardLevel.setAbilites(cardAbilityList);

                    //TODO: If Epic ever gives us more than one image for this i may need to do the same logic here as i did above with card levels
                    Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "level image url: " + level.getJSONObject("images").getString("large"));
                    cardLevel.setImageURL(level.getJSONObject("images").getString("large"));

                    cardLevels.add(cardLevel);

                }

                Log.i("INFO", "ParagonAPICardInfo - onPostExecute - "+ "adding card levels to card object");
                cardList[i].setCardLevels(cardLevels);


                cardList[i].setImageUrl(cardArray.getJSONObject(i).getJSONArray("levels").getJSONObject(0).getJSONObject("images").getString("large"));
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        CustomComparator cardCompare = new CustomComparator();
        Arrays.sort(cardList, cardCompare);

        final ArrayList<CardData> newCards = new ArrayList<>();
        for(int i = 0; i < cardList.length; i++){
            if(cardList[i].getAffinity().toLowerCase().trim().equals(selectedAffinity.toLowerCase().trim())){
                newCards.add(cardList[i]);
            }
        }
        MyDeckAdapter CardAdapter = new MyDeckAdapter(getActivity(), cardList, selectedAffinity, prevName);

        gridview.setAdapter(CardAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gridview.playSoundEffect(SoundEffectConstants.CLICK); //send feedback on main drawer
                gridview.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


                //Toast.makeText(getApplicationContext(), "You Clicked " +cardList[+ position].getName(), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < newCards.size(); i++){
                    if(newCards.get(i).getName().equals(newCards.get(position).getName())){
                        CardData finalChoice = newCards.get(i);
                        finalChoice.setBareImageUrl(newCards.get(position).getImageUrl());
                        android.app.DialogFragment newFragment = CardDisplay.newInstance(finalChoice);
                        newFragment.show(getActivity().getFragmentManager(), "dialog");

                        /*getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, CardDisplay.newInstance(finalChoice))
                                .addToBackStack("NEW")
                                .commit();*/
                    }
                }
            }
        });

        return view;

    }
    public class CustomComparator implements Comparator<CardData> {
        @Override
        public int compare(CardData o1, CardData o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

}
